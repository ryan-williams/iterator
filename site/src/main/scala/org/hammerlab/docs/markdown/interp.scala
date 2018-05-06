package org.hammerlab.docs.markdown

import org.hammerlab.docs.markdown.dsl._
import org.hammerlab.docs.markdown.tree.NonLink
import org.hammerlab.docs.markdown.tree.NonLink.{ Code, Text }
import org.hammerlab.docs.markdown.util.symbol

trait interp
  extends symbol {

  case class Arg(value: Inline)
  object Arg {
    implicit def   string(value: String): Arg = Code(value)
    implicit def   symbol(value: Symbol): Arg = Code(value)
    implicit def nonLinkArg(n:  NonLink): Arg = L(n)
    implicit def    linkArg(a: Inline.A): Arg = R(a)
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
