package org.hammerlab.iterator.docs

object markdown {

  sealed trait Inline

  case class Text(value: String          ) extends Inline
  case class    B(value: String          ) extends Inline
  case class    I(value: String          ) extends Inline
  case class  Del(value: String          ) extends Inline
  case class Code(value: String          ) extends Inline
  case class    A(value: Inline, url: URL) extends Inline

  // "Top-level" elements within a section: <p>, <img>, <ol>, <ul>
  sealed trait Elem

  // <p>
  case class P(elems: Seq[Inline]) extends Elem

  // <img>; folds in optional wrapping with an anchor tag (<a>)
  case class Img(
     src: URL,
     alt: Option[String] = None,
    href: Option[   URL] = None
  )
  extends Elem

  // <pre><code>…</code></pre>: "fenced" code block
  case class Fence(value: String) extends Elem

  // <li>
  case class LI(title: Inline, elems: Seq[Elem])

  // Lists: <ul>, <ol>
  case class UL(items: Seq[LI]) extends Elem
  case class OL(items: Seq[LI]) extends Elem


  case class Section(title: Inline, elems: Seq[Elem])
}
