package org.hammerlab.iterator.site

import hammerlab.show._
import org.hammerlab.docs.block
import org.hammerlab.docs.Code.Example
import org.hammerlab.iterator.docs.Elem.{ Section, dsl }
import org.hammerlab.iterator.docs._
import org.scalajs.dom.document

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("hammerlab.iterators.docs")
object Docs
  extends scalatags.Text.Cap
     with fence.utils
     with interp
     with symbol
     with URL.utils
     with attr_dsl
     with Example.make {

  val sections =
    List(
      count,
      either,
      end,
      group,
      level,
      ordering,
      range,
      sample,
      scan,
      sliding,
      start,
      util
    )
    .map(_ !)

  import scalatags.Text.all._

  def badge(url: URL, alttext: String, image: URL) =
    a(
      clz - 'badge,
      href := url,
       alt := alttext,
      img(
        src := image
      )
    )

  import build_info.iterator.{ organization, name, version, githubUser, githubRepo, modName }

  object github {
    val user = githubUser.get
    val repo = githubRepo.get
    val badge =
      img(
        clz - "github-badge",
        src := "./github.svg",
        alt := "Github Logo",
        title := "Github Logo"
      )

    def link(children: Modifier*) =
      a(
        href := s"https://github.com/$user/$repo",
        children,
        badge
      )
  }

  import github._

  object travis {
    val domain = "https://travis-ci.org"
    val base = s"$domain/$user/$repo"

    def apply() =
      badge(
        URL(base),
        "Build Status",
        URL(s"$base.svg?branch=master")
      )
  }

  object coveralls {
    val domain = "https://coveralls.io"
    def apply() =
      badge(
        URL(s"$domain/github/$user/$repo"),
        "Coverage Status",
        URL(s"$domain/repos/github/$user/$repo/badge.svg")
      )
  }

  object mavenCentral {
    def apply() =
      badge(
        URL(s"http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22$organization%22%20AND%20a%3A%22$modName%22"),
        "Maven Central",
        URL(s"https://img.shields.io/maven-central/v/$organization/$modName.svg?maxAge=1800")
      )
  }

  import hammerlab.iterator._
  import hammerlab.cmp.first._
  import cats.implicits.catsKernelStdOrderForInt

  val intro =
    List[Elem](
      p(
        travis(),
        coveralls(),
        mavenCentral()
      ),
      p"Enrichment-methods for ${'Iterator}s, ${'Iterable}s, and ${'Array}s",
      fence(
        block {
          import hammerlab.iterator._
        }
      ),
      fence(
        example(Iterator(1, 2, 3).nextOption         , Some(1)),
        example(Iterator(1, 2, 3).buffered.headOption, Some(1)),
        "",
        example(
          Array(1, 2, 1, 3).countElems,
          Map(1→2, 2→1, 3→1)
        ),
        example(
          List(1, 1, 2, 1, 7, 7, 7).runLengthEncode,
          Iterator(1→2, 2→1, 1→1, 7→3)
        )
      ),
      p"To use, add this to ${"build.sbt"}:",
      pre(
        code(
          Seq(
            "libraryDependencies += \"%s\"  %%%% \"%s\" %% \"%s\"".format(organization, name, version),
            "// For ScalaJS:",
            "libraryDependencies += \"%s\" %%%%%% \"%s\" %% \"%s\"".format(organization, name, version)
          )
          .mkString("\n")
        )
      ),
      p"The wildcard ${"import hammerlab.iterator._"} above brings all functionality into scope.",
      p"Individual packages can also be imported via e.g. ${"import hammerlab.iterator.sliding._"}."
    )

  val html =
    dsl.h(
      'iterators,
      github.link('iterators),
      intro,
      dsl.h(
        'examples,
        "Examples",
        p"Grouped by package:",
        sections
      )
    )

  val menu =
    li(
      clz - "sidebar-brand",
      "Packages:"
    ) ::
    sections
      .map {
        case Section(id, name, _, elems) ⇒
          li(
            clz - "nav-item",
            a(
              href := '#' + id.value,
              clz - "nav-link",
              name
            )
          )
      }

  def main(args: Array[String]): Unit = {
    document
      .getElementById("main")
      .innerHTML =
      div(
        html.compile(skipLevels = 2): _*
      )
      .render

    document
      .getElementById("nav-list")
      .innerHTML =
      menu.render
  }
}
