package hammerlab.iterator.macros

import java.io.PrintStream

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

object Macros {
  def foo[L, R](l: L, r: R)(implicit ps: PrintStream): Unit = macro Impl.foo[L, R]
}

object Impl {

  val comma = """(?s)\s*,(\s*.*)""".r
  val brace = """\s*\{\s*""".r

  def stripMargin(lines: Seq[String]): Seq[String] = {
    val spaces =
      lines
        .map {
          _
            .takeWhile(_.isWhitespace)
            .length
        }
        .min

    lines
      .map {
        _.drop(spaces)
      }
  }

  def foo[L: c.WeakTypeTag, R: c.WeakTypeTag](c: Context)(l: c.Expr[L], r: c.Expr[R])(ps: c.Expr[PrintStream]): c.Tree = {
    import c.universe._

    val lpos = l.tree.pos
    val rpos = r.tree.pos

    val content = lpos.source.content

    val between =
      content
        .subSequence(
          lpos.end,
          rpos.start
        )
        .toString

    val rstart =
      between match {
        case comma(extra) ⇒
          extra.indexOf('\n') match {
            case n if n == extra.length ⇒
              rpos.start - extra.length
            case n ⇒
              rpos.start - extra.length + n + 1
          }

        case l ⇒
          throw new IllegalStateException(
            s"Unrecognized inter-argument span: $l"
          )
      }

    def code(start: Int, end: Int) =
      Literal(
        Constant(
          stripMargin(
            content
              .subSequence(start, end)
              .toString
              .split("\n") match {
                case lines
                  if lines.head.matches("\\s*\\{\\s*") &&
                     lines.last.matches("\\s*\\}\\s*") ⇒
                  lines
                    .slice(
                      1,
                      lines.size - 1
                    )
                case lines ⇒ lines
              }
          )
          .mkString("\n")
        )
      )

    def printlns(exps: Literal*) =
      Block(
        exps
          .map {
            exp ⇒
              Apply(
                Select(
                  ps.tree,
                  TermName("println")
                ),
                List(exp)
              )
          }: _*
      )

    implicit def liftString(s: String): Literal = Literal(Constant(s))

    printlns(
      "Input:\n",
      code(
        lpos.start,
        lpos.end
      ),
      "Output:\n",
      code(
        rstart,
        rpos.end
      )
    )
  }
}
