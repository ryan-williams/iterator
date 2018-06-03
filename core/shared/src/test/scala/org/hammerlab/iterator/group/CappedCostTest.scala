package org.hammerlab.iterator.group

import hammerlab.iterator._
import org.hammerlab.Suite

trait CappedCostTest
  extends Suite {

  def check(it: Seq[Int],
            limit: Int,
            costFn: Int ⇒ Int = x ⇒ x)(
      expected: Seq[Int]*
  )(
      implicit
      strategy: ElementTooCostlyStrategy
  ): Unit =
    ==(
      it
        .iterator
        .cappedCostGroups(
          costFn,
          limit
        )
        .map(_.toList)
        .toList,
      expected
    )
}

class DiscardElementTooCostlyTest
  extends CappedCostTest {

  import ElementTooCostlyStrategy.Discard

  test("empty") {
    check(
      Nil,
      1
    )()
  }

  test("1-10 9") {
    check(
      1 to 10,
      9
    )(
      Seq(1, 2, 3),
      Seq(4, 5),
      Seq(6),
      Seq(7),
      Seq(8),
      Seq(9)
    )
  }

  test("1-10 1") {
    check(
      1 to 10,
      1
    )(
      Seq(1)
    )
  }

  test("random") {
    check(
      Seq(11, 1, 11, 2, 8, 10, 11, 1),
      10
    )(
      Seq(1),
      Seq(2, 8),
      Seq(10),
      Seq(1)
    )
  }

  test("consecutive discards") {
    check(
      Seq(11, 1, 11, 12, 2, 8, 10, 11, 1),
      10
    )(
      Seq(1),
      Seq(2, 8),
      Seq(10),
      Seq(1)
    )
  }
}

class EmitAloneElementTooCostlyTest
  extends CappedCostTest {

  import ElementTooCostlyStrategy.EmitAlone

  test("empty") {
    check(
      Nil,
      1
    )()
  }

  test("1-10 1") {
    check(
      1 to 10,
      1
    )(
      (1 to 10).map(Seq(_)): _*
    )
  }

  test("random") {
    check(
      Seq(11, 1, 11, 12, 2, 8, 10, 11, 1),
      10
    )(
      Seq(11),
      Seq(1),
      Seq(11),
      Seq(12),
      Seq(2, 8),
      Seq(10),
      Seq(11),
      Seq(1)
    )
  }
}

class ThrowOnTooCostlyElementTest
  extends CappedCostTest {
  import ElementTooCostlyStrategy.Throw

  test("empty") {
    check(
      Nil,
      1
    )()
  }

  test("1-10 10") {
    check(
      1 to 10,
      10
    )(
      (1 to 4) ::
      (5 to 10)
      .map(Seq(_))
      .toList: _*
    )
  }

  test("1-10 9") {
    ==(
      intercept[ElementTooCostlyException[Int, Int]] {
        check(
          1 to 10,
          9
        )()
      },
      ElementTooCostlyException(
        10, 9, 10
      )
    )
  }

  test("random") {
    ==(
      intercept[ElementTooCostlyException[Int, Int]] {
        check(
          Seq(11, 1, 11, 12, 2, 8, 10, 11, 1),
          10
        )()
      },
      ElementTooCostlyException(
        11,
        10,
        11
      )
    )
  }
}
