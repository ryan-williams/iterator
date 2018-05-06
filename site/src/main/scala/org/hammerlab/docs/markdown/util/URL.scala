package org.hammerlab.docs.markdown.util

case class URL(override val toString: String) {
  def / (segment: String): URL = URL(s"$this/$segment")
}
