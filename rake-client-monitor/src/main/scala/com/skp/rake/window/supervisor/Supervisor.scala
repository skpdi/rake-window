package com.skp.rake.window.supervisor

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import com.skp.rake.window.supervisor.Supervisor.{Stop, CreateJsWorker}
import com.skp.rake.window.worker.StaticFileChecker

import scala.concurrent.duration._

class Supervisor extends Actor {

  import context.dispatcher

  var workers = Set.empty[ActorRef]
  val MAX_WORKER_NUM = 10

  context.system.scheduler.scheduleOnce(1 seconds, self, CreateJsWorker)

  override val supervisorStrategy = OneForOneStrategy() {
    case _ => Restart
  }

  override def receive: Receive = {
    case CreateJsWorker =>
      if (workers.size <= MAX_WORKER_NUM) {
        val worker = context.actorOf(Props[StaticFileChecker])
        workers += worker
      }

    case Stop =>
      context.children foreach (context.stop(_))
  }
}

object Supervisor {
  sealed trait SupervisorEvent
  case object CreateJsWorker extends SupervisorEvent
  case object Stop extends SupervisorEvent
}
