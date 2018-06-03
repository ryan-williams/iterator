package org.hammerlab.iterator.end

import org.hammerlab.Suite

import hammerlab.iterator.end._

import scala.Array.fill

class ExpandLastElementTest
  extends Suite {

  test("empty") {
    ==(
      Iterator[Int]()
        .expandLastElement(
          (x: Int) ⇒
            fill(10)(x).iterator
        )
        .toList,
      Nil
    )
  }

  test("one element") {
    ==(
      Iterator(10)
        .expandLastElement(
          (last: Int) ⇒
            ((last * 2) to (last * 5) by 10)
              .iterator
        )
        .toList,
      List(
        10,
        20,
        30,
        40,
        50
      )
    )
  }

  test("two elements") {
    ==(
      Iterator(10, 20)
        .expandLastElement(
          (last: Int) ⇒
            ((last * 2) to (last * 5) by 30)
              .iterator
        )
        .toList,
      List(
         10,
         20,
         40,
         70,
        100
      )
    )
  }
}
