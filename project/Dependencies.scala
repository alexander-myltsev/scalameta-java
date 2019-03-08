import sbt._

object Dependencies {
  private object version {
    val scalaMeta = "4.1.0"
    val scalaCheck = "1.14.0"
    val scalaTest = "3.0.5"
    val `java8-compat` = "0.9.0"
    val javaParser = "3.13.1"
  }

  lazy val javaParser = "com.github.javaparser" % "javaparser-symbol-solver-core" % version.javaParser
  lazy val scalaMeta = "org.scalameta" %% "scalameta" % version.scalaMeta
  lazy val `scala-java8-compat` = "org.scala-lang.modules" %% "scala-java8-compat" % version.`java8-compat`

  lazy val scalaTest = "org.scalatest" %% "scalatest" % version.scalaTest
  lazy val scalaMetaCore = "org.scalameta" % "semanticdb-scalac-core" % version.scalaMeta cross CrossVersion.full
  lazy val scalaMetaTestKit = "org.scalameta" %% "testkit" % version.scalaMeta
  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % version.scalaCheck
}
