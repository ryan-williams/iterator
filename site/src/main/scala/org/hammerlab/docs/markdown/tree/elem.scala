package org.hammerlab.docs.markdown.tree

import hammerlab.lines.Lines
import org.hammerlab.docs.Opt
import org.hammerlab.docs.Opt.Non
import org.hammerlab.docs.markdown.util.{ Clz, URL }

trait elem[_Id] {

  private type Id = _Id

  implicit class Inlines(val elems: Seq[Inline])
  object Inlines {
    implicit def single(elem: Inline): Inlines = Inlines(Seq(elem))
    implicit def unwrap(inline: Inlines): Seq[Inline] = inline.elems

    implicit def inlineNonLink(n: NonLink): Inlines = L(n)
    implicit def inlineLink(a: Inline.A): Inlines = R(a)

    implicit def inlineNonLinks(n: Seq[NonLink]): Inlines = n.map(L(_))
    implicit def inlineLinks(a: Seq[Inline.A]): Inlines = a.map(R(_))
  }
  implicit def flattenInlines(inlines: Seq[Inlines]): Seq[Inline] = inlines.flatMap(_.elems)

  type Inline = Either[NonLink, Inline.A]
  implicit def inlineNonLink(n: NonLink): Inline = L(n)
  implicit def inlineLink(a: Inline.A): Inline = R(a)

  val L = Left
  def R[T](t: T) = Right(t)
  object R {
    def unapply[L, R](t: Either[L, R]): Option[R] =
      t match {
        case Right(v) ⇒ Some(v)
        case _ ⇒ None
      }
  }

  object Inline {

    implicit def wrap(v: String): Inline = L(NonLink.Text(v))

    case class A(
      children: Seq[NonLink],
      target: Target,
      alt: Opt[String],
      clz: Clz
    )

    object A {
      trait dsl {
        def apply(
          content: Seq[NonLink],
          target: Target,
          alt: Opt[String],
          clz: Clz
        ) =
          new A(
            content,
            target,
            alt,
            clz
          )
        def apply(
          content: NonLink,
          target: Target,
          alt: Opt[String] = Non,
          clz: Clz = Nil
        ) =
          new A(
            Seq(content),
            target,
            alt,
            clz
          )
      }
    }
  }

  type CanLinkTo

  type Target = Either[URL, CanLinkTo]
  implicit def urlTarget(url: URL): Target = L(url)
  implicit def sectionTarget(section: CanLinkTo): Target = R(section)

  // "Top-level" elements within a section: <p>, <ol>, <ul>
  sealed trait Elem

  case class Section(
    title: Seq[Inline],
    id: Id,
    elems: Seq[Elem] = Nil
  )
  extends Elem

  sealed trait NonSection extends Elem

  // <p>
  case class P(elems: Seq[Inline]) extends NonSection

  // <pre><code>…</code></pre>: "fenced" code block
  case class Fence(lines: Lines) extends NonSection

  // Lists: <ul>, <ol>
  case class UL(items: Seq[LI]) extends NonSection
  case class OL(items: Seq[LI]) extends NonSection

  // <li>
  case class LI(title: Inline, elems: Seq[NonSection])

  implicit class Elems(val elems: Seq[Elem])
  object Elems {
    implicit def single(elem: Elem): Elems = Elems(Seq(elem))
  }
  implicit def flattenElems(elems: Seq[Elems]): Seq[Elem] = elems.flatMap(_.elems)
}
