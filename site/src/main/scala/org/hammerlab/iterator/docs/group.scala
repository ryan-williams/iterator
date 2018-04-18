package org.hammerlab.iterator.docs

object group
  extends base {
  val ! =
    pkg(
      'group,

      p"Group runs of elements that satisfy a predicate or equivalence relation:",
      block(
        example(
          Iterator(1, 0, 2, 3, 0, 0, 4, 5, 6).groupRuns((l, r) ⇒ l < r),
          Iterator(Iterator(1), Iterator(0, 2, 3), Iterator(0), Iterator(0, 4, 5, 6))
        )
      ),

      p"Run-length encode elements:",
      block(
        example(
          List(1, 1, 2, 1, 7, 7, 7).runLengthEncode,
          Iterator(1→2, 2→1, 1→1, 7→3)
        )
      ),

      p"Contiguous weighted sums up to a maximum:",
      block(
        example(
          Iterator(1 to 6: _*).cappedCostGroups(costFn = x⇒x, limit = 10),
          Iterator(Iterator(1, 2, 3, 4), Iterator(5), Iterator(6))
        )
      )
    )
}
