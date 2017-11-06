package org.hammerlab.iterator.end

import hammerlab.iterator._
import org.hammerlab.test.Suite

class DropRightTest extends Suite {
  test("drop-ones") {
    for { n ← 0 to 10 } {
      (1 to n).dropright(1).toList should be(1 until n)
    }
  }

  test("drop-twos") {
    for { n ← 0 to 10 } {
      (1 to n).dropright(2).toList should be(1 to (n - 2))
    }
  }

  test("drop-zero") {
    for { n ← 0 to 10 } {
      (1 to n).dropright(0).toList should be(1 to n)
    }
  }

  test("except on invalid arg") {
    intercept[IllegalArgumentException] {
      (1 to 10).dropright(-1)
    }
  }
}
