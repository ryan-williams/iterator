package org.hammerlab.iterator.docs.markdown

case class URL(override val toString: String) {
  def / (segment: String): URL = URL(s"$this/$segment")
}
