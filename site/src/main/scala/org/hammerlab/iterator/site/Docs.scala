package org.hammerlab.iterator.site

import hammerlab.show._
import org.hammerlab.iterator.docs._
import org.scalajs.dom.document

import scala.scalajs.js.annotation.JSExportTopLevel
import scalatags.Text.all.{ `class`, div }

@JSExportTopLevel("hammerlab.iterators.docs")
object Docs
  extends scalatags.Text.Cap
     with symbol
     with utils {

  val html =
    div(
      clz - 'container,
         count !,
        either !,
           end !,
         group !,
         level !,
      ordering !,
         range !,
        sample !,
          scan !,
       sliding !,
         start !,
          util !
    )

  def main(args: Array[String]): Unit = {
    document
      .getElementById("main")
      .innerHTML =
      html.render
  }
}
