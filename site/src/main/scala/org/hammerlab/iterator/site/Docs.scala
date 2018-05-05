package org.hammerlab.iterator.site

import hammerlab.show._
import org.hammerlab.docs.Code.{ Example, Setup }
import org.hammerlab.docs.block
import org.hammerlab.iterator.docs._
import org.hammerlab.iterator.site.Menu.Item
import org.scalajs.dom.document.getElementById
import markdown.dsl._
import org.hammerlab.iterator.docs.markdown.Elem.Fence
import org.hammerlab.lines.Lines
import org.hammerlab.test.Cmp

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("hammerlab.iterators.docs")
object Docs
  extends interp
     with symbol
     with Example.make
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

  import build_info.iterator.{ githubRepo, githubUser, modName, name, organization, version }
  import hammerlab.cmp.first._
  import hammerlab.iterator._

  import cats.instances.   int.catsKernelStdOrderForInt

  implicitly[Cmp[Int]]
  implicitly[Cmp[Option[Int]]]

  implicit val _github = GitHub(githubUser.get, githubRepo.get)
  implicit val mavenCoords = MavenCoords(organization, name, modName)

  val intro =
    section(
      'iterators,
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
/*
    .zipWithIndex
    .map {
      case (Tag(e), i) ⇒ Tag(e(^.key := i))
    }
*/

  val html =
    section(
      github.badge,
      intro,
      section(
        "Examples",
        p"Grouped by package:"
      )
    )

  //val rendered = Tree.Section(html).reduceIds

//  val menu =
//    Menu(
//      Item(
//        "iterators",
//        span("Intro")
//      ) ::
//      rendered
//        .children
//        .collect {
//          case s: Tree.Section ⇒
//            Item(s)
//        }
//        .toList
//    )

  def main(args: Array[String]): Unit = {
//    rendered.compile.renderIntoDOM(
//      getElementById("main")
//    )

//    menu.renderIntoDOM(
//      getElementById("nav-container")
//    )
  }
}
