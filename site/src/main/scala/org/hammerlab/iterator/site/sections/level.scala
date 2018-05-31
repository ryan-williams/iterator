package org.hammerlab.iterator.site.sections

import hammerlab.iterator._
import hammerlab.option._
import org.hammerlab.iterator.site.base
import org.hammerlab.test.Cmp
import hammerlab.docs.block

object level
  extends base {

  @block
  trait setup {
    val it1 = Iterator(  1,   2)
    val it2 = Iterator(  3,   4)
    val it  = Iterator(it1, it2).level
  }

  val ! =
    new setup {
      /**
       * "override" [[hammerlab.cmp.first.iteratorsCanEq]] to use reference equality; important so the examples below
       * don't each completely drain the example-iterator, so that the examples actually verify that the `cur` pointer
       * points to the correct sub-iterator at each step.
       */
      implicit val iteratorsCanEq: Cmp[Iterator[Int]] = Cmp((l, r) ⇒ (l != r) ? "!=")
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
    }.!
}
