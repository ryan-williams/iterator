package org.hammerlab.iterator.docs.markdown

import hammerlab.lines.Lines
import org.hammerlab.iterator.docs.Opt
import org.hammerlab.iterator.docs.Opt.Non
import org.hammerlab.iterator.docs.markdown.Inline.{ A, NonLink }
import org.hammerlab.iterator.docs.markdown.{ HasId, Target, URL }

// "Top-level" elements within a section: <p>, <ol>, <ul>
sealed trait Elem
object Elem {

  case class Section(
    title: Seq[Inline],
    id: Id,
    elems: Seq[Elem] = Nil
  )
  extends Elem
     with HasId

  object Section {

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
            case i: NonLink ⇒
              i
                .value
                .replaceAll(" ", "-")
                .filter { idChars(_) }
            case A(children, _, _, _) ⇒
              id(children)
          }
          .mkString("-")
      )

    def apply(title: Inline, elems: Seq[Elem]): Section =
      new Section(
        Seq(title),
        id(Seq(title)),
        elems = elems
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

  /**
   * &lt;img&gt; as a stand-alone [[Elem]] in a [[Section]]
   *
   * It is the only [[Elem]]-type that is allowed to be wrapped in a link, so it folds that into its state via optional
   * [[Img.href]]
   */
  case class Img(
    src: URL,
    alt: String,
    href: Opt[Target] = Non,
    clz: Clz = Nil
  )
  extends NonSection
}
