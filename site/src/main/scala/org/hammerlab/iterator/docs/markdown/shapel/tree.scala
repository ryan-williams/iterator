package org.hammerlab.iterator.docs.markdown.shapel

import hammerlab.lines.Lines
import org.hammerlab.iterator.docs.Opt
import org.hammerlab.iterator.docs.markdown.Clz
import shapeless.{ :+:, ::, CNil, HNil }

trait tree {
  type Inline = Link :+: NonLink :+: CNil
  type Link =
    Seq[NonLink] ::
      Target ::
      Opt[String] ::
      Clz :: HNil

  type Section
  //type Section = Seq[Inline] :: Id :: Seq[Elem] :: HNil

  type Elem = Section :+: NonSection :+: CNil

  type Id
  type Target

  type NonSection =
        P :+:
    Fence :+:
       UL :+:
       OL :+:
    Image :+:
     CNil

  type P = Seq[Inline]
  type Fence = Lines
  type UL = Seq[LI]
  type OL = Seq[LI]
  type Image = Opt[Target] :: NonLink.Img :: HNil

  type LI
//  type LI = Inline :: Seq[NonSection] :: HNil
}
