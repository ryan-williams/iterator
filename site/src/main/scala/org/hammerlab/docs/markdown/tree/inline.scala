package org.hammerlab.docs.markdown.tree

import org.hammerlab.docs.Opt
import org.hammerlab.docs.Opt.Non
import org.hammerlab.docs.markdown.util.Clz

trait inline[Id] {
  sealed trait Inline
  object Inline {

    implicit def wrap(v: String): Inline = NonLink.Text(v)

    sealed trait NonLink
      extends Inline {
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

    case class A(
      children: Seq[NonLink],
      target: Target,
      alt: Opt[String],
      clz: Clz
    )
    extends Inline
    object A {
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
  sealed trait Target

  case class URL(override val toString: String)
    extends Target {
    def / (segment: String): URL = URL(s"$this/$segment")
  }

  trait HasId
    extends Target {
    def id: Id
  }
}
