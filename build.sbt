
default(
  scalacOptions += "-Yrangepos",
  `2.12`.version := "2.12.4",
  versions(
    scalatags → "0.6.7"
  ),
  testSuiteVersion := "1.0.1".snapshot,
  testUtilsVersion := "1.0.1".snapshot,
  sonatypeStage(
    1469,  // org.hammerlab.test:*:1.0.1
    1470,  // shapeless-utils 1.3.0
    1471   // io
  )
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
lazy val `core.js`  = core.js
lazy val `core.jvm` = core.jvm
lazy val `core-x`   = parent(`core.js`, `core.jvm`)

lazy val macros =
  crossProject
    .settings(
      subgroup("macros", "iterators"),
      v"1.0.0",
      scalameta,
      enableMacroParadise
    )
lazy val `macros.js`  = macros.js
lazy val `macros.jvm` = macros.jvm
lazy val `macros-x`   = parent(`macros.js`, `macros.jvm`)

import scalajs.react
lazy val site =
  project
    .settings(
      `2.12` only,
      react.npm,
      scalaJSUseMainModuleInitializer := true,
      dep(
        hammerlab.io % "5.1.0",
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
        `core.js`,
      `macros.js`
    )

lazy val iterators =
  root(
      `core-x`,
    `macros-x`,
         site
  )
