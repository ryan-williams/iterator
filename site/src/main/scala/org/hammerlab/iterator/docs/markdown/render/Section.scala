package org.hammerlab.iterator.docs.markdown.render

import org.hammerlab.iterator.docs.Opt.Non
import org.hammerlab.iterator.docs.{ Opt, markdown }
import org.hammerlab.iterator.docs.markdown.Elem.NonSection
import org.hammerlab.iterator.docs.markdown.{ Elem, Inline }

case class Id(entries: Seq[markdown.Id]) {
  def +(o: markdown.Id) = Id(entries :+ o)
  def suffixes: Iterator[Id] =
    entries
      .reverseIterator
      .scanLeft[List[markdown.Id]](Nil) {
        case (parts, next) ⇒
          next :: parts
      }
      .map(Id(_))
      .drop(1)
}

case class Section(
  title: Seq[Inline],
  id: Id,
  elems: Seq[Either[Section, NonSection]]
)
object Section {
  def apply(section: Elem.Section, parent: Opt[Id] = Non, seen: Set[Id] = Set()): Section = {
    val id =
      parent
        .fold[Id] {
          Id(Seq(section.id))
        } {
          _ + section.id
        }

    val prefixed =
      id
        .suffixes
        .find(!seen(_)) match {
          case Some(id) ⇒ id
          case None ⇒
            throw new IllegalStateException(
              s"No valid suffixes found for $id: ${id.suffixes.mkString(",")} ${seen.mkString(",")}"
            )
        }

    val next = seen + prefixed

    Section(
      section.title,
      prefixed,
      section
        .elems
        .map {
          case s: Elem.   Section ⇒  Left(apply(s, id))
          case s: Elem.NonSection ⇒ Right(s)
        }
    )
  }
}
