package org.hammerlab.iterator.site

import org.hammerlab.iterator.docs.{ URL, attr_dsl, symbol }

import scalatags.Text.all._

package object badge
  extends attr_dsl
     with symbol
     with URL.utils {
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
        href := s"https://github.com/$user/$repo",
        children,
        badge
      )
    }
  }
}
