import shapeless.{ Generic, HList }

class ReactTest
  extends hammerlab.Suite {

  test("conv") {
    implicitly[Iso[A, B]].apply(A(4, "123")) should be(B(4, "123"))
  }

/*
  import japgolly.scalajs.react.vdom.html_<^._
  import japgolly.scalajs.react.vdom.html_<^.<.{div,p}
  test("keys") {
    println("pre")
    val p1 = p(^.key := 1, "yay")
    println("post")
    println(p1)
    println(p1.getClass)

    val p2 = p("boo")
    println(p2.getClass)

    val p3 = p("boo3")

    val d = div(
      p1,
      p2,
      p3
    )

    println("d")
    println(d.getClass)
    println(d.render)
  }
*/
}

case class A(n: Int, s: String)
case class B(n: Int, s: String)

trait Iso[A, B] {
  def apply(a: A): B
}
object Iso {
  implicit def gen[A, B, L <: HList](implicit
                                     genA: Generic.Aux[A, L],
                                     genB: Generic.Aux[B, L]): Iso[A, B] =
    (a: A) ⇒
      genB.from(
        genA.to(
          a
        )
      )
}
