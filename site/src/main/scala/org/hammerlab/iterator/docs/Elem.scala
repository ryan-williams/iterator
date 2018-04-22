package org.hammerlab.iterator.docs

import scalatags.Text.all
import scalatags.Text.all._

sealed trait Elem {
  def compile: Seq[Modifier]
}
object Elem {

  case class Id(value: String)
  object Id {
    implicit def fromString(s: String) = Id(s)
    implicit def fromSymbol(s: Symbol) = Id(s.toString.drop(1))
    implicit def unwrap(id: Id): String = id.value
  }

  case class Section(id: Id,
                     name: String,
                     elems: Seq[Elem])
    extends Elem {

    /**
     * Recursively filter to only [[Section]]s
     */
    def tree: Section =
      copy(
        elems =
          elems
            .collect {
              case s: Section ⇒
                s.tree
            }
      )

    def compile: Seq[Modifier] = compile(1)
    def compile(level: Int): Seq[Modifier] = {
      val header =
        (
          level match {
            case 1 ⇒ h1
            case 2 ⇒ h2
            case 3 ⇒ h3
            case 4 ⇒ h4
            case 5 ⇒ h5
            case _ ⇒
              throw new IllegalStateException(
                s"Nested too deep ($level levels) during section compilation"
              )
          }
        ) (
          all.id := id.value,
          id.value
        )

      val link =
        a(
          href := '#' + id.value,
          header
        )

      val children =
        elems
          .flatMap {
            case Tag(tags @ _*) ⇒ tags
            case s: Section ⇒ s.compile(level + 1)
          }
          .toList

      link :: children
    }
  }

  case class Tag(value: Modifier*) extends Elem {
    def compile: Seq[Modifier] = value
  }
  object Tag {
    implicit def fromModifier(m: Modifier) = Tag(m)
  }

  implicit def fromModifier(m: Modifier): Elem = Tag(m)

  object dsl {
    def h(id: Id, elems: Elem*) = Section(id, id.value, elems)
  }
}
