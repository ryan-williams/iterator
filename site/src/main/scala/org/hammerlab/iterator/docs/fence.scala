package org.hammerlab.iterator.docs

import hammerlab.indent.implicits.spaces2
import hammerlab.lines._
import org.hammerlab.docs.Code
import org.hammerlab.docs.Code.Setup

object fence {
  trait utils
    extends symbol {
    type T = Elem.Tag
    import scalatags.Text.tags.{ code, pre }
    def fence(body: Code*): T =
      pre(
        code(
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
  }
}
