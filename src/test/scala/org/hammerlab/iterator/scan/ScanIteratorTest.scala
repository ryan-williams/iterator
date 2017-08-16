package org.hammerlab.iterator.scan

import cats.implicits.{ catsKernelStdGroupForInt, catsKernelStdMonoidForString }
import org.hammerlab.iterator.scan.ScanIterator._
import org.hammerlab.test.Suite

class ScanIteratorTest
  extends Suite {
  test("scanL ints") {
    (1 to 10)
      .scanL
      .toList should be(
      (1 to 10)
        .scanLeft(0)(_ + _)
    )
  }

  test("scanL strings") {
    "abcde"
      .split("")
      .scanL
      .toList should be(
      "abcde".scanLeft("")(_ + _)
    )
  }

  test("scanR ints") {
    (1 to 10)
      .iterator
      .scanR
      .toList should be(
        (1 to 10)
          .scanRight(0)(_ + _)
      )
  }

  test("scanR strings") {
    "abcde"
      .split("")
      .scanR
      .toList should be(
      "abcde".scanRight("")(_ + _)
    )
  }
}
