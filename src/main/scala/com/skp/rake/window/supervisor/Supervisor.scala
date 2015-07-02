package com.skp.rake.window.supervisor

import akka.actor.{Props, Actor, ActorRef, Terminated}
import com.skp.rake.window.supervisor.Supervisor.CreateJsWorker
import com.skp.rake.window.worker.StaticFileChecker

import scala.concurrent.duration._

class Supervisor extends Actor {

  import context.dispatcher

  var workers = Set.empty[ActorRef]
  val MAX_WORKER_NUM = 10

  context.system.scheduler.scheduleOnce(1 seconds, self, CreateJsWorker)

  override def receive: Receive = {
    case CreateJsWorker =>
      if (workers.size <= MAX_WORKER_NUM) {
        val worker = context.actorOf(Props[StaticFileChecker])
        context.watch(worker)
        workers += worker
      }

    case Terminated =>
      workers -= sender

      if (workers.size == 0)
        self ! CreateJsWorker
  }
}

object Supervisor {
  sealed trait SupervisorEvent
  case object CreateJsWorker extends SupervisorEvent
}
