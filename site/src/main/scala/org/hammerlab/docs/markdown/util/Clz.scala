package org.hammerlab.docs.markdown.util

case class Clz(values: Seq[Clz.Entry] = Nil) {
  def &(entry: Clz.Entry): Clz = Clz(values :+ entry)
}
object Clz
  extends symbol {
  case class Entry(override val toString: String)
  object Entry {
    implicit def fromSym(value: String): Entry = Entry(value)
    implicit def fromStr(value: Symbol): Entry = Entry(value)
  }
  implicit def fromSym  (value: String): Clz = Clz(value :: Nil)
  implicit def fromStr  (value: Symbol): Clz = Clz(value :: Nil)
  implicit def fromEntry(value: Entry): Clz = Clz(value :: Nil)
  implicit def fromEntries(values: Seq[Entry]): Clz = Clz(values)
}
