package org.hammerlab.iterator.docs

import org.hammerlab.docs.Code.Setup

object scan
  extends base {
  val catsMonoidLink =
    code(
      a(
        href := "https://typelevel.org/cats/typeclasses/monoid.html",
        "cats.Monoid"
      )
    )

  val ! =
    h(
      p"Scans, in terms of $catsMonoidLink, that include the final total/sum ${i('xor)} the initial zero / empty-value:",
      fence(
        Setup("import hammerlab.monoid._  // some Monoid defaults"),
        example(
          Seq(1, 2, 3, 4).scanL,
          Iterator(0, 1, 3, 6)
        ),
        example(
          Seq(1, 2, 3, 4).scanLeftInclusive,
          Iterator(1, 3, 6, 10)
        ),
        example(
          Seq(1, 2, 3, 4).scanR,
          Iterator(9, 7, 4, 0)
        ),
        example(
          Seq(1, 2, 3, 4).scanRightInclusive,
          Iterator(10, 9, 7, 4)
        )
      ),
      p"Additionally, scan over just the values of pairs:",
      fence(
        example(
          Seq('a→1, 'b→2, 'c→3, 'd→4).scanLeftValues,
          Iterator('a→0, 'b→1, 'c→3, 'd→6)
        ),
        example(
          Seq('a→1, 'b→2, 'c→3, 'd→4).scanLeftValuesInclusive,
          Iterator('a→1, 'b→3, 'c→6, 'd→10)
        ),
        example(
          Seq('a→1, 'b→2, 'c→3, 'd→4).scanRightValues,
          Iterator('a→9, 'b→7, 'c→4, 'd→0)
        ),
        example(
          Seq('a→1, 'b→2, 'c→3, 'd→4).scanRightValuesInclusive,
          Iterator('a→10, 'b→9, 'c→7, 'd→4)
        )
      )
    )
}
