package org.hammerlab.docs.markdown.render

import hammerlab.indent.implicits.spaces2
import japgolly.scalajs.react.internal.OptionLike
import japgolly.scalajs.react.{ raw, vdom }
import org.hammerlab.docs.markdown.{ fqn, tree }
import tree._
import fqn._
import fqn.tree._
import Inline._
import NonLink._
import org.hammerlab.docs._
import org.hammerlab.docs.markdown.tree.NonLink
import org.hammerlab.docs.markdown.util.{ Clz, URL, symbol }
import shapeless.{ Inl, Inr }

import scala.scalajs.js

object react {

  import japgolly.scalajs.react.vdom.html_<^._
  import <._
  import ^._
  import japgolly.scalajs.react.vdom.Attr.ValueType

  type Tag = VdomElement

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

  implicit def convKey(k: Key): raw.React.Key = k.toString

  def key(implicit k: Key) = ^.key := k

  def plain(plain: NonLink)(implicit k: Key): Tag =
    plain match {
      case         Text(value) ⇒ span(key, value)  // TODO: is wrapping in a <span> necessary / desirable?
      case            B(value) ⇒    b(key, value)
      case            I(value) ⇒    i(key, value)
      case          Del(value) ⇒  del(key, value)
      case NonLink.Code(value) ⇒ code(key, value)
      case NonLink.Img(_src, _alt, clz) ⇒
        img(
          key,
             src  := _src,
          `class` :=  clz,
             alt  := _alt
        )
    }

  def by[A, U](implicit f: A ⇒ js.Any): ValueType[A, U] = ValueType.byImplicit(f)

  implicit val  idAttr: ValueType[ Id, vdom.Attr.Key] = by(_.toString)
  implicit val urlAttr: ValueType[URL,        String] = by(_.toString)
  implicit val targetAttr: ValueType[Target, String] =
    by {
      case Inl(url) ⇒ url.toString
      case Inr(Inl(target)) ⇒ s"#${target.id}"
    }
  implicit val clzAttr: ValueType[Clz, String] = ValueType.byImplicit(_.values.mkString(" "))

  implicit def inlines(value: Seq[Inline]): VdomNode =
    value
      .zipWithIndex
      .toVdomArray {
        case (e, i) ⇒
        apply(e)(i)
      }

  def apply(value: Inline)(implicit k: Key): Tag =
    value match {
      case Inl(plain) ⇒ this.plain(plain)
      case Inr(Inl(A(children, url, _alt, clz))) ⇒
        a(
          key,
          href := url,
          alt :=? _alt,
          `class` := clz,
          inlines(children)
        )
    }

  def apply(item: LI)(implicit k: Key): Tag =
    li(
      key,
      apply(item.title),
      item.elems
    )

  implicit def listitems(elems: Seq[LI]): VdomNode =
    elems
      .zipWithIndex
      .toVdomArray {
        case (e, i) ⇒
          apply(e)(i)
      }

  implicit def nonsections(elems: Seq[NonSection]): VdomNode =
    elems
      .zipWithIndex
      .toVdomArray {
        case (e, i) ⇒
        apply(e)(i)
      }

  def nonsection(elem: NonSection)(implicit k: Key): Tag =
    elem match {
      case P(elems) ⇒
        p(
          key,
          elems
        )
      case Fence(lines) ⇒ pre(key, code(lines.showLines))
      case    UL(items) ⇒  ul(key, items)
      case    OL(items) ⇒  ol(key, items)
    }

  def apply(elem: Elem, level: Int = 1)(implicit k: Key): VdomNode =
    elem match {
      case Section(title, id, children) ⇒
        val h = (
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
          title
        )

        children match {
          case Seq() ⇒ h
          case children ⇒
            VdomArray(
              h,
              children
                .zipWithIndex
                .toVdomArray {
                  case (e, i) ⇒
                    apply(e, level + 1)(i)
                }
            )
        }
      case n: NonSection ⇒ nonsection(n)
    }
}
