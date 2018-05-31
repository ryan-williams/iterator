package org.hammerlab.iterator.site

import japgolly.scalajs.react.vdom.Attr.ValueType
import org.hammerlab.docs.markdown._
import tree._
import NonLink._
import dsl._
import org.hammerlab.docs.{ Opt, markdown }
import org.hammerlab.docs.Opt.Non
import org.hammerlab.docs.markdown.util.{ Clz, URL }

import scala.scalajs.js

trait badge
  extends dsl {

  implicit def urlToJS(url: URL): js.Any = url.toString
  implicit val urlValue: ValueType[URL, String] = ValueType.byImplicit

  def badge(url: URL,
            alttext: String,
            image: URL) =
    a(
      NonLink.Img(
        src = image,
        alt = alttext
      ),
      target = url,
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

    def user(implicit gh: GitHub) = gh.user
    def repo(implicit gh: GitHub) = gh.repo

    def title(implicit gh: GitHub) =
      github.link(
        n"$user/$repo ${github.badge}"
      )

    def link(elems: Seq[NonLink],
             alt: Opt[String] = Non,
             clz: Clz = Nil)(
        implicit gh: GitHub
    ) =
      a(
        elems,
        domain / user / repo,
        alt,
        clz
      )

    def badge(implicit gh: GitHub) =
      NonLink.Img(
        src = URL(".") / "github.svg",
        alt = "Github Logo",
        clz = "github-badge"
      )

    // Link to a github issue
    def issue(org: String,
              repo: String,
              issue: Int,
              comment: Opt[Int] = Non) =
      a(
        Text(s"$org/$repo#$issue"),
        domain / org / repo / 'issues / s"$issue${comment.fold("")(c ⇒ s"#issuecomment-$c")}"
      )
  }
}
