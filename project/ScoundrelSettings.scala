import sbt.Keys.{scalaVersion, scalacOptions, _}
import sbt._

object ScoundrelSettings {

  lazy val defaultSettings: Seq[Setting[_]] = Seq(
    version := "3.2.0-SNAPSHOT",
    organization := "tech.scoundrel",
    crossScalaVersions := Seq("2.11.11","2.12.4"),
    scalaVersion := "2.12.4",

    parallelExecution in Test := false
	) ++ Publish.toVeact

  lazy val rogueSettings = Seq(
    scalacOptions ++= Seq("-deprecation", "-unchecked"), //, "-Xlog-implicit-conversions"),
    scalacOptions ++= Seq("-feature", "-language:_"),

    testOptions in Test ++= Seq(Tests.Setup(() => MongoEmbedded.start), Tests.Cleanup(()=> MongoEmbedded.stop))
  ) ++ defaultSettings
}

object ScoundrelDependencies {
  val liftVersion = "3.2.0"
  val specsVer = "3.8.6"

  val liftMongoRecordDeps = Seq(
    "net.liftweb"             %%  "lift-record"           % liftVersion,
    "net.liftweb"             %%  "lift-json-ext"         % liftVersion,
    "net.liftweb"             %%  "lift-util"             % liftVersion
  )

  val liftRogueDeps = Seq(
    "net.liftweb"              %% "lift-common"         % liftVersion % "compile",
    "net.liftweb"              %% "lift-json"           % liftVersion % "compile",
    "net.liftweb"              %% "lift-util"           % liftVersion % "compile"
  )

  val liftRecordDeps = Seq(
    "net.liftweb"              %% "lift-webkit"         % liftVersion % "compile" intransitive()
  )

  val joda = Seq(
    "joda-time"                % "joda-time"            % "2.9.9"     % "compile",
    "org.joda"                 % "joda-convert"         % "1.8.1"     % "compile"
  )
  val mongoDeps = Seq(
    "org.mongodb"              % "mongodb-driver"       % "3.4.3"     % "compile",
    "org.mongodb"              % "mongodb-driver-async" % "3.4.3"     % "compile"
  )

  val testCoreDeps = Seq(
    "org.specs2"              %% "specs2-core"          % specsVer    % "test"
  )

  val testDeps = Seq(
    "junit"                    % "junit"                % "4.5"       % "test",
    "org.specs2"              %% "specs2-core"          % specsVer    % "test",
    "org.specs2"              %% "specs2-matcher"       % specsVer    % "test",
    "org.specs2"              %% "specs2-junit"         % specsVer    % "test",
    "org.scalatest"           %% "scalatest"            % "3.0.3"     % "test",
    "com.novocode"             % "junit-interface"      % "0.11"      % "test",
    "org.slf4j"                % "slf4j-simple"         % "1.7.21"    % "test"
  )

  val shapeless = "com.chuusai" %% "shapeless" % "2.3.2"

  val scalaLangModules = Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.5")

//  val testDeps: Seq[ModuleID] = testCoreDeps ++ testExtraDeps

  val mongodbRecordDeps: Seq[ModuleID] = liftMongoRecordDeps ++ mongoDeps ++ scalaLangModules ++ testCoreDeps

  val coreDeps: Seq[ModuleID] = mongoDeps ++ joda

  val rogueLiftDeps: Seq[ModuleID] = mongoDeps ++ joda ++ liftRogueDeps ++ liftRecordDeps

  val ccDeps: Seq[ModuleID] = mongoDeps ++ Seq(shapeless) ++ testDeps
}
