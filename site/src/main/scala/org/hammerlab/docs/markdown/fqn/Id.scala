package org.hammerlab.docs.markdown.fqn

import org.hammerlab.docs.markdown.dsl

case class Id(entries: Seq[dsl.Id]) {

  override def toString: String =
    entries
      .map(_.value)
      .mkString("-")

  def +(o: dsl.Id) = Id(entries :+ o)
  def suffixes: Iterator[Id] =
    entries
      .reverseIterator
      .scanLeft[List[dsl.Id]](Nil) {
        case (parts, next) ⇒
          next :: parts
      }
      .map(Id(_))
      .drop(1)
}
