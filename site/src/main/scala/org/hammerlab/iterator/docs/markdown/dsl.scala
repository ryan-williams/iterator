package org.hammerlab.iterator.docs.markdown

import hammerlab.lines.Lines
import org.hammerlab.iterator.docs.markdown
import org.hammerlab.iterator.docs.markdown.tree.NonLink.Del

trait dsl
  extends interp {

  import org.hammerlab.docs.Code
  import org.hammerlab.docs.Code.Setup

  import markdown.dsl._

  object a extends Inline.A.dsl

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

  def del(value: String): Del = Del(value)

  object section {

    import Inline.A

    val idChars = (
      "-_:.".toSet ++
        ('a' to 'z') ++
        ('A' to 'Z') ++
        ('0' to '9')
      )

    import shapeless.{ Inl, Inr }
    def id(title: Seq[Inline]): Id =
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

    def apply(title: Inline, elems: Elem*): Section = apply(Seq(title), elems: _*)
    def apply(title: Seq[Inline], elems: Elem*): Section =
      Section(
        title,
        id(title),
        elems = elems
      )
  }

  def h(id: Id, title: Seq[Inline], elems: Elem*): Section = Section(title, id, elems)
  def h(id: Id, title: Inline, elems: Elem*): Section = Section(Seq(title), id, elems)
  def h(title: Inline, elems: Elem*): Section = section(Seq(title), elems: _*)
}

object dsl
  extends markdown.tree.elem[Id] {
  type Id = markdown.Id
  override type CanLinkTo = Section
}
