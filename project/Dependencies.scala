import sbt._

object Dependencies {
  private object version {
    val scalaMeta = "4.1.0"
  }

  lazy val javaParser = "com.github.javaparser" % "javaparser-symbol-solver-core" % "3.13.1"
  lazy val scalaMeta = "org.scalameta" %% "scalameta" % version.scalaMeta
  lazy val `scala-java8-compat` = "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
}
