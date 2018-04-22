package org.hammerlab.iterator.docs

object sample
  extends base {
  scala.util.Random.setSeed(123)
  val ! =
    h(
      p"Resevoir-sample:",
      fence(
        example(
          Iterator(1 to 100: _*).sample(5),
          Array(15, 33, 40, 75, 83)
        )
      )
    )
}
