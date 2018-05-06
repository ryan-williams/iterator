package org.hammerlab.docs.markdown.tree

import hammerlab.lines.Lines
import org.hammerlab.docs.Opt
import org.hammerlab.docs.Opt.Non
import org.hammerlab.docs.markdown.util.{ Clz, URL }

trait elem[_Id] {

  private type Id = _Id

  type Inline = Either[NonLink, Inline.A]
  val L = Left
  def R[T](t: T) = Right(t)
  object R {
    def unapply[L, R](t: Either[L, R]): Option[R] =
      t match {
        case Right(v) ⇒ Some(v)
        case _ ⇒ None
      }
  }

  implicit def inlineNonLink(n: NonLink): Inline = L(n)
  implicit def inlineLink(a: Inline.A): Inline = R(a)

  implicit def inlineNonLinks(n: Seq[NonLink]): Seq[Inline] = n.map(L(_))
  implicit def inlineLinks(a: Seq[Inline.A]): Seq[Inline] = a.map(a ⇒ a: Inline)

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
  object Section {
    def apply(
      title: Inline,
      id: Id,
      elems: Seq[Elem]
    ): Section =
      Section(
        Seq(title),
        id,
        elems
      )
  }

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

}
