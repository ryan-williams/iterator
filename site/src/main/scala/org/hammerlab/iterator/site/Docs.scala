package org.hammerlab.iterator.site

import build_info.iterator._
import cats.instances.int.catsKernelStdOrderForInt
import hammerlab.cmp.first._
import hammerlab.iterator._
import hammerlab.show._
import japgolly.scalajs.react.vdom.html_<^.<.div
import org.hammerlab.docs.Code.Example
import org.hammerlab.docs._
import org.hammerlab.docs.markdown.dsl._
import org.hammerlab.docs.markdown.tree.NonLink.Text
import org.hammerlab.docs.markdown.{ dsl, fqn, render }
import org.hammerlab.iterator.site.Menu.Item
import org.hammerlab.lines.Lines
import org.scalajs.dom.document.getElementById

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("hammerlab.iterators.docs")
object Docs
  extends Example.make
     with dsl
     with badge {

  implicit val _github = GitHub(githubUser.get, githubRepo.get)
  implicit val mavenCoords = MavenCoords(organization, name, modName)

  val intro =
    Seq(
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
      Fence(
        Lines(
          "libraryDependencies += \"%s\"  %%%% \"%s\" %% \"%s\"".format(organization, name, version),
          "",
          "// For ScalaJS:",
          "libraryDependencies += \"%s\" %%%%%% \"%s\" %% \"%s\"".format(organization, name, version)
        )
      ),
      p"The wildcard ${"import hammerlab.iterator._"} above brings all functionality into scope.",
      p"Individual packages can also be imported via e.g. ${"import hammerlab.iterator.sliding._"}."
    )

  val html =
    section(
      'iterators,
      github.title,
      intro,
      section(
        t"Examples",
        p"Grouped by package:",
        sections !
      )
    )

  val rendered = fqn.tree.MkSection(html)

  val react = div(render.react(rendered)('root))

  val menu =
    Menu(
      Item(
        rendered.id,
        Text("Intro")
      ),
      rendered
        .elems
        .collect {
          case s: fqn.tree.Section ⇒
            Item(s, 2)
        }
    )

  // TODO: fragment-links on headers' hover
  // TODO: scalafiddle integration
  // TODO: export dsl
  // TODO: embed scalajs-react components in DSL
  // TODO: run examples server-side only
  // TODO: toggle open/close state of nav elements
  // TODO: expose nav-bar state (e.g. max depth)
  // TODO: auto-collapse nav into hamburger menu

  def main(args: Array[String]): Unit = {
    menu.renderIntoDOM(
      getElementById("nav-container")
    )
    react.renderIntoDOM(
      getElementById("main")
    )
  }
}
