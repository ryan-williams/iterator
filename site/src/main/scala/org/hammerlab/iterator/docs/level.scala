package org.hammerlab.iterator.docs

import hammerlab.iterator._
import hammerlab.option._
import org.hammerlab.docs.block
import org.hammerlab.test.Cmp

object level
  extends section {

  val ! = make.!

  @block
  trait setup {
    val it1 = Iterator(  1,   2)
    val it2 = Iterator(  3,   4)
    val it  = Iterator(it1, it2).level
  }

  object make
    extends base
       with setup {

    implicit val cmpIt: Cmp[Iterator[Int]] = Cmp((l, r) ⇒ (l != r) ? "!=")

    val ! =
      pkg(
        p"Flatten a nested iterator but retain access to a cursor into unflattened version:",
        fence(
          setup,
          example(it.cur.get,  it1),
          example(it.next   ,    1),
          example(it.cur.get,  it1),
          example(it.next   ,    2),
          example(it.cur.get,  it2),
          example(it.next   ,    3),
          example(it.cur.get,  it2),
          example(it.next   ,    4),
          example(it.cur    , None)
        )
      )
  }
}
