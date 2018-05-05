package org.hammerlab.iterator.docs

import hammerlab.indent.implicits.spaces2
import hammerlab.show._
import org.hammerlab.docs.Code.Example
import org.hammerlab.iterator.site.sections
import japgolly.scalajs.react.vdom.html_<^._
import <._, ^._
import japgolly.scalajs.react.vdom.TagOf
import org.scalajs.dom.html

trait attr_dsl {
  object clz {
    def -(value: String): TagMod = `class` := value
  }
}

trait symbol {
  implicit def symbolToString(s: Symbol): String = s.toString.drop(1)
  implicit def symbolToTextModifier(s: Symbol): VdomNode = s.toString.drop(1)
}

case class URL(value: String)

trait interp
  extends symbol {

  case class Arg(m: VdomElement)
  object Arg {
    implicit def   string(s:   String): Arg = Arg(code(s))
    implicit def   symbol(s:   Symbol): Arg = Arg(code(s))
    implicit def modifier(m: TagOf[html.Element]): Arg = Arg(m)
    implicit def unwrap(a: Arg): VdomElement = a.m
  }

  implicit class CodeContext(sc: StringContext) {
    def t(args: Arg*): Elem.Tag = {
      val strings =
        sc
          .parts
          .iterator
          .map{ span(_) }

        (
          (strings.next: VdomNode) ::
          args
            .iterator
            .zip(strings)
            .flatMap {
              case (arg, string) ⇒
                Iterator[VdomNode](
                  arg: VdomNode,
                  string
                )
            }
            .toList
        )
        .toVdomArray
    }

    def p(args: Arg*) = <.p(t(args: _*).value)
  }
}

trait section {
  def ! : Elem.Section
}

trait Pkg {
  import Elem._

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

  def c3(name: String) = h(name, title = code(name))
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
     with Pkg
     with sections
