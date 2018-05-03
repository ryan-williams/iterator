package org.hammerlab.iterator.docs

import hammerlab.indent.implicits.spaces2
import hammerlab.show._
import org.hammerlab.docs.Code.Example
import org.hammerlab.iterator.site.sections

import scalatags.generic._

trait module {
  type B
  type O <: F
  type F
  implicit val b: Bundle[B, O, F]
}

abstract class Mod[_B, _O <: _F, _F, _Bundle <: Bundle[_B, _O, _F]](_b: _Bundle)
  extends module {
  override type B = _B
  override type O = _O
  override type F = _F
  override implicit val b = _b
}

trait attr_dsl {
  self: module ⇒
  import b.all._
  object clz {
    def -(value: String): Modifier = `class` := value
  }

  def by[From, To](fn: From ⇒ To)(implicit av: AttrValue[To]): AttrValue[From] =
    (t: B, a: Attr, v: From) ⇒ av(t, a, fn(v))

  implicit def by[From, To](implicit av: AttrValue[To], fn: From ⇒ To): AttrValue[From] =
    (t: B, a: Attr, v: From) ⇒ av(t, a, fn(v))
}

trait symbol
  extends attr_dsl {
  self: module ⇒
  import b.all._
  implicit def symbolToString(s: Symbol): String = s.toString.drop(1)
  implicit def symbolToTextModifier(s: Symbol): Modifier = s.toString.drop(1)
}

case class URL(value: String)
object URL {
  trait utils
    extends attr_dsl {
    self: module ⇒
    import b.all._
    implicit val urlToAttrValue: AttrValue[URL] = by(_.value)
  }
}

trait interp
  extends symbol
     with elem {
  self: module ⇒
  import b.all._

  case class Arg(m: Modifier)
  object Arg {
    implicit def   string(s:   String): Arg = Arg(code(s))
    implicit def   symbol(s:   Symbol): Arg = Arg(code(s))
    implicit def modifier(m: Modifier): Arg = Arg(m)
    implicit def unwrap(a: Arg): Modifier = a.m
  }

  implicit class CodeContext(sc: StringContext) {
    def t(args: Arg*): Elem.Tag = {
      val strings =
        sc
          .parts
          .iterator
          .map{ s ⇒ s: Modifier }

      span(
        (
          strings.next ::
          args
            .iterator
            .zip(strings)
            .flatMap {
              case (arg, string) ⇒
                Iterator(
                  arg: Modifier,
                  string
                )
            }
            .toList
        ): _*
      )
    }

    def p(args: Arg*): Modifier = b.tags.p(t(args: _*).value)
  }
}

trait section {
  self: elem ⇒
  def ! : Elem.Section
}

trait Pkg {
  self: elem with module ⇒
  import Elem._
  import b.all._

  def pkg(body: Elem*)(implicit name: sourcecode.FullName): Section =
    h(
      Id(
        name
          .value
          .split("\\.")
          .dropWhile(_ != "docs")
          .drop(1)
          .head
      ),
      body
    )

  def c3(name: String) = h(name, code(name))
}

trait base
  extends Example.make
     with symbol
     with interp
     with attr_dsl
     with hammerlab.cmp.first
     with hammerlab.iterator.all
     with cats.instances.AllInstances
     with fence.utils
     with elem
     with Pkg
     with module
     with sections
