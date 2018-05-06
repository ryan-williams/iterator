package org.hammerlab.iterator.docs

import java.io.ByteArrayInputStream

import org.hammerlab.docs.block

object end
  extends base {

  @block
  private trait setup {
    val stream = new ByteArrayInputStream("scala".getBytes)
    val bytes = Array.fill(stream.available())(0.toByte)
    stream.read(bytes)
  }

  val ! =
    new setup {
      val ! =
        pkg(
          c3('finish),
          p"Run a closure when an iterator is finished traversing:",
          fence(
            setup,
            example(
              bytes
                .filter(_ == 'a')
                .finish {
                  println("closing!")
                  stream.close()
                }
                .size,

              // prints "closing!" and closes `stream` after traversal is finished
              2
            )
          ),
          c3('dropRight),
          p"Drop ${'k} elements from the end of an iterator in ${"O(k)"} space:",
          fence(
            example(
              Iterator(1 to 10: _*).dropright(4),
              Iterator(1, 2, 3, 4, 5, 6)
            )
          )
        )
    }.!
}
