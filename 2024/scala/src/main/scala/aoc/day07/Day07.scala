package aoc
package day07

object Day07 extends Aoc {

  private val opPermutationsPart1 = scala.collection.mutable.Map.empty[Long, Seq[Seq[Op]]]
  private val opPermutationsPart2 = scala.collection.mutable.Map.empty[Long, Seq[Seq[Op]]]

  override def part1(lines: List[String]): String = {
    val equations = lines.map(Equation.apply)
    val sum = equations.collect { case e if validPart1(e) => e.result }.sum
    sum.toString
  }

  override def part2(lines: List[String]): String = {
    val equations = lines.map(Equation.apply)
    val sum = equations.collect { case e if validPart2(e) => e.result }.sum
    sum.toString
  }

  private def validPart1(e: Equation): Boolean = {
    val ops = opPermutationsPart1.getOrElseUpdate(e.inputs.length - 1, createOpPermutationsPart1(e.inputs.length - 1))
    ops.exists { seq =>
      val iter = seq.iterator
      e.result == e.inputs.tail.foldLeft(e.inputs.head) { case (res, next) => iter.next().apply(res, next) }
    }
  }

  private def createOpPermutationsPart1(count: Int): Seq[Seq[Op]] = {
    var ops = Seq(Seq[Op](Add), Seq[Op](Mult))
    var i = 0
    while (i < count) {
      val add = ops.map(p => p :+ Add)
      val mult = ops.map(p => p :+ Mult)
      ops = add ++ mult
      i += 1
    }
    ops
  }

  private def validPart2(e: Equation): Boolean = {
    val ops = opPermutationsPart2.getOrElseUpdate(e.inputs.length - 1, createOpPermutationsPart2(e.inputs.length - 1))
    ops.exists { seq =>
      val iter = seq.iterator
      e.result == e.inputs.tail.foldLeft(e.inputs.head) { case (res, next) => iter.next().apply(res, next) }
    }
  }

  private def createOpPermutationsPart2(count: Int): Seq[Seq[Op]] = {
    var ops = Seq(Seq[Op](Add), Seq[Op](Mult), Seq[Op](Concat))
    var i = 0
    while (i < count) {
      val add = ops.map(p => p :+ Add)
      val mult = ops.map(p => p :+ Mult)
      val concat = ops.map(p => p :+ Concat)
      ops = add ++ mult ++ concat
      i += 1
    }
    ops
  }

  case class Equation(result: Long, inputs: Seq[Long])

  object Equation {
    def apply(line: String): Equation = {
      val Array(r, i) = line.split(':')
      Equation(r.toLong, i.split(' ').map(_.trim).filter(_.nonEmpty).map(_.toLong))
    }
  }

  sealed trait Op {
    def apply(left: Long, right: Long): Long
  }

  case object Add extends Op {
    override def apply(left: Long, right: Long): Long = left + right
  }

  case object Mult extends Op {
    override def apply(left: Long, right: Long): Long = left * right
  }

  case object Concat extends Op {
    override def apply(left: Long, right: Long): Long = (left.toString + right.toString).toLong
  }
}
