package org.hammerlab.iterator.site

import hammerlab.show._
import org.hammerlab.iterator.docs._
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel
import scalatags.Text.all.{ `class`, div }

@JSExportTopLevel("hammerlab.iterators.docs")
object Docs
  extends symbol {

  object cls {
    def -(name: String) = `class` := name
  }

  val html =
    div(
      cls-'container,
       count !,
      either !,
         end !,
       group !
    )

  def main(args: Array[String]): Unit = {
    dom.document.getElementById("main").innerHTML = html.render
  }
}
