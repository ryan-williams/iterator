package org.hammerlab.iterator.docs

import cats.instances
import hammerlab.indent.implicits.spaces2
import hammerlab.show._
import org.hammerlab.docs.Code
import org.hammerlab.docs.Code.Example

import scalatags.Text.{ Aggregate, Attrs, Cap, Styles }
import scalatags.{ DataConverters, Text, text }

trait symbol
  extends Aggregate {
  implicit def symbolToString(s: Symbol): String = s.toString.drop(1)
  implicit def symbolToTextModifier(s: Symbol): Text.Modifier = s.toString.drop(1)
  implicit def symbolToAttrValue(implicit av: AttrValue[String]): AttrValue[Symbol] =
    (t: text.Builder, a: Attr, v: Symbol) ⇒ av(t, a, v)
}

trait interp
  extends symbol {

  self:
      Cap
      with Aggregate
      with text.Tags
      with DataConverters
      with Attrs ⇒

  case class Arg(m: Modifier)
  object Arg {
    implicit def string(s: String): Arg = Arg(s)
    implicit def symbol(s: Symbol): Arg = Arg(s)
    implicit def modifier(m: Modifier): Arg = Arg(m)
    implicit def unwrap(a: Arg): Modifier = a.m
  }

  implicit class CodeContext(sc: StringContext) {
    def t(args: Arg*): Seq[Modifier] = {
      val strings =
        sc
        .parts
        .iterator
        .map{ s ⇒ s: Modifier }

      strings.next ::
        args
        .iterator
        .zip(strings)
        .flatMap {
          case (arg, string) ⇒
            Iterator(code(arg), string)
        }
        .toList
    }

    def p(args: Arg*): Modifier = scalatags.Text.tags.p(t(args: _*))
  }
}

trait base
  extends Cap
    with Attrs
    with Styles
    with text.Tags
    with DataConverters
    with Example.make
    with symbol
    with interp
    with hammerlab.cmp.first
    with hammerlab.iterator.all
    with cats.instances.AllInstances {

  def block(body: Code*) =
    pre(
      Code
        .lines(body: _*)
        .join("\n")
        .show
    )

  def pkgLink(name: String) =
    a(
      href := name,
      h1(name)
    )

  def pkg(name: String,
          body: Text.Modifier*) =
    div(
      (
        Seq(
          pkgLink(name)
        ) ++
        body
      ): _*
    )
}

object base extends base
