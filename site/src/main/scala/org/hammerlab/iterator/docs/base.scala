package org.hammerlab.iterator.docs

import hammerlab.indent.implicits.spaces2
import hammerlab.show._
import org.hammerlab.docs.Code
import org.hammerlab.docs.Code.Example

import scalatags.Text.all.`class`
import scalatags.Text.{ Aggregate, Attrs, Cap, Styles }
import scalatags.{ DataConverters, Text, text }

trait utils
  extends Attrs {
  object clz {
    def -(name: String) = `class` := name
  }
}

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
    with utils
    with hammerlab.cmp.first
    with hammerlab.iterator.all
    with cats.instances.AllInstances {

  /**
   * Optionally override by mixing-in a trait annotated with the [[org.hammerlab.docs.Code.Setup.setup]] macro
   *
   * This stub is useful for helping IntelliJ not worry about a macro-generated `setup` member in such cases
   */
  // TODO: add self-type `base` to setup-macro's output trait, and add "override" modifier to the `setup` member there,
  // so that this works
  //def setup: Setup = null

  def block(body: Code*) =
    pre(
      code(
        clz - 'scala,
        Code
          .lines(body: _*)
          .join("\n")
          .show
      )
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
