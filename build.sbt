
default(
  scalacOptions += "-Yrangepos",
  testSuiteVersion := "1.0.0".snapshot,
  testUtilsVersion := "1.0.1".snapshot,
  versions(
    scalacss → "0.5.3",
    scalatags → "0.6.8".snapshot
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

lazy val react =
  project
    .settings(
      scala212Only,
      dep(
        scalajs.react_dep % "1.2.0"
      ),
      npmDependencies in Compile ++= Seq(
        "react"     → "16.2.0",
        "react-dom" → "16.2.0"
      )
    )
    .enablePlugins(
      ScalaJSPlugin,
      ScalaJSBundlerPlugin
    )

lazy val site =
  project
    .settings(
      scala212Only,
      dep(
        scalajs.react_dep % "1.2.0"
      ),
      npmDependencies in Compile ++= Seq(
        "react"     → "16.2.0",
        "react-dom" → "16.2.0"
      ),
      scalaJSUseMainModuleInitializer := true,
      dep(
        hammerlab.io % "5.0.0".snapshot,
        scalatags,
        hammerlab("docs", "snippets") % "1.0.0".snapshot
      ),
      enableMacroParadise
    )
    .enablePlugins(
      JS
    )
    .dependsOn(
        coreJS,
      macrosJS
    )

lazy val iterators =
  rootProject(
      coreJS,   coreJVM,
    macrosJS, macrosJVM,
    react, site
  )
