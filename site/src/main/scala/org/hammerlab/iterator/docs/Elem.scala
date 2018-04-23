package org.hammerlab.iterator.docs

import hammerlab.option._

import scalatags.Text.all
import scalatags.Text.all._
import scalatags.{ Text, text }

sealed trait Elem {
  def compile: Seq[Modifier]
}
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
      (t: text.Builder, a: Attr, v: Id) ⇒ av(t, a, v.toString)
  }

  case class Elems(value: Seq[Elem]) extends Elem {
    override def compile: Seq[Modifier] = value.flatMap(_.compile)
  }
  implicit def wrapMultiple(elems: Seq[Elem]): Elems = Elems(elems)

  case class Section(id: Id,
                     name: String,
                     title: Modifier,
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

    def compile: Seq[Modifier] = compile()
    def compile(level: Int = 1,
                parent: Option[Id] = None,
                skipLevels: Int = 1): Seq[Modifier] = {
      val id = parent.fold { this.id } { _ + this.id }

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
          all.id := id,
          title
        )

      val link =
        a(
          href := s"#$id",
          header
        )

      val children =
        elems
          .flatMap {
            case Elems(elems) ⇒ elems
            case e ⇒ Seq(e)
          }
          .flatMap {
            case Tag(m) ⇒ Seq(m)
            case s: Section ⇒
              s.compile(
                level + 1,
                (level > skipLevels) ? id,
                skipLevels = skipLevels
              )
          }
          .toList

      link :: children
    }
  }

  case class Tag(value: Modifier) extends Elem {
    def compile: Seq[Modifier] = Seq(value)
  }
  object Tag {
    implicit def fromModifier(m: Modifier) = Tag(m)
    implicit def   toModifier(t: Tag) = t.value
  }

  implicit def fromModifier(m: Modifier): Elem = Tag(m)

  trait dsl {
    def h(id: Id, elems: Elem*) = Section(id, id, id: String, elems)
    def h(id: Id, title: Modifier, elems: Elem*) = Section(id, id, title, elems)
  }
  object dsl extends dsl
}
