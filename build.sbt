import sbt.Keys.parallelExecution

val scala = "2.12.3"

import RogueSettings._

name := "scoundrel"

version := "0.1-SNAPSHOT"

lazy val mongodbRecord = (project in file("mongodb-record")).settings(
  organization := "tech.scoundrel",
  name := "mongodb-record",
  scalaVersion := scala,
  libraryDependencies ++= Seq(
    "org.mongodb"             %   "mongodb-driver"        % "3.4.1",
    "org.mongodb"             %   "mongodb-driver-async"  % "3.4.1",
    "net.liftweb"             %%  "lift-record"           % "3.1.0",
    "net.liftweb"             %%  "lift-json-ext"         % "3.1.0",
    "net.liftweb"             %%  "lift-util"             % "3.1.0",
    "org.scala-lang.modules"  %%  "scala-xml"             % "1.0.5",
    "org.specs2"              %%  "specs2-core"           % "3.8.6" % "test"
  ),
  parallelExecution in Test := false
)

Seq(RogueSettings.defaultSettings: _*)

lazy val field = (project in file("rogue/field")).settings(defaultSettings).dependsOn(mongodbRecord)

lazy val index = (project in file("rogue/index")).settings(defaultSettings).dependsOn(field)

lazy val core = (project in file("rogue/core")).settings(defaultSettings).dependsOn(field, index % "compile;test->test;runtime->runtime")

lazy val indexchecker = (project in file("rogue/indexchecker")).settings(defaultSettings).dependsOn(core)

lazy val lift = (project in file("rogue/lift")).settings(defaultSettings).dependsOn(field, indexchecker, core % "compile;test->test;runtime->runtime")
//lazy val spindle = (project in file("spindle")).settings(defaultSettings).dependsOn(core)

lazy val cc = (project in file("rogue/cc")).dependsOn(field, core).settings(defaultSettings)

lazy val root = (project in file(".")).settings(defaultSettings).aggregate(mongodbRecord, field, core, index, indexchecker, lift, cc)


