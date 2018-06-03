package org.hammerlab.iterator.range

import hammerlab.iterator.macros.IteratorOps

/**
 * Given an [[Iterator]] of [[Int]]s, collapse contiguous "ranges" of integers that are each 1 greater than their
 * predecessor.
 *
 * For example, given an input [2, 3, 1, 4, 5, 6, 5, 6, 8], this would emit [[Range]]s (in half-open notation): [2, 4),
 * [1, 2), [4, 7), [5, 7), [8, 9).
 *
 * See `ContiguousTest` for more examples.
 */
@IteratorOps
class Contiguous(it: Iterator[Int]) {
  def contiguousRanges: Iterator[scala.Range] =
    new Iterator[scala.Range] {
      var anchor = -1

      override def hasNext: Boolean = it.hasNext || anchor >= 0

      override def next(): scala.Range = {
        if (anchor < 0) anchor = it.next()

        var start = anchor
        var end = anchor + 1
        var continue = true
        while (it.hasNext && continue) {
          val e = it.next()
          if (e == end)
            end += 1
          else {
            continue = false
            anchor = e
          }
        }

        if (continue)
          anchor = -1

        start until end
      }
    }
}
