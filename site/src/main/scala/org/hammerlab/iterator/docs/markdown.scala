package org.hammerlab.iterator.docs

import hammerlab.lines._
import hammerlab.indent.implicits.spaces2
import japgolly.scalajs.react.internal.OptionLike
import japgolly.scalajs.react.raw
import org.hammerlab.iterator.docs.markdown.Opt.Non

object markdown {

  object dsl {
    import org.hammerlab.docs.Code
    import org.hammerlab.docs.Code.Setup
    import org.hammerlab.iterator.docs.markdown.Elem.{ Fence, P }

    def section(title: Inline, elems: Elem*): Section = Section(title, elems)
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
  }

  sealed trait Opt[+T]
  object Opt {
    case object Non extends Opt[Nothing]
    case  class Som[T](value: T) extends Opt[T]

    implicit def lift[T](t: T): Opt[T] = Som(t)
    implicit def toStd[T](t: Opt[T]): Option[T] =
      t match {
        case Non    ⇒ None
        case Som(t) ⇒ Some(t)
      }
    implicit def fromStd[T](t: Option[T]): Opt[T] =
      t match {
        case None    ⇒ Non
        case Some(t) ⇒ Som(t)
      }
  }

  case class Clz(values: Seq[Clz.Entry] = Nil) {
    def &(entry: Clz.Entry): Clz = Clz(values :+ entry)
  }
  object Clz extends symbol {
    case class Entry(value: String)
    object Entry {
      implicit def fromSym(value: String): Entry = Entry(value)
      implicit def fromStr(value: Symbol): Entry = Entry(value)
    }
    implicit def fromSym  (value: String): Clz = Clz(value :: Nil)
    implicit def fromStr  (value: Symbol): Clz = Clz(value :: Nil)
    implicit def fromEntry(value: Entry): Clz = Clz(value :: Nil)
    implicit def fromEntries(values: Seq[Entry]): Clz = Clz(values)
  }

  sealed trait Inline
  object Inline {
    case class A(
      children: Seq[Plain],
      url: URL,
      alt: Opt[String] = Non,
      clz: Clz = Nil
    )
    extends Inline

    // <img>; folds in optional wrapping with an anchor tag (<a>)
    case class Img(
      src: URL,
      alt: Opt[String] = Non,
      href: Opt[   URL] = Non,
      clz: Clz = Nil
    )
    extends Inline

    sealed trait Plain extends Inline
    object Plain {
      case class Text(value: String) extends Plain
      case class    B(value: String) extends Plain
      case class    I(value: String) extends Plain
      case class  Del(value: String) extends Plain
      case class Code(value: String) extends Plain

      object Text { implicit def wrap(v: String): Text = Text(v) }
    }

    implicit def wrap(v: String): Inline = Plain.Text(v)
  }

  // "Top-level" elements within a section: <p>, <ol>, <ul>
  sealed trait Elem
  object Elem {

    sealed trait NonSection extends Elem

    // <p>
    case class P(elems: Seq[Inline]) extends NonSection

    // <pre><code>…</code></pre>: "fenced" code block
    case class Fence(lines: Lines) extends NonSection

    // <li>
    case class LI(title: Inline, elems: Seq[NonSection])

    // Lists: <ul>, <ol>
    case class UL(items: Seq[LI]) extends NonSection
    case class OL(items: Seq[LI]) extends NonSection
  }

  case class Id(value: String)

  case class Section(
    title: Inline,
    id: Opt[Id] = Non,
    elems: Seq[Elem] = Nil
  )
  extends Elem

  object Section {

    def apply(title: Inline, elems: Seq[Elem]): Section = new Section(title, elems = elems)

    import Inline._, Plain._
    import Elem._

