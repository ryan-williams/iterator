package org.hammerlab.iterator.site

import org.hammerlab.iterator.docs.{ URL, attr_dsl, module, symbol }

package object badge
  extends attr_dsl
     with symbol
     with URL.utils {

  self: module ⇒
  import b.all._

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

    def link(children: Modifier*)(implicit gh: GitHub) = {
      import gh._
      a(
        href := s"$domain/$user/$repo",
        children,
        badge
      )
    }

    // Link to a github issue
    def issue(org: String, repo: String, issue: Int, comment: Int): Modifier = this.issue(org, repo, issue, Some(comment))
    def issue(org: String, repo: String, issue: Int, comment: Option[Int] = None): Modifier =
      a(
        href := s"$domain/$org/$repo/issues/$issue${comment.fold("")(c ⇒ s"#issuecomment-$c")}",
        s"$org/$repo#$issue"
      )
  }
}
