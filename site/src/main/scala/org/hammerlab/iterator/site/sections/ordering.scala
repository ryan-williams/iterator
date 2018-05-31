package org.hammerlab.iterator.site.sections

import hammerlab.iterator.View
import org.hammerlab.iterator.site.base
import hammerlab.docs.block

object ordering
  extends base {

  @block
  trait view {
    // Rank a (Symbol,Int) pair using its Int value
    implicit val _ = View[(Symbol, Int), Int](_._2)
  }

  val ! =
    new either.setup
      with view {
      val ! =
        pkg(
          p"A variety of merge operations are available for sequences that are mutually ordered (possibly with respect to some 3rd type that each of their elements can be converted to).",
          c3('eitherMerge),
          p"Merge two ordered sequences using ${'Either}s to preserve provenance (or handle the case that the sequences' elements are not the same type):",
          fence(
            example(
              Seq(1, 3, 4).eitherMerge(Seq(2, 3, 5, 6)),
              Iterator(L(1), R(2), L(3), R(3), L(4), R(5), R(6))
            )
          ),
          c3('leftMerge),
          p"Collecting right-side elements for each left-side element:",
          fence(
            example(
              Seq(1, 3, 4).leftMerge(Seq(2, 3, 5, 6)),
              Iterator((1, Iterator(2)), (3, Iterator(3)), (4, Iterator(5, 6)))
            )
          ),
          c3('merge),
          fence(
            example(
              Seq(1, 3, 4).merge(Seq(2, 3, 5, 6)),
              Iterator(1, 2, 3, 3, 4, 5, 6)
            )
          ),
          h(
            t"Merging with a 3rd type",
            p"Instances of the ${'View} type-class let merges use a type other than that of the elements being merged:",
            fence(
              view,
              example(
                Seq('a → 1, 'b → 3).merge(Seq('c→2)),
                Iterator('a → 1, 'c → 2, 'b → 3)
              ),
              example(
                Seq('a → 1, 'b → 3).eitherMerge(Seq(2)),
                Iterator(L('a → 1), R(2), L('b → 3))
              )
            )
          )
        )
    }.!
}
