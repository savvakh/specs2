package org.specs2
package specification
import execute._

sealed trait Fragment {
  def matches(s: String) = true
}
case class SpecStart(name: String) extends Fragment
case class SpecEnd(name: String) extends Fragment
case class Group(fragments: List[Fragment])
case class Text(t: String) extends Fragment {
  override def matches(s: String) = t.matches(s)
}
case class Example(desc: String = "", body: () => Result) extends Fragment with Executable { 
  def execute = body()
  override def matches(s: String) = desc.matches(s)
  override def toString = "Example("+desc+")"
}
case class Step(action: () => Result) extends Fragment with Executable {
  def execute = action()
  override def toString = "Step"
}
case class Args(ex: String = ".*", xonly: Boolean = false) {
  def ^(f: Fragment) = new Fragments(() => List(f), this)
}
case object Args {
  def apply(arguments: Array[String]) = {
    new Args(xonly = arguments.exists(_.contains("xonly")))
  }
}
object StandardFragments {
  case class End() extends Fragment
  case class Par() extends Fragment
  case class Br() extends Fragment
}

