package com.skp.rake.window.worker

import akka.actor.{ActorLogging, Actor, ActorRef}
import akka.pattern._
import com.skp.rake.window.worker.StaticFileChecker.CheckJsFile
import dispatch._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class StaticFileChecker extends Actor with ActorLogging {

  import context.dispatcher

  context.system.scheduler.schedule(1 second, 1 second, self, CheckJsFile)

  val testScriptPath = "pg.rake.skplanet.com:8443/log/resources/js/rake-skp.test-1.4.min.js"
  val liveScriptPath = "rake.skplanet.com:8443/log/resources/js/rake-skp-1.4.min.js"

  override def receive: Receive = {
    case CheckJsFile =>

    case Success(body: String) => log.debug(body)
    case Failure(e) => log.debug(e.toString)
  }

  def getResponse(url: String) = {
    val req = host(url).secure.GET
    val f: Future[String] = Http(req OK as.String)

    f.pipeTo(self)
  }
}

object StaticFileChecker {
  sealed trait StaticFileCheckerEvent
  case object CheckJsFile
}

