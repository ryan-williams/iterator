package org.hammerlab.iterator.docs

import org.hammerlab.docs.block
import org.hammerlab.iterator.docs.either.setup

trait either
  extends base {

  sections +=
    new setup {
      val ! =
        pkg(
          fence(
            setup,
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
    }.!
}

object either {
  @block
  trait setup {
    def L[T](t: T) =  Left(t)
    def R[T](t: T) = Right(t)
  }
}
