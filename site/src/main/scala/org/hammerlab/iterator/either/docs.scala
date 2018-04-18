package org.hammerlab.iterator.either

import org.hammerlab.iterator
import org.hammerlab.docs.Macros.example
import cats.implicits._
import hammerlab.cmp.first._
import org.hammerlab.test.Cmp

trait docs
  extends iterator.docs {

  import hammerlab.iterator._

  def L[T](t: T) = Left(t)
  def R[T](t: T) = Right(t)

  implicitly[Cmp[Iterator[Int]]]
  implicitly[Cmp[(Int, Int)]]

  val either =
    pkg(
      'either,
      code(
        example(
          Iterator(R('a), R('b), L(4)).findLeft,
          Some(4)
        ),
        example(
          Iterator(
            R('a),
            L( 1),
            R('b),
            R('c),
            L( 2),
            L( 3),
            R('d)
          )
          .groupByLeft
          .mapValues(_.mkString),
          Iterator((1,"'b'c"), (2,""), (3,"'d"))
        )
      )
    )
}

object docs extends docs {
  val body = either
}
