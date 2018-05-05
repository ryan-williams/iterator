package org.hammerlab.iterator.site

import japgolly.scalajs.react.vdom.Attr.ValueType
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^.<._
import japgolly.scalajs.react.vdom.html_<^.^._
import japgolly.scalajs.react.vdom.html_<^._
import org.hammerlab.iterator.docs.{ URL, attr_dsl, symbol }
import org.scalajs.dom.html

import scala.scalajs.js

trait badge
  extends attr_dsl
     with symbol {

  implicit def urlToJS(url: URL): js.Any = url.value
  implicit val urlValue: ValueType[URL, String] = ValueType.byImplicit

  def badge(url: URL,
            alttext: String,
            image: URL) =
    a(
      clz - 'badge,
      href := url,
      alt := alttext,
      img(
        src := image
      )
    )

  def travis()(implicit gh: GitHub) = {
    val domain = "https://travis-ci.org"
    val base = s"$domain/${gh.user}/${gh.repo}"

    badge(
      URL(base),
      "Build Status",
      URL(s"$base.svg?branch=master")
    )
  }

  object coveralls {
    val domain = "https://coveralls.io"
    def apply()(implicit gh: GitHub) = {
      import gh._
      badge(
        URL(s"$domain/github/$user/$repo"),
        "Coverage Status",
        URL(s"$domain/repos/github/$user/$repo/badge.svg")
      )
    }
  }

  def mavenCentral()(implicit mc: MavenCoords) = {
    import mc._
    badge(
      URL(s"http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22$organization%22%20$baseName"),
      "Maven Central",
      URL(s"https://img.shields.io/maven-central/v/$organization/$name.svg?maxAge=1800")
    )
  }

  object github {
    val domain = s"https://github.com"

    val badge =
      img(
        clz - "github-badge",
        src := "./github.svg",
        alt := "Github Logo",
        title := "Github Logo"
      )

    def link(children: VdomNode*)(implicit gh: GitHub) = {
      import gh._
      a(
        href := s"$domain/$user/$repo",
        children.toVdomArray,
        badge
      )
    }

    // Link to a github issue
    def issue(org: String, repo: String, issue: Int, comment: Int): TagOf[html.Anchor] = this.issue(org, repo, issue, Some(comment))
    def issue(org: String, repo: String, issue: Int, comment: Option[Int] = None): TagOf[html.Anchor] =
      a(
        href := s"$domain/$org/$repo/issues/$issue${comment.fold("")(c ⇒ s"#issuecomment-$c")}",
        s"$org/$repo#$issue"
      )
  }
}
