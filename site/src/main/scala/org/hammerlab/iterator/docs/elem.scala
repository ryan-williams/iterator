package org.hammerlab.iterator.docs

trait elem {
  self: module ⇒
  import b.all._

  sealed trait Elem
  object Elem {

    case class Id(value: String) {
      override def toString: String = value
      def +(o: Id) = Id(s"$value-${o.value}")
    }
    object Id {
      implicit def fromString(s: String) = Id(s)
      implicit def fromSymbol(s: Symbol) = Id(s.toString.drop(1))
      implicit def unwrap(id: Id): String = id.toString
      implicit def symbolToAttrValue(implicit av: AttrValue[String]): AttrValue[Id] =
        (t: Builder, a: Attr, v: Id) ⇒ av(t, a, v.toString)
    }

    case class Section(id: Id,
                       title: Modifier,
                       elems: Seq[Elem])
      extends Elem

    case class Tag(value: Modifier) extends Elem {
      def compile: Seq[Modifier] = Seq(value)
    }
    object Tag {
      implicit def fromModifier(m: Modifier) = Tag(m)
      implicit def   toModifier(t: Tag) = t.value
    }

    implicit def fromModifier(m: Modifier): Elem = Tag(m)

    sealed trait Arg
    object Arg {
      implicit def fromModifier(m: Modifier): Arg = Tag(m)
    }
    implicit class Single(val elem: Elem) extends Arg
    implicit class Multi(val elems: Seq[Elem]) extends Arg
    implicit def unroll(args: Seq[Arg]): Seq[Elem] =
      args.flatMap {
        case s: Single ⇒ Seq(s.elem)
        case m: Multi ⇒ m.elems
      }

    def h(id: Id, title: Modifier, elems: Arg*) = Section(id, title, elems)
    def h(id: Id, elems: Arg*) = Section(id, id: String, elems)
  }
}
