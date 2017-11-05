package org.hammerlab.iterator

import scala.collection.mutable
import scala.reflect.ClassTag

trait Bulk {
  /**
   * Some smarter bulk operations on [[BufferedIterator]]s, consuming exactly the elements necessary and not more
   */
  implicit class BufferedBulkOps[T](it: BufferedIterator[T]) extends Serializable {
    def takewhile(fn: T ⇒ Boolean): SimpleBufferedIterator[T] =
      new SimpleBufferedIterator[T] {
        override protected def _advance: Option[T] =
          if (it.hasNext && fn(it.head))
            Some(it.next)
          else
            None
      }

    def dropwhile(fn: T ⇒ Boolean): Unit =
      while (it.hasNext && fn(it.head))
        it.next

    def collectwhile[U](pf: PartialFunction[T, U]): BufferedIterator[U] =
      new SimpleBufferedIterator[U] {
        override protected def _advance: Option[U] =
          if (it.hasNext && pf.isDefinedAt(it.head))
            Some(
              pf(
                it.next
              )
            )
          else
            None
      }
  }

  /**
   * Exposes `.takeEager` on iterators, which is identical to `.take.toArray`, but always consumes the taken elements from
   * the original iterator.
   *
   * Some stdlib iterators backed by [[Seq]]s have `.take` implementations that create a view over the "taken" elements,
   * so that the original iterator is not consumed when the taken/prefix iterator is materialized; this homogenizes that
   * behavior.
   */
  implicit class TakeEagerIterator[T: ClassTag](it: Iterator[T]) {
    def takeEager(n: Int): Array[T] = {
      val builder = mutable.ArrayBuilder.make[T]()
      for {
        _ ← 0 until n
        if it.hasNext
      } {
        builder += it.next
      }
      builder.result
    }
  }
}
