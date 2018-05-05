package org.hammerlab.iterator.docs

import japgolly.scalajs.react.vdom.TopNode
import japgolly.scalajs.react.vdom.html_<^._

//sealed trait Elem
//object Elem {
//
//  case class Id(value: String) {
//    override def toString: String = value
//    def +(o: Id) = Id(s"$value-${o.value}")
//  }
//  object Id {
//    implicit def fromString(s: String) = Id(s)
//    implicit def fromSymbol(s: Symbol) = Id(s.toString.drop(1))
//    implicit def unwrap(id: Id): String = id.toString
//  }
//
//  case class Section(id: Id,
//                     title: VdomNode,
//                     elems: Seq[Elem])
//    extends Elem
//
//  case class Tag(value: VdomNode) extends Elem {
//    def compile: Seq[VdomNode] = Seq(value)
//  }
//  object Tag {
//    implicit def fromModifier[N <: TopNode](m: VdomTagOf[N]): Tag = Tag(m)
//    implicit def   toModifier[N <: TopNode](t: Tag) = t.value
//  }
//
//  implicit def fromModifier[N <: TopNode](m: VdomTagOf[N]): Elem = Tag(m)
//
//  sealed trait Arg
//  object Arg {
//    implicit def fromModifier[N <: TopNode](m: VdomTagOf[N]): Arg = Tag(m)
//  }
//  implicit class Single(val elem: Elem) extends Arg
//  implicit class Multi(val elems: Seq[Elem]) extends Arg
//
//  implicit def unroll(args: Seq[Arg]): Seq[Elem] =
//    args.flatMap {
//      case s: Single ⇒ Seq(s.elem)
//      case m: Multi ⇒ m.elems
//    }
//
//  def h(id: Id, title: VdomNode, elems: Arg*) = Section(id, title, elems)
//  def h(id: Id, elems: Arg*) = Section(id, id: String, elems)
//}
