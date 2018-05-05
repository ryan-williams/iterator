package org.hammerlab.iterator.site

import hammerlab.show._
import org.hammerlab.docs.Code.Example
import org.hammerlab.docs.block
import org.hammerlab.iterator.docs._
import org.hammerlab.iterator.site.Menu.Item
import org.scalajs.dom.document.getElementById

import scala.collection.mutable
import scala.scalajs.js.annotation.JSExportTopLevel

import japgolly.scalajs.react.vdom.html_<^._, <._

trait sections {
  self: base ⇒
  import Elem._
  val sections = mutable.ArrayBuffer[Section]()
}

@JSExportTopLevel("hammerlab.iterators.docs")
object Docs
  extends sections
     with fence.utils
     with interp
     with symbol
     with attr_dsl
     with Example.make
     with count
//     with either
//     with end
//     with group
//     with level
//     with ordering
//     with range
//     with sample
//     with scan
//     with sliding
//     with start
//     with util
     with badge {

  import Elem._

  import build_info.iterator.{ githubRepo, githubUser, modName, name, organization, version }
  import hammerlab.cmp.first._
  import hammerlab.iterator._

  implicit val _github = GitHub(githubUser.get, githubRepo.get)
  implicit val mavenCoords = MavenCoords(organization, name, modName)

  import <.{p, pre, code, span}

  val intro =
    List[Tag](
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
/*
    .zipWithIndex
    .map {
      case (Tag(e), i) ⇒ Tag(e(^.key := i))
    }
*/

  val html =
    h(
      id = 'iterators,
      title = github.link('iterators),
      //intro,
      h(
        'examples,
        title = span("Examples"),
        p"Grouped by package:",
        sections
      )
    )

  val rendered = Tree.Section(html).reduceIds

  val menu =
    Menu(
      Item(
        "iterators",
        span("Intro")
      ) ::
      rendered
        .children
        .collect {
          case s: Tree.Section ⇒
            Item(s)
        }
        .toList
    )

  def main(args: Array[String]): Unit = {
    rendered.compile.renderIntoDOM(
      getElementById("main")
    )

    menu.renderIntoDOM(
      getElementById("nav-container")
    )
  }
}
