package org.hammerlab.iterator.either

import hammerlab.iterator.start._
import hammerlab.iterator.macros.IteratorOps
import org.hammerlab.iterator.util.SimpleIterator

import scala.collection.mutable.ArrayBuffer

@IteratorOps
class EitherIterator[T, U](it: BufferedIterator[Either[T, U]]) {

  def findLeft: Option[T] =
    it
      .collect {
        case Left(t) ⇒ t
      }
      .buffered
      .headOption

  def groupByLeft: BufferedIterator[(T, Iterator[U])] = {

    // Clear out any leading Rights
    it
      .collectwhile {
        case Right(_) ⇒ null
      }
      .toList

    new SimpleIterator[(T, BufferedIterator[U])] {
      var curLeft: Option[T] = None
      var curRights = Iterator[U]().buffered
      override protected def _advance: Option[(T, BufferedIterator[U])] = {

        // Clear any unused elements from the previous rights/U's/"values" iterator
        curRights.toList

        it
          .nextOption
          .collect {
            case Left(t) ⇒
              curRights =
                it
                  .collectwhile {
                    case Right(u) ⇒ u
                  }

              t → curRights
            case Right(u) ⇒
              throw new IllegalStateException(
                s"nextOption should not be a Right"
              )
          }
      }
    }
  }

  def roundUpRight: BufferedIterator[(Seq[T], U)] =
    new SimpleIterator[(Seq[T], U)] {
      override protected def _advance: Option[(Seq[T], U)] = {
        val lefts = ArrayBuffer[T]()
        while (true) {
          it.headOption match {
            case Some(Left(t)) ⇒
              it.next
              lefts += t
            case Some(Right(u)) ⇒
              it.next
              return Some(lefts → u)
            case None ⇒
              return None
          }
        }
        ???  // can't get here
      }
    }
}
