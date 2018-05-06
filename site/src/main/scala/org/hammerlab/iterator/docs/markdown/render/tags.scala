package org.hammerlab.iterator.docs.markdown.render

import hammerlab.indent.implicits.spaces2
import org.hammerlab.iterator.docs.Opt
import org.hammerlab.iterator.docs.markdown._
import tree._
import dsl._
import NonLink._
import org.hammerlab.iterator.docs.markdown.util.{ Clz, URL }
import shapeless.{ Inl, Inr }

object tags {

  object  Text extends Render(scalatags. Text)
  object JsDom extends Render(scalatags.JsDom)

  import scalatags.generic.{ Bundle, TypedTag }

  abstract class Render[
    Builder,
    Output <: FragT,
    FragT
  ](
    val bundle: Bundle[Builder, Output, FragT]
  ) {
    import bundle.all._
    type Tag = TypedTag[Builder, Output, FragT]

    private def by[From, To](fn: From ⇒ To)(implicit av: AttrValue[To]): AttrValue[From] =
      (t: Builder, a: Attr, v: From) ⇒ av(t, a, fn(v))

    implicit val urlToAttrValue: AttrValue[URL] = by(_.toString)
    implicit val targetToAttrValue: AttrValue[Target] =
      by {
        case Inl(url) ⇒ url.toString
        case Inr(Inl(target)) ⇒ s"#${target.id}"
      }
    implicit val  idToAttrValue: AttrValue[ Id] = by(_.value)
    implicit val clzToAttrValue: AttrValue[Clz] = by(_.values.mkString(" "))
    implicit def optToAttrValue[T](implicit a: AttrValue[Option[T]]): AttrValue[Opt[T]] = by(Opt.toStd[T])

    implicit def optionAttrValue[T](implicit av: AttrValue[T]): AttrValue[Option[T]] =
      (t: Builder, a: Attr, v: Option[T]) ⇒
        v.foreach {
          av(t, a, _)
        }

    def plain(plain: NonLink): Tag =
      plain match {
        case NonLink.Text(value) ⇒ span(value)  // TODO: is wrapping in a <span> necessary / desirable?
        case            B(value) ⇒    b(value)
        case            I(value) ⇒    i(value)
        case          Del(value) ⇒  del(value)
        case         Code(value) ⇒ code(value)
        case  NonLink.Img(_src, _alt, clz) ⇒
          img(
               src  := _src,
            `class` :=  clz,
               alt  := _alt
          )
      }

    def apply(inline: Inline): Tag =
      inline match {
        case Inl(plain) ⇒ this.plain(plain)
        case Inr(Inl(Inline.A(children, url, _alt, clz))) ⇒
          a(
            href := url,
            alt := _alt,
            `class` := clz,
            children.map(this.plain)
          )
      }

    def apply(item: LI): Tag =
      li(
        apply(item.title),
        item.elems.map(apply)
      )

    def apply(elem: NonSection): Tag =
      elem match {
        case     P(elems) ⇒   p(elems.map(apply): _*)
        case    UL(items) ⇒  ul(items.map(apply): _*)
        case    OL(items) ⇒  ol(items.map(apply): _*)
        case Fence(lines) ⇒ pre(code(lines.showLines))
      }

    def apply(elem: Elem, level: Int = 1): Tag =
      elem match {
        case Section(title, _id, children) ⇒
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
            id := _id,
            title.map(apply),
            children.map(apply(_, level + 1))
          )
        case n: NonSection ⇒ apply(n)
      }
  }
}
