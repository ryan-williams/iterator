package org.hammerlab.docs.markdown.util

import org.hammerlab.docs.markdown.tree.NonLink.Text

trait symbol {
  implicit def symbolToString(s: Symbol): String = s.toString.drop(1)
  implicit def symbolToTextModifier(s: Symbol): Text = Text(s.toString.drop(1))
}
