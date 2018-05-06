package org.hammerlab.iterator.site

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.hammerlab.iterator.docs.Opt
import org.hammerlab.iterator.docs.Opt.Non
import org.hammerlab.iterator.docs.markdown.fqn.Id
import org.hammerlab.iterator.docs.markdown.fqn.tree._
import org.hammerlab.iterator.docs.markdown.render
import org.hammerlab.iterator.docs.markdown.render.react.{ idAttr, inlines }

object Menu {

  case class Children(open: Boolean, items: Seq[Item])
  object Children {
    def apply(items: Seq[Item]): Option[Children] =
      if (items.isEmpty)
        None
      else
        Some(
          Children(open = true, items)
        )
  }
  case class Item(id: Id, display: Seq[Inline], children: Option[Children])
  object Item {
    def apply(id: Id, display: Seq[Inline], children: Item*): Item =
      Item(
        id,
        display,
        Children(children)
      )
    def apply(section: Section, maxDepth: Opt[Int] = Non, depth: Int = 1): Item =
      Item(
        section.id,
        section.title,
        (
          if (maxDepth.exists(_ <= depth))
            Nil
          else
            section
              .elems
              .collect {
                case s: Section ⇒ apply(s, maxDepth, depth + 1)
              }
        ): _*
      )
  }

  val component =
    ScalaComponent
      .builder[Seq[Item]]("Menu")
      .render_P {
        items ⇒

          def item(it: Item): VdomNode = {
            val Item(id, display, children) = it
            <.li(
              ^.key := id,
              ^.`class` := "nav-item",
              <.a(
                ^.href := s"#$id",
                ^.`class` := "nav-link",
                display
              ),
              children
                .collect {
                  case Children(true, children) ⇒
                    <.ul(
                      ^.`class` := "nav-indent",
                      children.toVdomArray(item)
                    )
                }
                .getOrElse(Nil.toVdomArray)
            )
          }

          <.ul(
            ^.`class` := "nav nav-pills",
            items
              .toVdomArray { item }
          )
      }
      .build

  def apply(items: Seq[Item]) = component(items)
}
