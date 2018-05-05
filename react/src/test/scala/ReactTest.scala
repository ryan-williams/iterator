
class ReactTest
  extends hammerlab.Suite {
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
}
