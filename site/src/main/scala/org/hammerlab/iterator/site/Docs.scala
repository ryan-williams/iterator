package org.hammerlab.iterator.site

import hammerlab.show._
import org.hammerlab.docs.Code.Example
import org.hammerlab.docs.block
import org.hammerlab.iterator.docs._
import org.scalajs.dom
import org.scalajs.dom.document

import scala.collection.mutable
import scala.scalajs.js.annotation.JSExportTopLevel

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
     with URL.utils
     with attr_dsl
     with Example.make
     with elem
     with tree
     with module
     with count
     with either
     with end
     with group
     with level
     with ordering
     with range
     with sample
     with scan
     with sliding
     with start
     with util
     with badge {

  type B = dom.Element
  type O = dom.Element
  type F = dom.Node
  implicit val b = scalatags.JsDom

  import b.all._
  import Elem._

  import build_info.iterator.{ githubRepo, githubUser, modName, name, organization, version }
  import hammerlab.cmp.first._
  import hammerlab.iterator._

  implicit val _github = GitHub(githubUser.get, githubRepo.get)
  implicit val mavenCoords = MavenCoords(organization, name, modName)

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
      id = 'iterators,
      title = github.link('iterators),
      intro,
      h(
        'examples,
        title = "Examples",
        p"Grouped by package:",
        sections
      )
    )

  val rendered = Tree.Section(html).reduceIds

  val menu =
    a(
      href := "#iterators",
      clz - "nav-link",
      "Intro"
    ) ::
    rendered
      .children
      .toList
      .flatMap {
        case Tree.Section(id, title, elems) ⇒
          span(
            href := id.href,
            onclick := {
              () ⇒ {
              }
            },
            clz - "nav-link",
            title
          ) ::
          elems
            .toList
            .collect {
              case Tree.Section(id, title, elems) ⇒
                a(
                  href := id.href,
                  clz - "nav-link nav-link-2",
                  title
                )
            }
        case _ ⇒ Nil
      }

  def main(args: Array[String]): Unit = {
    document
      .getElementById("main")
      .appendChild(
        div(
          rendered.compile
        )
        .render
      )

    document
      .getElementById("nav-container")
      .appendChild(
        menu.render
      )
  }
}
