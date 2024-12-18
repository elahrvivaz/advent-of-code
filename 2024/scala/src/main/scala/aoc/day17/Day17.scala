package aoc.day17

import aoc.Aoc

import scala.collection.mutable.ArrayBuffer

object Day17 extends Aoc {

  override def part1(lines: List[String]): Long = {
    val ops = parseInput(lines)
    val output = run(ops)
    println(output.mkString(","))
    output.sum
  }

  override def part2(lines: List[String]): Long = {
    val ops = parseInput(lines)
    var a: Long = Int.MaxValue
    val b = ops.head.b.value
    val c = ops.head.c.value

    val program = ops.flatMap(o => Seq(o.opcode, o.operand)).toSeq
    ops.head.a.value = a
    while (run(ops, Some(program)) != program && a < Long.MaxValue) {
      a += 1
      ops.head.a.value = a
      ops.head.b.value = b
      ops.head.c.value = c
    }
    a
  }

  private def parseInput(lines: List[String]): Array[Instruction] = {
    //Register A: 729
    //Register B: 0
    //Register C: 0
    //
    //Program: 0,1,5,4,3,0
    val a = Register(lines.head.split(":").last.trim.toInt)
    val b = Register(lines(1).split(":").last.trim.toInt)
    val c = Register(lines(2).split(":").last.trim.toInt)
    lines.last.split(":").last.split(",").grouped(2).map { case Array(opcode, operand) =>
      Instruction(opcode.trim.toInt, operand.trim.toInt, a, b, c)
    }.toArray
  }

  def run(ops: Array[Instruction], expectedOutput: Option[Seq[Int]] = None): Seq[Int] = {
    val output = ArrayBuffer.empty[Int]
    var i = 0
    var c = 0
    while (i < ops.length) {
      ops(i).apply() match {
        case None =>
          i += 1
        case Some(Output(v)) =>
          output += v.toInt
          if (expectedOutput.exists(o => !o.startsWith(output))) {
            return Seq.empty
          }
          i += 1
        case Some(Jump(p)) =>
          i = (p / 2).toInt
          if (p % 2 != 0) {
            throw new RuntimeException()
          }
      }
    }
    output.toSeq
  }

  class Register(var value: Long)

  sealed trait Result
  case class Output(value: Long) extends Result
  case class Jump(position: Long) extends Result

  sealed abstract class Instruction(val opcode: Int, val operand: Int, val a: Register, val b: Register, val c: Register) {

    def apply(): Option[Result]

    protected def combo(operand: Int): Long = {
      if (operand < 4) {
        operand
      } else if (operand == 4) {
        a.value
      } else if (operand == 5) {
        b.value
      } else if (operand == 6) {
        c.value
      } else {
        throw new IllegalArgumentException()
      }
    }

    override def toString: String = s"$opcode,$operand"
  }

  object Instruction {
    def apply(opcode: Int, operand: Int, a: Register, b: Register, c: Register): Instruction = opcode match {
      case 0 => new adv(operand, a, b, c)
      case 1 => new bxl(operand, a, b, c)
      case 2 => new bst(operand, a, b, c)
      case 3 => new jnz(operand, a, b, c)
      case 4 => new bxc(operand, a, b, c)
      case 5 => new out(operand, a, b, c)
      case 6 => new bdv(operand, a, b, c)
      case 7 => new cdv(operand, a, b, c)
    }
  }

  class adv(operand: Int, a: Register, b: Register, c: Register) extends Instruction(0, operand, a, b, c) {
    override def apply(): Option[Result] = {
      a.value = (a.value / math.pow(2, combo(operand))).toInt
      None
    }
  }

  class bxl(operand: Int, a: Register, b: Register, c: Register) extends Instruction(1, operand, a, b, c) {
    override def apply(): Option[Result] = {
      b.value = operand ^ b.value
      None
    }
  }

  class bst(operand: Int, a: Register, b: Register, c: Register) extends Instruction(2, operand, a, b, c) {
    override def apply(): Option[Result] = {
      b.value = combo(operand) % 8
      None
    }
  }

  class jnz(operand: Int, a: Register, b: Register, c: Register) extends Instruction(3, operand, a, b, c) {
    override def apply(): Option[Result] =
      if (a.value == 0) { None } else { Some(Jump(operand)) }
  }

  class bxc(operand: Int, a: Register, b: Register, c: Register) extends Instruction(4, operand, a, b, c) {
    override def apply(): Option[Result] = {
      b.value = b.value ^ c.value
      None
    }
  }

  class out(operand: Int, a: Register, b: Register, c: Register) extends Instruction(5, operand, a, b, c) {
    override def apply(): Option[Result] = Some(Output(combo(operand) % 8))
  }

  class bdv(operand: Int, a: Register, b: Register, c: Register) extends Instruction(6, operand, a, b, c) {
    override def apply(): Option[Result] = {
      b.value = (a.value / math.pow(2, combo(operand))).toInt
      None
    }
  }

  class cdv(operand: Int, a: Register, b: Register, c: Register) extends Instruction(7, operand, a, b, c) {
    override def apply(): Option[Result] = {
      c.value = (a.value / math.pow(2, combo(operand))).toInt
      None
    }
  }
}
