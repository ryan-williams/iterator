package org.hammerlab.iterator

import hammerlab.indent.implicits.spaces2
import hammerlab.lines.Lines
import hammerlab.show._
import org.hammerlab.docs.Code

import scalatags.Text.all._
import scalatags.{ Text, text }

trait docs {
  implicit def symbolToString(s: Symbol): String = s.toString.drop(1)
  implicit def symbolToTextModifier(s: Symbol): Text.Modifier = s.toString.drop(1)
  implicit def symbolToAttrValue(implicit av: AttrValue[String]): AttrValue[Symbol] =
    (t: text.Builder, a: Attr, v: Symbol) ⇒ av(t, a, v)

  def code(body: Code*) =
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

  def pkg(name: String, body: Text.Modifier*) =
    div(
      (
        Seq(
          pkgLink(name)
        ) ++
        body
      ): _*
    )
}

object docs extends docs
