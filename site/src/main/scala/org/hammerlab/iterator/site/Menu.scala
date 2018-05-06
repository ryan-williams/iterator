package org.hammerlab.iterator.site

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.hammerlab.iterator.docs.markdown.dsl
//import org.hammerlab.iterator.docs.Tree

object Menu {

  case class Children(open: Boolean, items: Seq[Item])
  object Children {
    def apply(items: Seq[Item]): Option[Children] =
      if (items.isEmpty)
        None
      else
        Some(
          Children(open = false, items)
        )
  }
  case class Item(id: String, display: VdomNode, children: Option[Children] = None)
  object Item {
    def apply(id: String, children: Item*): Item = apply(id, <.span(id), children: _*)
    def apply(id: String, title: VdomNode, children: Item*): Item =
      Item(
        id,
        title,
        Children(children)
      )
    def apply(section: dsl.Section): Item =
      Item(
        section.id.value,
        ???, //section.title,
        section
          .elems
          .collect {
            case s: dsl.Section ⇒ apply(s)
          }: _*
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
              ^.`class` := "nav-link",
              <.a(
                ^.href := s"#$id",
                display
              ),
              children
                .collect {
                  case Children(true, children) ⇒
                    <.ul(
                      children.map(item): _*
                    )
                }
                .getOrElse(Nil.toVdomArray)
            )
          }

          <.nav(
            ^.id := "nav-container",
            ^.`class` := "navbar nav-pills",
            items
              .toVdomArray {
                item
              }
          )
      }
      .build

  def apply(items: Seq[Item]) = component(items)
}
