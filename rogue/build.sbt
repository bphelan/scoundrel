/*
lazy val core = Project("rogue-core", file("rogue-core/")) dependsOn(index % "compile;test->test;runtime->runtime")
  lazy val lift = Project("rogue-lift", file("rogue-lift/")) dependsOn(core % "compile;test->test;runtime->runtime")

 */