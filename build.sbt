
default(
  scalacOptions += "-Yrangepos",
  hammerlab.test.suite.version := "1.0.1",
  `2.12`.version := "2.12.4",
  versions(
    scalatags → "0.6.7"
  ),
  sonatypeStage(1457)  // org.hammerlab.test:*:1.0.1
)

lazy val core =
  crossProject
    .settings(
      name := "iterator",
      github.repo("iterators"),
      v"2.1.0",
      // Skip compilation during doc-generation; otherwise it fails due to macro-annotations not being expanded
      emptyDocJar,
      scalameta,
      dep(
              cats,
        math.utils % "2.2.0",
             spire,
             types % "1.1.0"
      ),
      buildInfoKeys :=
        Seq[BuildInfoKey](
          organization,
          name,
          version,
          scalaVersion,
          sbtVersion,
          modName,
          githubUser,
          githubRepo,
        ),
      buildInfoPackage := "build_info",
      buildInfoObject := name.value,
      consolePkgs += "hammerlab.iterator"
    )
    .jvmSettings(
      buildInfoKeys ++= Seq[BuildInfoKey](
        baseDirectory,
        target,
        crossTarget
      )
    )
    .dependsOn(
      macros
    )
    .enablePlugins(
      BuildInfoPlugin
    )
lazy val coreJS  = core.js
lazy val coreJVM = core.jvm

lazy val macros =
  crossProject
    .settings(
      subgroup("macros", "iterators"),
      v"1.0.0",
      scalameta,
      enableMacroParadise
    )
lazy val macrosJS  = macros.js
lazy val macrosJVM = macros.jvm

import scalajs.react
lazy val site =
  project
    .settings(
      react.npm,
      scalaJSUseMainModuleInitializer := true,
      dep(
        hammerlab.io % "5.0.1".snapshot,
        react,
        scalatags,
        hammerlab("docs", "snippets") % "1.0.0".snapshot
      ),
      enableMacroParadise
    )
    .enablePlugins(
      JS,
      ScalaJSBundlerPlugin
    )
    .dependsOn(
        coreJS,
      macrosJS
    )

lazy val iterators =
  rootProject(
      coreJS,   coreJVM,
    macrosJS, macrosJVM,
    site
  )
