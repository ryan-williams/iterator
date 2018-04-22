import sbt.Keys._
import sbt._
import sbt.PluginTrigger.AllRequirements
import sbt.librarymanagement.CrossVersion

object Plugin
  extends AutoPlugin {
  override def trigger = AllRequirements

  object autoImport {
    val modName = settingKey[String]("Artifact-name as ivy/maven see it, i.e. with scala-binary-version appended")
    val scalaVersionObject = settingKey[ScalaVersion]("ScalaVersion object, created from binary and full scala versions")
  }

  import autoImport._
  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      scalaVersionObject :=
        ScalaVersion(
          (scalaVersion in artifactName).value,
          (scalaBinaryVersion in artifactName).value
        ),
      modName := {
        val ScalaVersion(full, binary) = scalaVersionObject.value
        val cross = CrossVersion(projectID.value.crossVersion, full, binary)
        val name = artifact.value.name
        cross
          .fold {
            name
          } {
            _(name)
          }
      }
    )
}
