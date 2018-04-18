package org.hammerlab.iterator.docs

import java.io.ByteArrayInputStream

object end
  extends base {
    val ! =
      pkg(
        'end,
        p"${'finish}: run a closure when the iterator is finished traversing:",
        block(
          example(
            {
              val source = new ByteArrayInputStream("scala".getBytes)
              val bytes = Array.fill(source.available())(0.toByte)
              source.read(bytes)
              bytes
                .filter(_ == 'a')
                .finish {
                  println("closing!")
                  source.close()
                }
                .size
            },
            // prints "closing!" and closes `source` after traversal is finished
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
