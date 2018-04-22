package org.hammerlab.iterator.site

import hammerlab.show._
import org.hammerlab.iterator.docs.Elem.Section
import org.hammerlab.iterator.docs._
import org.scalajs.dom.document

import scala.scalajs.js.annotation.JSExportTopLevel
import scalatags.Text.all.{div, li, a}

@JSExportTopLevel("hammerlab.iterators.docs")
object Docs
  extends scalatags.Text.Cap
     with symbol
     with utils {

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

  val bodies =
    sections
      .flatMap(
        _
          .!
          .compile(2)
      )

  val html =
    div(
      (
        (clz - 'container) ::
        bodies
      ): _*
    )

  val menu =
    sections
      .map {
        _.! match {
          case Section(id, name, elems) ⇒
            li(
              clz - "nav-item",
              a(
                href := '#' + id.value,
                clz - "nav-link",
                name
              )
            )
        }
      }

  def main(args: Array[String]): Unit = {
    document
      .getElementById("main")
      .innerHTML =
      html.render

    document
      .getElementById("nav-list")
      .innerHTML =
      menu.render
  }
}
