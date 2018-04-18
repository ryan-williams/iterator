package org.hammerlab.iterator.count

import hammerlab.iterator._
import org.hammerlab.iterator
import org.hammerlab.docs.Macros.example
import cats.instances.int.catsKernelStdOrderForInt
import org.hammerlab.docs.Code

trait docs
  extends iterator.docs {
  val count =
    pkg(
      'count,
      code(
        example(
          Array(1, 2, 1, 3).countElems,
          Map(1→2, 2→1, 3→1)
        ),
        example(
          Iterator('a→1, 'b→2, 'a→10, 'c→3).countByKey,
          Map('a→2, 'b→1, 'c→1)
        )
      )
    )
}

object docs extends docs {
  val body = count
}
