package org.hammerlab.iterator.docs.markdown.shapel

import org.hammerlab.iterator.docs.markdown.{ Clz, URL }

sealed trait NonLink {
  def value: String
}
object NonLink {
  case class Text(value: String) extends NonLink
  case class    B(value: String) extends NonLink
  case class    I(value: String) extends NonLink
  case class  Del(value: String) extends NonLink
  case class Code(value: String) extends NonLink

  object Text {
    implicit def wrap(v: String): Text = Text(v)
  }

  // <img>
  case class Img(
    src: URL,
    alt: String,
    clz: Clz = Nil
  )
  extends NonLink {
    def value = alt
  }
}
