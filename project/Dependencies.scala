import sbt._

object Dependencies {
  val akkaVersion     = "2.4-M2"
  val akkaActor       = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val akkaRemote      = "com.typesafe.akka" %% "akka-cluster" % akkaVersion
  val akkaCluster     = "com.typesafe.akka" %% "akka-cluster" % akkaVersion
  val akkaTestkit     = "com.typesafe.akka" %% "akka-testkit"% akkaVersion
  val akkaPersistence = "com.typesafe.akka" %% "akka-persistence-experimental" % akkaVersion

  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.0-M4"
  val dispatch = "net.databinder.dispatch" %% "dispatch-core" % "0.11.3"

  val akkaDeps = Seq(
    akkaActor,
    akkaTestkit,
    akkaCluster,
    akkaRemote,
    akkaPersistence
  )

  val commonDeps = Seq(
    dispatch,
    scalaTest % Test
  )
}