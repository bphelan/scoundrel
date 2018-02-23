import sbt.Keys._
import sbt._
import xerial.sbt.Sonatype.SonatypeKeys._
import com.typesafe.sbt.pgp.PgpKeys.useGpg

object Publish {
  lazy val toSonatype: Seq[Def.Setting[_]] = Seq(
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

  lazy val toVeact: Seq[Def.Setting[_]] = Seq(
    publishMavenStyle := false,
    publishArtifact in Test := false,
    publishTo := {
      val (repo, path) = repoInfo(isSnapshot.value)

      Some {
        sbt
          .Resolver
          .ssh(repo, "tools.veact.net", path)(sbt.Resolver.ivyStylePatterns)
          .as(MavenCredentials.userName, MavenCredentials.keyFilePath) withPermissions ("0644")
      }
    }
  )

  private def repoInfo(isSnapshot: Boolean) =
    if (isSnapshot)
      "VEACT snapshots" -> "/srv/maven/snapshots"
    else
      "VEACT releases" -> "/srv/maven/releases"

  private lazy val AdditionalResolvers = new {
    import MavenCredentials._

    val sonatypeReleases = sbt.Resolver.sonatypeRepo("releases")
    val sonatypesSnapshots = sbt.Resolver.sonatypeRepo("snapshots")
    val veactReleases = sbt.Resolver.sftp("releases", "tools.veact.net",
      "/srv/maven/releases")(sbt.Resolver.ivyStylePatterns) as (userName, keyFilePath)
    val veactSnapshots = sbt.Resolver.sftp("snapshots", "tools.veact.net",
      "/srv/maven/snapshots")(sbt.Resolver.ivyStylePatterns) as (userName, keyFilePath)
    val softpropsAtBintray = "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"
    val bintray = sbt.Resolver.url(
      "bintray-sbt-plugin-releases",
      url("http://dl.bintray.com/content/sbt/sbt-plugin-releases")
    )(Resolver.ivyStylePatterns)

    val all = Seq(sonatypeReleases, sonatypesSnapshots, veactReleases, veactSnapshots, bintray, softpropsAtBintray)
  }

  private lazy val MavenCredentials = new {
    val userName = "maven"
    val keyFilePath = Path.userHome / ".ssh" / "id_rsa_maven"
  }
}
