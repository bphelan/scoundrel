import sbt.Keys._
import sbt._
import xerial.sbt.Sonatype.SonatypeKeys._
import com.typesafe.sbt.pgp.PgpKeys.useGpg

/*
 Sonatype settings
  */
object SonatypeSettings {
  lazy val sonatypeSettings = Seq(
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },

    licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),

    sonatypeProfileName := "tech.scoundrel",

    homepage := Some(url("http://scoundrel.tech")),

    scmInfo := Some(
      ScmInfo(
        url("https://github.com/scoundrel-tech/scoundrel"),
        "scm:git@github.com:scoundrel-tech/scoundrel.git"
      )
    ),

    developers := List(
      Developer(
        id = "agoldenduck",
        name  = "Alex Dacre",
        email = "a.dacre@thinkbits.com",
        url = url("https://github.com/agoldenduck")
      )
    ),

    useGpg := true,
    publishTo := sonatypePublishTo.value
  )
}
