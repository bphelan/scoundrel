import ScoundrelSettings._

name := "scoundrel"

Seq(ScoundrelSettings.defaultSettings: _*)

lazy val mongodbRecord = (project in file("mongodb-record")).settings(defaultSettings)

lazy val field = (project in file("rogue/field")).settings(rogueSettings).dependsOn(mongodbRecord)

lazy val index = (project in file("rogue/index")).settings(rogueSettings).dependsOn(field)

lazy val core = (project in file("rogue/core")).settings(rogueSettings).dependsOn(field, index % "compile;test->test;runtime->runtime")

lazy val indexchecker = (project in file("rogue/indexchecker")).settings(rogueSettings).dependsOn(core)

lazy val lift = (project in file("rogue/lift")).settings(rogueSettings).dependsOn(field, indexchecker, core % "compile;test->test;runtime->runtime")

lazy val cc = (project in file("rogue/cc")).dependsOn(field, core).settings(rogueSettings)

lazy val root = (project in file(".")).settings(defaultSettings).settings(
  publishArtifact := false,
  publish := {},
  publishLocal := {}
).aggregate(mongodbRecord, field, core, index, indexchecker, lift, cc)
