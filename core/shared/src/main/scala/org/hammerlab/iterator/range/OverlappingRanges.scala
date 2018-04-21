package org.hammerlab.iterator.range

import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorOps
import org.hammerlab.iterator.util.SimpleIterator

import scala.collection.mutable

/**
 * Given two sequences of [[Range]]s, each ordered by increasing [[Range.start start]], emit a tuple for each left-side
 * range that contains all overlapping right-side ranges (as well as their original indices in the right-side sequence).
 */
@IteratorOps
class OverlappingRanges[T](it: Iterator[Range[T]]) {

  type RangeT = (T, Option[T])

  def joinOverlaps(other: Iterable[Range[T]])(
      implicit
      ord: Ordering[T]
  ): Iterator[(Range[T], Vector[(Range[T], Int)])] =
    joinOverlaps(other.iterator.buffered)

  def joinOverlaps(other: Iterator[Range[T]])(
      implicit
      ord: Ordering[T]
  ): Iterator[(Range[T], Vector[(Range[T], Int)])] = {
    val ≤ = ord.lteq _

    implicit val orderZippedRangeByEndOpt: Ordering[(Range[T], Int)] =
      Ordering
        .by[(Range[T], Int), Option[T]](_._1.endOpt)
        .reverse

    val queue = mutable.PriorityQueue[(Range[T], Int)]()

    val zippedOther =
      other
        .zipWithIndex
        .buffered

    new SimpleIterator[(Range[T], Vector[(Range[T], Int)])] {
      override protected def _advance: Option[(Range[T], Vector[(Range[T], Int)])] =
        it
          .nextOption
          .map {
            elem ⇒
              while (
                queue
                  .headOption
                  .exists {
                    case (range, _) ⇒
                      !range.∩(elem)
                  }
              ) {
                queue.dequeue()
              }

              while (
                zippedOther
                  .headOption
                  .flatMap(_._1.endOpt)
                  .exists(≤(_, elem.start))
              ) {
                zippedOther.next
              }

              while (
                zippedOther
                  .headOption
                  .exists(_._1.∩(elem))
              ) {
                queue.enqueue(zippedOther.next)
              }

              elem →
                queue
                  .toVector
                  .sortBy(_._2)
          }
    }
  }
}
