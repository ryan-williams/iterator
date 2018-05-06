package org.hammerlab.iterator.site.sections

import org.hammerlab.iterator.site.base

object group
  extends base {
  val ! =
    pkg(
      c3('groupRuns),
      p"Group runs of elements that satisfy a predicate or equivalence relation:",
      fence(
        example(
          Iterator(1, 0, 2, 3, 0, 0, 4, 5, 6).groupRuns(_ < _),
          Iterator(Iterator(1), Iterator(0, 2, 3), Iterator(0), Iterator(0, 4, 5, 6))
        )
      ),

      c3('runLengthEncode),
      p"Run-length encode elements:",
      fence(
        example(
          List(1, 1, 2, 1, 7, 7, 7).runLengthEncode,
          Iterator(1→2, 2→1, 1→1, 7→3)
        )
      ),

      c3('cappedCostGroups),
      p"Contiguous weighted sums up to a maximum:",
      fence(
        example(
          Iterator(1 to 6: _*).cappedCostGroups(costFn = x⇒x, limit = 10),
          Iterator(Iterator(1, 2, 3, 4), Iterator(5), Iterator(6))
        )
      )
    )
}
