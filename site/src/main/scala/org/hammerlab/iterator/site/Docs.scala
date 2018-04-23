package org.hammerlab.iterator.site

import hammerlab.show._
import org.hammerlab.docs.Code.Example
import org.hammerlab.docs.block
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
     with Example.make
     with Elem.dsl {

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

  import build_info.iterator.{ githubRepo, githubUser, modName, name, organization, version }
  import cats.implicits.catsKernelStdOrderForInt
  import hammerlab.cmp.first._
  import hammerlab.iterator._

  import scalatags.Text.all._

  implicit val github = GitHub(githubUser.get, githubRepo.get)
  implicit val mavenCoords = MavenCoords(organization, name, modName)
  import badge.{ coveralls, mavenCentral, travis }

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
            "",
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
    h(
      'iterators,
      badge.github.link('iterators),
      intro,
      h(
        'examples,
        "Examples",
        p"Grouped by package:",
        sections
      )
    )

  val rendered = Tree.Section(html)

  val menu =
    li(
      clz - "sidebar-brand",
      "Packages:"
    ) ::
    rendered
      .children
      .toList
      .collect {
        case Tree.Section(id, title, elems) ⇒
          li(
            clz - "nav-item",
            a(
              href := id.href,
              clz - "nav-link",
              title
            )
          )
      }

  def main(args: Array[String]): Unit = {
    document
      .getElementById("main")
      .innerHTML =
      div(
        rendered.compile
      )
      .render

    document
      .getElementById("nav-list")
      .innerHTML =
      menu.render
  }
}
