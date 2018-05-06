package org.hammerlab.docs.markdown

import org.hammerlab.docs.markdown.util.symbol

case class Id(value: String)
object Id
  extends symbol {
  implicit def str(value: String): Id = Id(value)
  implicit def sym(value: Symbol): Id = Id(value)
}
