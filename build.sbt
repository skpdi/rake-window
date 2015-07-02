import Resolvers._
import Dependencies._

name := """rake-window"""

organization := "com.skp"

version := "0.1"

scalaVersion := "2.11.7"

scalacOptions := Seq("-deprecation", "-encoding", "utf8")

fork := true

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

parallelExecution := true

parallelExecution in Test := false


resolvers += mavenCentral

libraryDependencies ++= akkaDeps

libraryDependencies ++= commonDeps
