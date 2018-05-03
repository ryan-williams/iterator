package org.hammerlab.iterator.docs

import org.hammerlab.docs.block

trait util
  extends base {
  sections +=
    pkg(
      c3('SimpleIterator), // TODO: link
      p"Interface for easily defining (buffered) ${'Iterator}s in terms of one function, ${'_advance}, that returns an ${'Option}.",
      p"${'head} and ${'hasNext} are cached until ${'next} is called",
      p"Many implementations can be found in this codebase, but here is a sample mimicking ${'takewhile} above:",  // TODO: github search link
      fence(
        block {
          implicit class Ops[T](it: BufferedIterator[T]) {
            def takewhile(fn: T ⇒ Boolean) =
              new SimpleIterator[T] {
                def _advance: Option[T] =
                  if (it.hasNext && fn(it.head))
                    Some(it.next)
                  else
                    None
              }
          }
        }
      ),
      p"${'SimpleIterator} also contains hooks for maintaining state across calls to ${'next}, and responding to the iterator becoming empty.",
      p"Here's an example of the latter from the implementation of ${'finish} above:",
      fence(
        block {
          implicit class Ops[T](it: Iterator[T]) {
            def finish(fn: ⇒ Unit) =
              new SimpleIterator[T] {
                def _advance: Option[T] =
                  if (it.hasNext)
                    Some(it.next)
                  else
                    None

                override def done(): Unit = fn
              }
          }
        }
      )
    )
}
