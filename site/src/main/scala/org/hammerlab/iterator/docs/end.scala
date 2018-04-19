package org.hammerlab.iterator.docs

import org.hammerlab.docs
import java.io.ByteArrayInputStream

object end {
  @docs.setup
  trait setup {
    val stream = new ByteArrayInputStream("scala".getBytes)
    val bytes = Array.fill(stream.available())(0.toByte)
    stream.read(bytes)
  }

  object make
    extends base
       with setup {
    def apply() =
      pkg(
        'end,
        p"${'finish}: run a closure when the iterator is finished traversing:",
        block(
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
        p"${'dropRight}: drop ${'k} elements from the end of an iterator in ${"O(k)"} space:",
        block(
          example(
            Iterator(1 to 10: _*).dropright(4),
            Iterator(1, 2, 3, 4, 5, 6)
          )
        )
      )
  }

  val ! = make()
}
