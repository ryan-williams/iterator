package org.hammerlab.docs.markdown.fqn

import org.hammerlab.docs.Opt.Non
import org.hammerlab.docs.{ Opt, markdown }
import markdown.dsl

import scala.collection.mutable

object tree
  extends markdown.tree.elem[Id] {

  override type CanLinkTo = Section

  object MkSection {

    type SectionMap = mutable.Map[dsl.Section, Section]

    def apply(
      section: dsl.Section
    )(
      implicit
      parent: Opt[Id] = Non,
      used: mutable.Set[Id] = mutable.Set(),
      seen: mutable.Set[dsl.Section] = mutable.Set(),
      map: SectionMap = mutable.Map()
    ):
      Section =
      map.getOrElseUpdate(
        section,
        {

          if (seen(section))
            throw new IllegalStateException(
              s"Link cycle: ${section.id}, (${seen.map(_.id).mkString(",")})"
            )

          seen += section

          val id =
            parent
              .fold[Id] {
                Id(Seq(section.id))
              } {
                _ + section.id
              }

          implicit val prefixed =
            id
              .suffixes
              .find(!used(_)) match {
                case Some(id) ⇒ id
                case None ⇒
                  throw new IllegalStateException(
                    s"No valid suffixes found for $id: ${id.suffixes.mkString(",")} ${used.mkString(",")}"
                  )
              }

          used += prefixed

          implicit def convTarget(in: dsl.Target): Target =
            in match {
              case L(url) ⇒ L(url)
              case R(section) ⇒
                R(
                  if (!seen(section))
                    apply(section)
                  else
                    map(section)
                )
            }

          implicit def inline(in: dsl.Inline): Inline =
            in match {
              case L(l) ⇒ L(l)
              case R(dsl.Inline.A(children, target, alt, clz)) ⇒
                   R(    Inline.A(children, target, alt, clz))
            }
          implicit def inlines(in: Seq[dsl.Inline]): Seq[Inline] = in.map(inline)

          implicit def listitem(item: dsl.LI): LI = LI(item.title, item.elems)
          implicit def listitems(items: Seq[dsl.LI]): Seq[LI] = items.map(listitem)

          implicit def nonsections(in: Seq[dsl.NonSection]): Seq[NonSection] = in.map(nonsection)
          implicit def nonsection(in: dsl.NonSection): NonSection =
            in match {
              case dsl.P(elems) ⇒ P(elems)
              case dsl.Fence(lines) ⇒ Fence(lines)
              case dsl.UL(items) ⇒ UL(items)
              case dsl.OL(items) ⇒ OL(items)
            }

          val children =
            section
              .elems
              .map {
                case section: dsl.Section ⇒ apply(section)
                case s: dsl.NonSection ⇒ s: NonSection
              }

          Section(
            section.title,
            prefixed,
            children
          )
        }
      )
  }
}
