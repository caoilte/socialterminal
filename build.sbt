organization := "org.caoilte"
name := "social-terminal"
version := "0.1-SNAPSHOT"
scalaVersion := "2.11.6"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "jline" % "jline" % "2.12",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3",
  "joda-time" % "joda-time" % "2.7",
  "org.joda" % "joda-convert" % "1.7",
  "org.scalaz" %% "scalaz-core" % "7.1.1"
)

