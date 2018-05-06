package org.hammerlab.iterator.docs.markdown.render

import hammerlab.indent.implicits.spaces2
import japgolly.scalajs.react.internal.OptionLike
import japgolly.scalajs.react.raw
import org.hammerlab.iterator.docs.markdown._, Elem._, Inline._, NonLink._
import org.hammerlab.iterator.docs._, Opt.Non

object react {

  import japgolly.scalajs.react.vdom.html_<^._
  import <._
  import ^._
  import japgolly.scalajs.react.vdom.Attr.ValueType

  type Tag = VdomNode

  case class Key(override val toString: String)
  object Key
    extends symbol {
    implicit def int(idx: Int): Key = Key(idx.toString)
    implicit def str(key: String): Key = Key(key)
    implicit def sym(key: Symbol): Key = Key(key)
  }

  implicit val optLike: OptionLike[Opt] =
    new OptionLike[Opt] {
      type O[A] = Opt[A]
      def map     [A, B](o: O[A])        (f: A ⇒ B): O[B]      = o map f
      def fold    [A, B](o: O[A], b: ⇒ B)(f: A ⇒ B): B         = o.fold(b)(f)
      def foreach [A]   (o: O[A])(f: A ⇒ Unit)     : Unit      = o foreach f
      def isEmpty [A]   (o: O[A])                  : Boolean   = o.isEmpty
      def toOption[A]   (o: O[A])                  : Option[A] = o
    }

  implicit val idAttr: ValueType[Id, String] = ValueType.byImplicit(_.toString)

  implicit def convKey(k: Key): raw.React.Key = k.toString

  def key(implicit k: Key) = ^.key := k

  def plain(plain: NonLink)(implicit k: Key): Tag =
    plain match {
      case Text(value) ⇒ span(key, value)  // TODO: is wrapping in a <span> necessary / desirable?
      case    B(value) ⇒    b(key, value)
      case    I(value) ⇒    i(key, value)
      case  Del(value) ⇒  del(key, value)
      case Code(value) ⇒ code(key, value)
    }

  implicit val urlAttr: ValueType[URL, String] = ValueType.byImplicit(_.toString)
  implicit val clzAttr: ValueType[Clz, String] = ValueType.byImplicit(_.values.mkString(" "))

  def apply(value: Inline)(implicit k: Key): Tag =
    value match {
      case plain: NonLink ⇒ this.plain(plain)
      case Inline.Img(_src, _alt, clz) ⇒
        img(
          key,
             src  :=  _src,
          `class` :=   clz,
             alt  :=? _alt
        )
      case A(children, url, _alt, clz) ⇒
        a(
          key,
          href := url,
          alt :=? _alt,
          `class` := clz,
          children
            .zipWithIndex
            .toVdomArray {
              case  (e, idx) ⇒
                apply(e)(idx)
            }
        )
    }

  def apply(item: LI)(implicit k: Key): Tag =
    li(
      key,
      apply(item.title),
      item.elems.toVdomArray(apply)
    )

  def apply(elem: NonSection)(implicit k: Key): Tag =
    elem match {
      case P(elems) ⇒
        p(
          key,
          elems
            .zipWithIndex
            .toVdomArray {
              case  (elem, idx) ⇒
                apply(elem)(idx)
            }
        )
      case Fence(lines) ⇒ pre(key, code(lines.showLines))
      case UL(items) ⇒
        ul(
          key,
          items
            .zipWithIndex
            .toVdomArray {
              case (e, i) ⇒
                apply(e)(i)
            }
        )
      case OL(items) ⇒
        ol(
          key,
          items
            .zipWithIndex
            .toVdomArray {
              case (e, i) ⇒
                apply(e)(i)
            }
        )
    }

  def apply(elem: Elem, level: Int = 1)(implicit k: Key): Tag =
    elem match {
      case Section(title, id, children) ⇒
        (
          level match {
            case 1 ⇒ h1
            case 2 ⇒ h2
            case 3 ⇒ h3
            case 4 ⇒ h4
            case 5 ⇒ h5
            case 6 ⇒ h6
            case _ ⇒
              throw new IllegalStateException(
                s"Nested too deep ($level levels) during section compilation"
              )
          }
        ) (
          key,
          ^.id := id,
          title.toVdomArray(apply),
          children.toVdomArray(apply(_, level + 1))
        )
      case n: NonSection ⇒ apply(n)
    }
}
