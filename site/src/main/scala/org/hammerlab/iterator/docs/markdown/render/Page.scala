package org.hammerlab.iterator.docs.markdown.render

import org.hammerlab.iterator.docs.markdown.Elem.Section
import org.hammerlab.iterator.docs.markdown.render.Page.Loc

case class Page(loc: Loc,
                content: Section)

object Page {
  type Loc = String
//  def apply(loc: Loc, content: Section): Page = {
//
//  }
}
