package org.hammerlab.iterator.site

import hammerlab.docs
import hammerlab.indent.spaces
import hammerlab.show._
import org.hammerlab.docs.markdown._
import org.hammerlab.docs.markdown.dsl._
import org.hammerlab.docs.markdown.tree.NonLink._
import org.hammerlab.docs.markdown.util.symbol

trait Pkg
  extends dsl {

  def pkg(body: Elem*)(implicit name: sourcecode.FullName): Section =
    section(
      Text(
        name
          .value
          .split("\\.")
          .dropWhile(_ != "sections")
          .drop(1)
          .head
        ),
      body
    )

  def c3(name: String) = section(Code(name))
}

trait base
  extends docs
     with dsl
     with symbol
     with hammerlab.iterator.all
     with cats.instances.AllInstances
     with Pkg
