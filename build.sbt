import sbt.Keys.parallelExecution

val scala = "2.11.8"

lazy val mongodbRecord = (project in file("mongodb-record")).settings(
  name := "mongoodb-record",
  scalaVersion := scala,
  libraryDependencies ++= Seq(
    "org.mongodb"             %   "mongodb-driver"        % "3.4.1",
    "org.mongodb"             %   "mongodb-driver-async"  % "3.4.1",
    "net.liftweb"             %   "lift-record_2.11"      % "3.1.0",
    "net.liftweb"             %   "lift-json-ext_2.11"    % "3.1.0",
    "net.liftweb"             %   "lift-util_2.11"        % "3.1.0",
    "org.scala-lang.modules"  %%  "scala-xml"             % "1.0.5",
    "org.specs2"              %%  "specs2-core"           % "3.8.6" % "test"
  ),
  parallelExecution in Test := false
)
