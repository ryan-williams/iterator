package org.hammerlab.iterator.site.sections

import org.hammerlab.docs.Code.{ Comment, Setup }
import org.hammerlab.iterator.site.base

object range
  extends base {

  import hammerlab.iterator.range._

  val ! =
    pkg(
      c3('sliceOpt),
      p"Select index-ranges from an iterator where the arguments can be options:",
      fence(
        example(
          (0 to 9).sliceOpt(Some(2), Some(5)),
          2 until 5 iterator
        ),
        Comment(
          "One (or neither) of the arguments can be Options",
          ""
        ),
        example(
          (0 to 9).sliceOpt(None, 5),
          0 until 5 iterator
        ),
        example(
          (0 to 9).sliceOpt(2, None),
          2 to 9 iterator
        ),
        example(
          (0 to 9).sliceOpt(2, 5),
          2 until 5 iterator
        )
      ),
      c3('joinRanges),
      p"Left-merge sequences of ${'Range}s, sorted by start-coordinate, based on overlaps.",
      fence(
        Setup("import hammerlab.iterator.range._"),
        example(
          Iterator[Range[Int]](
            ( 0,  2),
            ( 5,  7),
            (10, 20)
          )
          .joinOverlaps(
            Iterator[Range[Int]](
              (1,  2),
              (1,  3),
              (3,  4),
              (4,  8),
              (4, 11)
            )
          ),
          // Each left-hand range is keyed with overlapping right-hand ranges (and their indices)
          Iterator(
            Range( 0,  2) → Vector(Range(1,  2) → 0, Range(1,  3) → 1),
            Range( 5,  7) → Vector(Range(4,  8) → 3, Range(4, 11) → 4),
            Range(10, 20) → Vector(Range(4, 11) → 4)
          )
        )
      )
    )
}
