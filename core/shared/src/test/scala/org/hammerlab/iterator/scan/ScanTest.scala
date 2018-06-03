package org.hammerlab.iterator.scan

import cats.implicits.catsKernelStdMonoidForString
import hammerlab.iterator.scan._
import hammerlab.monoid._
import org.hammerlab.Suite
import org.hammerlab.test.Cmp

abstract class ScanTest
  extends Suite {

  // Test-case inputs
  val intsInput = 1 to 10
  val stringsInput = "abcde".split("")
  val foosInput =
    Iterator(
      Foo(111, "aaa"),
      Foo(222, "bbb"),
      Foo(333, "ccc")
    )

  // Expected outputs
  def ints: Seq[Int]
  def strings: Seq[String]
  def foos: Seq[Foo]

  def check[T: Cmp](it: Iterator[T], expected: Seq[T]): Unit =
    ==(it.toList, expected)
}

abstract class ScanLeftTest(includeCurrentValue: Boolean)
  extends ScanTest {

  test("scanL ints") {
    check(
      intsInput.scanL(includeCurrentValue),
      ints
    )
  }

  test("scanL strings") {
    check(
      stringsInput.scanL(includeCurrentValue),
      strings
    )
  }

  test("scanL case class") {
    check(
      foosInput.scanL(includeCurrentValue),
      foos
    )
  }
}

abstract class ScanRightTest(includeCurrentValue: Boolean)
  extends ScanTest {

  test("scanR ints") {
    check(
      intsInput.scanR(includeCurrentValue),
      ints
    )
  }

  test("scanR strings") {
    check(
      stringsInput.scanR(includeCurrentValue),
      strings
    )
  }

  test("scanR case class") {
    check(
      foosInput.scanR(includeCurrentValue),
      foos
    )
  }
}

class ScanLeftInclusiveTest
  extends ScanLeftTest(true) {

  override val ints: Seq[Int] =
    Seq(
       1,
       3,
       6,
      10,
      15,
      21,
      28,
      36,
      45,
      55
    )

  override val strings: Seq[String] =
    Seq(
      "a",
      "ab",
      "abc",
      "abcd",
      "abcde"
    )

  override val foos: Seq[Foo] =
    Seq(
      Foo(111, "aaa"),
      Foo(333, "aaabbb"),
      Foo(666, "aaabbbccc")
    )
}

class ScanLeftExclusiveTest
  extends ScanLeftTest(false) {

  override val ints: Seq[Int] =
    Seq(
       0,
       1,
       3,
       6,
      10,
      15,
      21,
      28,
      36,
      45
    )

  override val strings: Seq[String] =
    Seq(
      "",
      "a",
      "ab",
      "abc",
      "abcd"
    )

  override val foos: Seq[Foo] =
    Seq(
      Foo(  0,       ""),
      Foo(111,    "aaa"),
      Foo(333, "aaabbb")
    )
}

class ScanRightInclusiveTest
  extends ScanRightTest(true) {

  override val ints: Seq[Int] =
    Seq(
      55,
      54,
      52,
      49,
      45,
      40,
      34,
      27,
      19,
      10
    )

  override val strings: Seq[String] =
    Seq(
      "abcde",
      "bcde",
      "cde",
      "de",
      "e"
    )

  override val foos: Seq[Foo] =
    Seq(
      Foo(666, "aaabbbccc"),
      Foo(555, "bbbccc"),
      Foo(333, "ccc")
    )
}

class ScanRightExclusiveTest
  extends ScanRightTest(false) {

  override val ints: Seq[Int] =
    Seq(
      54,
      52,
      49,
      45,
      40,
      34,
      27,
      19,
      10,
      0
    )

  override val strings: Seq[String] =
    Seq(
      "bcde",
      "cde",
      "de",
      "e",
      ""
    )

  override val foos: Seq[Foo] =
    Seq(
      Foo(555, "bbbccc"),
      Foo(333,    "ccc"),
      Foo(  0,       "")
    )
}

case class Foo(n: Int, s: String)
