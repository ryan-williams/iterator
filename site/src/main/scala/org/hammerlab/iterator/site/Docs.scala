package org.hammerlab.iterator.site

import org.hammerlab.iterator.site
import hammerlab.iterator._
import hammerlab.lines.Lines._
import hammerlab.lines._
import hammerlab.show._
import org.scalajs.dom
import org.hammerlab.iterator.count
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("hammerlab.iterators.docs")
object Docs {

  def main(args: Array[String]): Unit = {
    //import org.hammerlab.iterator.count
    dom.document.getElementById("main").innerHTML = site.main.html.render
  }

//  def main(args: Array[String]): Unit = {
//    implicit val examples = ArrayBuffer[Example]()
//    implicit val pstream =
//      (Path(crossTarget.toPath) / 'examples).printStream
//
//    Macros.foo(
//      args.length > 0,
//      // test comment
//      "yay"
//    )
//
//    Macros.foo(
//      {
//        import scala.io.Source.fromFile
//        val source = fromFile("build.sbt")
//        source
//          .filter(_ == 'a')
//          .finish {
//            println("closing!")
//            source.close()
//          }
//          .size
//      },
//      // prints "closing!" and closes `source` after traversal is finished
//      32
//    )
//
//    pstream.close()
//  }

//  import scalatags.Text.TypedTag
//  import scalatags.Text.all._
//
//  def block(id: Symbol): TypedTag[String] = code()
//
//  def ===[L, R](l: L, r: R): Unit = {}
//
//  val tags =
//    html(
//      body(
//        div(
//          h2("Setup"), block('setup),
//          h2("Examples"),
//          h3("head/next options"), block( 'opts),
//          h3(            "count"), block('count)
//        )
//      )
//    )

/*  val docs =
    <html>
      <body>
        <div>
          <h2>Examples</h2>
          <pre>
            import hammerlab.iterator._
            ===(Iterator(1, 2, 3).nextOption, Some(1))
            ===(Iterator(1, 2, 3).buffered.headOption, Some(1))
            ===(   Array('a,   'b,   'a,    'c  ).countElems, Map('a→2, 'b→1, 'c→1))
            ===(Iterator('a→1, 'b→2, 'a→10, 'c→3).countByKey, Map('a→2, 'b→1, 'c→1))
          </pre>
        </div>
      </body>
    </html>*/
}

//class Blocks
//  extends hammerlab.Suite {
//
//  implicit class SymbolOps(name: Symbol) {
//    def -(body: ⇒ Unit): Unit =
//      test(name.toString.drop(1))(body)
//  }
//
//  // setup
//  import hammerlab.iterator._
//
//  'opts - {
//    ===(Iterator(1, 2, 3).nextOption, Some(1))
//    ===(Iterator(1, 2, 3).buffered.headOption, Some(1))
//  }
//
//  'count - {
//    ===(   Array('a,     'b,     'a,      'c    ).countElems, Map('a → 2, 'b → 1, 'c → 1))
//    ===(Iterator('a → 1, 'b → 2, 'a → 10, 'c → 3).countByKey, Map('a → 2, 'b → 1, 'c → 1))
//  }
//}
//
