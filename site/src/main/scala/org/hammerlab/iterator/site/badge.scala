package org.hammerlab.iterator.site

import japgolly.scalajs.react.vdom.Attr.ValueType
import org.hammerlab.iterator.docs.markdown.Inline.Plain.Text
import org.hammerlab.iterator.docs.markdown.Inline.{ A, Img }
import org.hammerlab.iterator.docs.markdown.Opt
import org.hammerlab.iterator.docs.markdown.Opt.Non
import org.hammerlab.iterator.docs.{ URL, symbol }

import scala.scalajs.js

trait badge
  extends symbol {

  implicit def urlToJS(url: URL): js.Any = url.value
  implicit val urlValue: ValueType[URL, String] = ValueType.byImplicit

  def badge(url: URL,
            alttext: String,
            image: URL) =
    Img(
      src = image,
      href = url,
      alt = alttext,
      clz = 'badge
    )

  def travis()(implicit gh: GitHub) = {
    val domain = URL("https://travis-ci.org")
    val base = domain / gh.user / gh.repo

    badge(
      base,
      "Build Status",
      URL(s"$base.svg?branch=master")
    )
  }

  object coveralls {
    val domain = URL("https://coveralls.io")
    def apply()(implicit gh: GitHub) = {
      import gh._
      badge(
        domain/'github/user/repo,
        "Coverage Status",
        domain/'repos/'github/user/repo/"badge.svg"
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
    val domain = URL("https://github.com")

    def badge(implicit gh: GitHub) = {
      import gh._
      Img(
         src = URL(".") / "github.svg",
         alt = "Github Logo",
        href = domain / user / repo,
         clz = "github-badge"
      )
    }

    // Link to a github issue
    def issue(org: String,
              repo: String,
              issue: Int,
              comment: Opt[Int] = Non) =
      A(
        Seq(Text(s"$org/$repo#$issue")),
        domain / org / repo / 'issues / s"$issue${comment.fold("")(c ⇒ s"#issuecomment-$c")}"
      )
  }
}
