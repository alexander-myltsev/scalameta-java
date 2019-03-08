import Dependencies._

ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.myltsev"

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "scalameta-java",
    libraryDependencies ++= Seq(
      javaParser,
      scalaMeta,
      `scala-java8-compat`,
      scalaTest % Test,
      scalaMetaCore % Test,
      scalaMetaTestKit % Test,
      scalaCheck % Test
    ),

    buildInfoPackage := "com.myltsev",
    buildInfoKeys := Seq[BuildInfoKey](
      "testSourceDirectory" -> new File("test-java-sources")
    )
  )
