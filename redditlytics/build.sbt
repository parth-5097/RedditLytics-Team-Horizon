name := """StarterCode"""
organization := "Deadman"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

libraryDependencies += guice

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "4.3.1"
libraryDependencies += "org.mockito" % "mockito-core" % "3.12.4" % "test"

jacocoExcludes in Test := Seq(
  "controllers.Reverse*",
  "controllers.javascript.*",
  "jooq.*",
  "Module",
  "router.Routes*",
  "*.routes*"
)

libraryDependencies ++= Seq(
  cacheApi
)

libraryDependencies ++= Seq(
  caffeine
)
lazy val akkaVersion = "2.10.0-M7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "junit" % "junit" % "4.13.1" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test)