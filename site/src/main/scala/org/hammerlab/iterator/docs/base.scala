package org.hammerlab.iterator.docs

import hammerlab.indent.implicits.spaces2
import hammerlab.show._
import org.hammerlab.docs.Code.Example
import org.hammerlab.iterator.docs.markdown._, dsl._
import org.hammerlab.iterator.docs.markdown.tree.NonLink._

trait symbol {
  implicit def symbolToString(s: Symbol): String = s.toString.drop(1)
  implicit def symbolToTextModifier(s: Symbol): Text = Text(s.toString.drop(1))
}

trait interp
  extends symbol {

  case class Arg(value: Inline)
  object Arg {
    implicit def   string(value: String): Arg = Arg(Code(value))
    implicit def   symbol(value: Symbol): Arg = Arg(Code(value))
    implicit def modifier(value: Inline): Arg = Arg(value)
    implicit def unwrap(a: Arg): Inline = a.value
  }

  implicit class CodeContext(sc: StringContext) {
    def t(args: Arg*): List[Inline] = {
      val strings =
        sc
          .parts
          .iterator
          .map{ Text(_) }

        (
          strings.next ::
          args
            .iterator
            .zip(strings)
            .flatMap {
              case (arg, string) ⇒
                Iterator[Inline](
                  arg.value,
                  string
                )
            }
            .toList
        )
    }

    def p(args: Arg*) = P(t(args: _*))
  }
}

trait Pkg {

  def pkg(body: Elem*)(implicit name: sourcecode.FullName): Section =
    section(
      Text(
        name
          .value
          .split("\\.")
          .dropWhile(_ != "docs")
          .drop(1)
          .head
        ),
      body: _*
    )

  def c3(name: String) = section(Code(name))
}

trait base
  extends Example.make
     with symbol
     with interp
     with hammerlab.cmp.first
     with hammerlab.iterator.all
     with cats.instances.AllInstances
     with Pkg
