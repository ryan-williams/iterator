package org.hammerlab.iterator.site.sections

import org.hammerlab.iterator.site.base

object count
  extends base {
  val ! =
    pkg(
      fence(
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
