package org.hammerlab.iterator.docs

import hammerlab.indent.implicits.spaces2
import hammerlab.show._
import org.hammerlab.docs.Code
import org.hammerlab.docs.Code.{ Comment, Example }
import hammerlab.lines._

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
    implicit def string(s: String): Arg = Arg(code(s))
    implicit def symbol(s: Symbol): Arg = Arg(code(s))
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
              Iterator(arg: Modifier, string)
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

  def c3(name: String) = h3(code(name))

  val github = s"https://github.com"

  // Link to a github issue
  def issue(org: String, repo: String, issue: Int, comment: Int): Modifier = this.issue(org, repo, issue, Some(comment))
  def issue(org: String, repo: String, issue: Int, comment: Option[Int] = None): Modifier =
    a(
      href := s"$github/$org/$repo/issues/$issue${comment.fold("")(c ⇒ s"#issuecomment-$c")}",
      s"$org/$repo#$issue"
    )

  def fence(body: Code*) =
    pre(
      code(
        clz - 'scala,
        Lines(
          body
            .map {
              case c: Comment ⇒
                // don't add an extra newline after Comments; call-site can do that if desired
                Comment.lines(c)
              case c ⇒
                Lines(Code.lines.apply(c), "")
            }: _*
        )
        .showLines
      )
    )

  def pkgLink(name: String) =
    a(
      href := '#' + name,
      id := name,
      h1(name)
    )

  def pkg(body: Text.Modifier*)(implicit name: sourcecode.Enclosing): Modifier =
    pkg(
      name
        .value
        .split("\\.")
        .dropWhile(_ != "docs")
        .drop(1)
        .head,
      body: _*
    )

  def pkg(name: String,
          body: Text.Modifier*): Modifier =
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
