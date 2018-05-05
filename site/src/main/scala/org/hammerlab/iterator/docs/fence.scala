package org.hammerlab.iterator.docs

import hammerlab.indent.implicits.spaces2
import hammerlab.lines._
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.vdom.html_<^.<._
import org.hammerlab.docs.Code
import org.hammerlab.docs.Code.Setup

object fence {
  trait utils
    extends symbol {
    def fence(body: Code*): Elem.Tag =
      pre(
        code(
          VdomNode(
          Lines(
            (
              body
                .map {
                  case s: Setup ⇒ Lines(s.lines, "")
                  case c ⇒ Code.lines.apply(c)
                }
            ): _*
          )
          .showLines
          )
        )
      )
      .render
  }
}
