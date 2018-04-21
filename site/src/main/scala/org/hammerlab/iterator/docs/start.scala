package org.hammerlab.iterator.docs

import org.hammerlab.docs.block

object start {
  val ! = make()

  @block trait setup {
    val it = (1 to 10).iterator.buffered
  }

  @block trait eager {
    val it2 = (1 to 10).iterator
  }

  @block trait stdlib {
    val it3 = (1 to 10).iterator
  }

  @block trait stdlib2 {
    val it4 = (1 to 10).iterator
  }

  object make
    extends base
       with setup
       with eager
       with stdlib
       with stdlib2 {
    def apply() =
      pkg(
        h3(t"Buffered {${'take}, ${'drop}, ${'collect}}${'while}"),
        p"${'takewhile}, ${'dropwhile}, and ${'collectwhile} for ${'BufferedIterator}s, consuming only the necessary elements:",
        fence(
          setup,
          example(
            it.takewhile(_ < 4),
            Iterator(1, 2, 3)
          ),
          "4, 5, and 6 are discarded",
          block {
            it.dropwhile(_ < 7)
          },
          example(
            it.collectwhile {
              case n if n < 9 ⇒ n * 111
            },
            Iterator(777, 888)
          ),
          example(
            it,
            Iterator(9, 10)
          )
        ),
        h3(t"Eager ${'take}, ${'drop}"),
        p"${'takeEager} and ${'dropEager} do what you probably expect ${'take} and ${'drop} to do:",
        fence(
          eager,
          example(
            it2.takeEager(4),
            Array(1, 2, 3, 4)
          ),
          example(
            it2.dropEager(3),
            Iterator(8, 9, 10)
          )
        ),
        p"OTOH, the standard library's implementations have ${del('wrong)} unexpected semantics in some cases:",
        fence(
          stdlib,
          example(
            it3.take(4),
            Iterator(1, 2, 3, 4)
          ),
          example(
            it3,
            // 😱
            Iterator(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
          ),
          stdlib2,
          example(
            it4.drop(3),
            Iterator(4, 5, 6, 7, 8, 9, 10)
          ),
          example(
            it4,
            // 🙀
            Iterator(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
          )
        ),
        p"See ${issue('scala, 'bug, 9247, 308218901)} for more information",
        h3(t"{${'head},${'next}}${'Option}"),
        fence(
          example(
            Iterator(1, 2, 3, 4).buffered.headOption,
            Some(1)
          ),
          example(
            Iterator(1, 2, 3, 4).nextOption,
            Some(1)
          )
        )
      )
  }
}
