package org.hammerlab.iterator.docs

trait sliding
  extends base {
  sections +=
    pkg(
      p"Windows of size 2, including an optional next or previous element:",
      fence(
        example(
          Seq(1, 2, 3).sliding2,
          Iterator(1 → 2, 2 → 3)
        ),
        example(
          Seq(1, 2, 3).sliding2Opt,
          Iterator(1 → Some(2), 2 → Some(3), 3 → None)
        ),
        example(
          Seq(1, 2, 3).sliding2Prev,
          Iterator(None → 1, Some(1) → 2, Some(2) → 3)
        )
      ),
      p"Windows of size 3, including 2 succeeding elements, one successor and one predecessor, or full tuples only:",
      fence(
        example(
          Seq(1, 2, 3, 4).sliding3,
          Iterator((1, 2, 3), (2, 3, 4))
        ),
        example(
          Seq(1, 2, 3, 4).sliding3Opt,
          Iterator((None, 1, Some(2)), (Some(1), 2, Some(3)), (Some(2), 3, Some(4)), (Some(3), 4, None))
        ),
        example(
          Seq(1, 2, 3, 4).sliding3NextOpts,
          Iterator((1, Some(2), Some(3)), (2, Some(3), Some(4)), (3, Some(4), None), (4, None, None))
        )
      ),
      p"Windows of arbitrary size, output having same number of elems as input:",
      fence(
        example(
          Seq(1, 2, 3, 4, 5).slide(4),
          Iterator(Seq(1, 2, 3, 4), Seq(2, 3, 4, 5), Seq(3, 4, 5), Seq(4, 5), Seq(5))
        )
      )
    )
}
