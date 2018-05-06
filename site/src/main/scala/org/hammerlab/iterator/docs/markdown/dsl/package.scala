package org.hammerlab.iterator.docs.markdown

import hammerlab.lines.Lines
import org.hammerlab.iterator.docs.markdown

package object dsl
  extends markdown.tree.elem[dsl.Id] {

  import org.hammerlab.docs.Code
  import org.hammerlab.docs.Code.Setup

  override type CanLinkTo = Section

  def section(title: Inline, elems: Elem*): Section = MkSection(title, elems)
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

  object MkSection {

    import Inline.A

    val idChars = (
      "-_:.".toSet ++
        ('a' to 'z') ++
        ('A' to 'Z') ++
        ('0' to '9')
      )

    import shapeless.{Inl, Inr}
    def id(title: Seq[Inline]): dsl.Id =
      Id(
        title
          .map {
            case Inl(i) ⇒
              i
                .value
                .replaceAll(" ", "-")
                .filter {
                  idChars(_)
                }
            case Inr(Inl(A(children, _, _, _))) ⇒
              id(
                children.map(
                  Inl(_)
                )
              )
          }
          .mkString("-")
      )

    def apply(title: Inline, elems: Seq[Elem]): Section =
      Section(
        Seq(title),
        id(Seq(title)),
        elems = elems
      )
  }
}
