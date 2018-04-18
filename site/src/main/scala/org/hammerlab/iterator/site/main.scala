package org.hammerlab.iterator.site

import org.hammerlab.iterator._

import scalatags.Text.all._

object main {
  val html =
    div(
      count.docs.body,
      either.docs.body
    )
}
