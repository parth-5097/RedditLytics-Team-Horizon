name := """StarterCode"""
organization := "Deadman"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

libraryDependencies += guice

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "4.3.1"
libraryDependencies += "org.mockito" % "mockito-core" % "2.22.0" % "test"

jacocoExcludes in Test := Seq(
  "controllers.Reverse*",
  "controllers.javascript.*",
  "jooq.*",
  "Module",
  "router.Routes*",
  "*.routes*"
)