package com.skp.rake.window.worker

import akka.actor.Status.Failure
import akka.actor.{ActorLogging, Actor, ActorRef}
import akka.pattern._
import com.skp.rake.window.worker.StaticFileChecker.{Error, Exist, Check}
import com.typesafe.config.ConfigFactory
import dispatch._

import scala.concurrent.duration._

class StaticFileChecker extends Actor with ActorLogging {

  val config = ConfigFactory.load("private")
  val testScript = host(config.getString("rake.testHost")).secure / config.getString("rake.testResource")
  val liveScript = host(config.getString("rake.liveHost")).secure / config.getString("rake.liveResource")
  val slack = host(config.getString("slack.host")).secure / config.getString("slack.resource")

  import context.dispatcher

  context.system.scheduler.schedule(1 second, 600 seconds, self, Check)

  override def receive: Receive = {
    case Check =>
      get(testScript)
      get(liveScript)

    case Right(Exist(url)) =>
      post(slack, url, "Exist: ")
      log.debug(s"Exist: $url")

    case Left(Error(url, message)) =>
      post(slack, url, message)
      log.debug(s"$url \n$message")

    case Failure(e) =>
      post(slack, "", e.toString)
      log.debug(s"${e.toString}")
  }

  def get(req: Req) = {
    Http(req OK as.String).either.map(x => {
      x.left.map(t => Error(req.url, t.toString)).right.map(t => Exist(req.url))
    }) pipeTo(self)
  }

  def post(resource: Req, url: String, message: String) = {

    val headers = Map("Content-Type" -> "application/json")

    val body =
      s"""
        |{"channel": "#rake-alert", "username": "rake-window", "text": "${message} ${url}", "icon_emoji": ":sweat:"}
      """.stripMargin;

    val req = resource << body <:< headers

    Http(req OK as.String)
  }
}

object StaticFileChecker {
  sealed trait StaticFileCheckerEvent
  case object Check extends StaticFileCheckerEvent
  case class Exist(url: String) extends StaticFileCheckerEvent
  case class Error(url: String, message: String) extends StaticFileCheckerEvent
}

