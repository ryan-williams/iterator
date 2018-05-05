package org.hammerlab.iterator.docs.markdown

import hammerlab.lines.Lines

object dsl {
  import org.hammerlab.docs.Code
  import org.hammerlab.docs.Code.Setup
  import Elem._

  def section(title: Inline, elems: Elem*): Section = Section(title, elems)
  def fence(body: Code*): Fence =
    Fence(
      Lines(
        (
          body
            .map {
              case s: Setup ⇒ Lines(s.lines, "")
              case c ⇒ Code.lines(c)
            }
        ): _*
      )
    )
  def p(elems: Inline*): P = P(elems)
}
