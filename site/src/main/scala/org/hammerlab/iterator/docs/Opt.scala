package org.hammerlab.iterator.docs

sealed trait Opt[+T]
object Opt {
  case object Non extends Opt[Nothing]
  case  class Som[T](value: T) extends Opt[T]

  implicit def lift[T](t: T): Opt[T] = Som(t)
  implicit def toStd[T](t: Opt[T]): Option[T] =
    t match {
      case Non    ⇒ None
      case Som(t) ⇒ Some(t)
    }
  implicit def fromStd[T](t: Option[T]): Opt[T] =
    t match {
      case None    ⇒ Non
      case Some(t) ⇒ Som(t)
    }
}
