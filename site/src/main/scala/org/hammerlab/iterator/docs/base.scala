package org.hammerlab.iterator.docs

import hammerlab.indent.implicits.spaces2
import hammerlab.show._
import org.hammerlab.docs.Code.Example
import org.hammerlab.iterator.docs.markdown._
import dsl._
import org.hammerlab.iterator.docs.markdown.tree.NonLink
import org.hammerlab.iterator.docs.markdown.tree.NonLink._
import shapeless.{ Inl, Inr }

trait symbol {
  implicit def symbolToString(s: Symbol): String = s.toString.drop(1)
  implicit def symbolToTextModifier(s: Symbol): Text = Text(s.toString.drop(1))
}

trait interp
  extends symbol {

  case class Arg(value: Inline)
  object Arg {
    implicit def   string(value: String): Arg = Code(value)
    implicit def   symbol(value: Symbol): Arg = Code(value)
    implicit def nonLinkArg(n: NonLink): Arg = Inl(n)
    implicit def    linkArg(a: Inline.A): Arg = Inr(Inl(a))
    implicit def modifier(value: Inline): Arg = Arg(value)
    implicit def unwrap(a: Arg): Inline = a.value
  }

  case class NArg(value: NonLink)
  object NArg {
    implicit def   string(value:  String): NArg = Text(value)
    implicit def   symbol(value:  Symbol): NArg = Code(value)
    implicit def     nArg(value: NonLink): NArg = NArg(value)
    implicit def unwrap(a: NArg): NonLink = a.value
  }

  implicit class CodeContext(sc: StringContext) {
    def n(args: NArg*): List[NonLink] = {
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
                Iterator(
                  arg.value,
                  string
                )
            }
            .toList
        )
    }

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

trait Pkg
  extends dsl {

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
     with dsl
     with symbol
     with hammerlab.cmp.first
     with hammerlab.iterator.all
     with cats.instances.AllInstances
     with Pkg
