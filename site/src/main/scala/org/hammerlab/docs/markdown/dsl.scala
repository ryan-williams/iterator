package org.hammerlab.docs.markdown

import hammerlab.lines.Lines
import org.hammerlab.docs.markdown
import org.hammerlab.docs.markdown.tree.NonLink.Del

trait dsl
  extends interp {

  import org.hammerlab.docs.Code
  import org.hammerlab.docs.Code.Setup

  import markdown.dsl._

  object a extends Inline.A.dsl

  def fence(body: Code*): Fence =
    Fence(
      body
        .map {
          case s: Setup ⇒ Lines(s.lines, "")
          case c ⇒ Code.lines(c)
        }
    )
  def p(elems: Inlines*): P = P(elems)

  def del(value: String): Del = Del(value)

  trait section {

    import Inline.A

    val idChars = (
      "-_:.".toSet ++
      ('a' to 'z') ++
      ('A' to 'Z') ++
      ('0' to '9')
    )

    def id(title: Seq[Inline]): Id =
      Id(
        title
          .map {
            case L(i) ⇒
              i
                .value
                .replaceAll(" ", "-")
                .filter {
                  idChars(_)
                }
            case R(A(children, _, _, _)) ⇒
              id(
                children.map(
                  L(_)
                )
              )
          }
          .mkString("-")
      )

    def apply(title: Inlines, elems: Elems*): Section =
      Section(
        title,
        id(title),
        elems = elems
      )

    def apply(id: Id, title: Inlines, elems: Elems*): Section = Section(title, id, elems)
  }
  object section extends section
  object       h extends section
}

object dsl
  extends markdown.tree.elem[Id] {
  type Id = markdown.Id
   val Id = markdown.Id
  override type CanLinkTo = Section
}