    import scalatags.generic.{ Bundle, TypedTag }
    abstract class Render[
      Builder,
      Output <: FragT,
      FragT
    ](
      val bundle: Bundle[Builder, Output, FragT],
    ) {
      import bundle.all._
      type Tag = TypedTag[Builder, Output, FragT]

      private def by[From, To](fn: From ⇒ To)(implicit av: AttrValue[To]): AttrValue[From] =
        (t: Builder, a: Attr, v: From) ⇒ av(t, a, fn(v))

      implicit val urlToAttrValue: AttrValue[URL] = by(_.value)
      implicit val  idToAttrValue: AttrValue[ Id] = by(_.value)
      implicit val clzToAttrValue: AttrValue[Clz] = by(_.values.mkString(" "))
      implicit def optToAttrValue[T](implicit a: AttrValue[Option[T]]): AttrValue[Opt[T]] = by(Opt.toStd[T])

      implicit def optionAttrValue[T](implicit av: AttrValue[T]): AttrValue[Option[T]] =
        (t: Builder, a: Attr, v: Option[T]) ⇒
          v.foreach {
            av(t, a, _)
          }

      def plain(plain: Plain, _id: Opt[Id] = Non): Tag =
        plain match {
          case Text(value     ) ⇒ span(id := _id, value)  // TODO: is wrapping in a <span> necessary / desirable?
          case    B(value     ) ⇒    b(id := _id, value)
          case    I(value     ) ⇒    i(id := _id, value)
          case  Del(value     ) ⇒  del(id := _id, value)
          case Code(value     ) ⇒ code(id := _id, value)
        }

      def inline(inline: Inline, _id: Opt[Id] = Non): Tag =
        inline match {
          case plain: Plain ⇒ this.plain(plain, _id)
          case Img(_src, _alt, _href, clz) ⇒
            val tag =
              img(
                id := _id,
                src := _src,
                `class` := clz,
                _alt.map(alt := _),
              )

            _href.fold[Tag] {
              tag
            } {
              _href ⇒
                a(
                  href := _href,
                  tag
                )
            }
          case A(children, url, _alt, clz) ⇒
            a(
              id := _id,
              href := url,
              alt := _alt,
              `class` := clz,
              children.map(this.plain(_))
            )
        }

      def apply(item: LI): Tag =
        li(
          inline(item.title),
          item.elems.map(apply)
        )

      def apply(elem: NonSection): Tag =
        elem match {
          case P(elems) ⇒
            p(
              elems.map(inline(_)): _*
            )
          case Fence(lines) ⇒ pre(code(lines.showLines))
          case UL(items) ⇒ ul(items.map(apply): _*)
          case OL(items) ⇒ ol(items.map(apply): _*)
        }


      def apply(elem: Elem, level: Int = 1): Tag =
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
              inline(title, id),
              children.map(apply(_, level + 1))
            )
          case n: NonSection ⇒ apply(n)
        }
    }

    object RenderText extends Render(scalatags.Text)
    object RenderJsDom extends Render(scalatags.JsDom)

    object RenderReact {

      import japgolly.scalajs.react.vdom.html_<^._, <._, ^._
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
          def map     [A, B](o: O[A])(f: A => B)         : O[B]      = o map f
          def fold    [A, B](o: O[A], b: => B)(f: A => B): B         = o.fold(b)(f)
          def foreach [A]   (o: O[A])(f: A => Unit)      : Unit      = o foreach f
          def isEmpty [A]   (o: O[A])                    : Boolean   = o.isEmpty
          def toOption[A]   (o: O[A])                    : Option[A] = o
        }

      implicit val idAttr: ValueType[Id, String] = ValueType.byImplicit(_.toString)

      implicit def convKey(k: Key): raw.React.Key = k.toString

      def key(implicit k: Key) = ^.key := k

      def plain(plain: Plain, _id: Opt[Id] = Non)(implicit k: Key): Tag =
        plain match {
          case Text(value) ⇒ span(key, id :=? _id, value)  // TODO: is wrapping in a <span> necessary / desirable?
          case    B(value) ⇒    b(key, id :=? _id, value)
          case    I(value) ⇒    i(key, id :=? _id, value)
          case  Del(value) ⇒  del(key, id :=? _id, value)
          case Code(value) ⇒ code(key, id :=? _id, value)
        }

      implicit val urlAttr: ValueType[URL, String] = ValueType.byImplicit(_.toString)
      implicit val clzAttr: ValueType[Clz, String] = ValueType.byImplicit(_.values.mkString(" "))

      def inline(value: Inline, _id: Opt[Id] = Non)(implicit k: Key): Tag =
        value match {
          case plain: Plain ⇒ this.plain(plain, _id)
          case Img(_src, _alt, _href, clz) ⇒
            val tag =
              img(
                key,
                id :=? _id,
                src := _src,
                `class` := clz,
                alt :=? _alt
              )

            _href.fold[Tag] {
              tag
            } {
              _href ⇒
                a(
                  key,
                  href := _href,
                  tag
                )
            }
          case A(children, url, _alt, clz) ⇒
            a(
              key,
              id :=? _id,
              href := url,
              alt :=? _alt,
              `class` := clz,
              children
                .zipWithIndex
                .toVdomArray {
                  case  (e, idx) ⇒
                  inline(e)(idx)
                }
            )
        }

      def apply(item: LI)(implicit k: Key): Tag =
        li(
          key,
          inline(item.title),
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
                  inline(elem)(idx)
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
              inline(title),
              children.toVdomArray(apply(_, level + 1))
            )
          case n: NonSection ⇒ apply(n)
        }
    }
  }
}
