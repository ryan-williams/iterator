package org.hammerlab.iterator.site.sections

import org.hammerlab.iterator.site.base

object sample
  extends base {
  scala.util.Random.setSeed(123)
  val ! =
    pkg(
      p"Resevoir-sample:",
      fence(
        example(
          (1 to 100).sample(5),
          Array(15, 33, 40, 75, 83)
        )
      )
    )
}
