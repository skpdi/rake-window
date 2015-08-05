package com.skp.rake.window.app

import akka.actor.{SupervisorStrategy, ActorSystem, Props}
import com.skp.rake.window.supervisor.Supervisor

object RakeWindow extends App {

  val system = ActorSystem("rake-window")
  val supervisor = system.actorOf(Props[Supervisor], "supervisor")

  // TODO system.shutdown() hook to stop the supervisor, supervisor ! Stop
}
