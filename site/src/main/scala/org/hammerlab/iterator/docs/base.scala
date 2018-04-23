package org.hammerlab.iterator.docs

import hammerlab.indent.implicits.spaces2
import hammerlab.show._
import org.hammerlab.docs.Code
import org.hammerlab.docs.Code.{ Comment, Example }
import hammerlab.lines._

import scalatags.Text.{ Aggregate, Attrs, Cap, Styles }
import scalatags.Text.all.`class`
import scalatags.{ DataConverters, Text, text }

trait attr_dsl
  extends Aggregate {
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

case class URL(value: String)
object URL {
  trait utils
    extends Aggregate {
    implicit def urlToAttrValue(implicit av: AttrValue[String]): AttrValue[URL] =
      (t: text.Builder, a: Attr, v: URL) ⇒ av(t, a, v.value)
  }
}

trait interp
  extends symbol {

  self: Aggregate ⇒

  import scalatags.Text.tags
  import scalatags.Text.tags.{code, span}

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

    def p(args: Arg*): Modifier = tags.p(t(args: _*).value)
  }
}

trait section {
  import Elem._
  def ! : Section
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
    with attr_dsl
    with hammerlab.cmp.first
    with hammerlab.iterator.all
    with cats.instances.AllInstances
    with section
    with fence.utils
    with Elem.dsl {

  import Elem._

  def c3(name: String) = dsl.h(name, code(name))

  val github = s"https://github.com"

  // Link to a github issue
  def issue(org: String, repo: String, issue: Int, comment: Int): Modifier = this.issue(org, repo, issue, Some(comment))
  def issue(org: String, repo: String, issue: Int, comment: Option[Int] = None): Modifier =
    a(
      href := s"$github/$org/$repo/issues/$issue${comment.fold("")(c ⇒ s"#issuecomment-$c")}",
      s"$org/$repo#$issue"
    )

  def pkg(body: Elem*)(implicit name: sourcecode.Enclosing): Section =
    dsl.h(
      Id(
        name
          .value
          .split("\\.")
          .dropWhile(_ != "docs")
          .drop(1)
          .head
      ),
      body: _*
    )
}
