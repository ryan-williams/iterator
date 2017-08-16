package org.hammerlab.iterator.scan

import cats.Monoid

case class ScanIterator[T](it: Iterator[T]) {
  def scanL(implicit m: Monoid[T]): Iterator[T] = it.scanLeft(m.empty)(m.combine)
  def scanR(implicit m: Monoid[T]): Iterator[T] = it.scanRight(m.empty)(m.combine)
}

object ScanIterator {
  implicit def makeScanIterator[T](it: Iterator[T]): ScanIterator[T] = ScanIterator(it)
  implicit def makeScanIterator[T](it: Iterable[T]): ScanIterator[T] = ScanIterator(it.iterator)
  implicit def makeScanIterator[T](it: Array[T]): ScanIterator[T] = ScanIterator(it.iterator)
}
