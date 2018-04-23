package org.hammerlab.iterator.docs

import scala.collection.mutable
import scalatags.Text.all
import scalatags.Text.all._

sealed trait Tree
object Tree {

  case class Id(value: Seq[Elem.Id]) {
    def +(o: Elem.Id) = Id(value :+ o)
    def suffixes: Iterator[Id] =
      value
        .reverseIterator
        .scanRight[Id](Id(Nil)) {
          case (next, Id(parts)) ⇒
            Id(next +: parts)
        }
        .drop(1)

    def id = value.mkString("-")
    def href = s"#$id"
  }
  implicit def fromId(id: Elem.Id) = Id(Vector(id))
  implicit def unwrap(id: Id): Seq[Elem.Id] = id.value

  case class Section(id: Id,
                     title: Modifier,
                     children: Seq[Tree])
    extends Tree {
    def reduceIds: Section = {
      reduceIds(mutable.Set[Id]())
    }
    def reduceIds(seen: mutable.Set[Id]): Section = {
      val reduced =
        id
          .suffixes
          .find(!seen(_))
          .get

      seen.add(reduced)
      Section(
        reduced,
        title,
        children
          .map {
            case s: Section ⇒ s.reduceIds(seen)
            case t ⇒ t
          }
      )
    }

    def compile: Modifier = compile()
    def compile(level: Int = 1): Modifier = {
      val header =
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
          all.id := id.id,
          title
        )

      val link =
        a(
          href := id.href,
          header
        )

      div(
        link,
        children
          .map {
            case s: Section ⇒ s.compile(level + 1)
            case Tag(elem) ⇒ elem
          }
      )
    }
  }

  object Section {
    def apply(s: Elem.Section): Section = apply(s, None)
    def apply(s: Elem.Section, parent: Option[Id]): Section = {
      val id = parent.fold[Id] { s.id } { _ + s.id }
      Section(
        id,
        s.title,
        s
          .elems
          .map {
            case s: Elem.Section ⇒ apply(s, Some(id))
            case t: Elem.Tag ⇒ t: Tag
          }
      )
    }
  }

  case class Tag(value: Modifier) extends Tree
  implicit def convertTag(t: Elem.Tag): Tag = Tag(t.value)
}
