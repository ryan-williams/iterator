package org.hammerlab.iterator.sliding

import hammerlab.iterator.sliding._
import org.hammerlab.Suite

class Sliding2PrevTest extends Suite {
  test("empty") {
    ==(Iterator().sliding2Prev.toList, Nil)
  }

  test("one") {
    ==(Iterator(1).sliding2Prev.toSeq, Seq(None → 1))
  }

  test("two") {
    ==(Iterator(1, 2).sliding2Prev.toSeq, Seq(None → 1, Some(1) → 2))
  }

  test("three") {
    ==(Iterator(1, 2, 3).sliding2Prev.toSeq, Seq(None → 1, Some(1) → 2, Some(2) → 3))
  }
}
