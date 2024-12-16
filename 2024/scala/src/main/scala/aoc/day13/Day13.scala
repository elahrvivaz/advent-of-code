package aoc.day13

import aoc.Aoc

import scala.collection.mutable.ArrayBuffer

object Day13 extends Aoc {

  override def part1(lines: List[String]): String = {
    val machines = parseMachines(lines)
    machines.flatMap(_.solvePart2()).sum.toString
  }

  override def part2(lines: List[String]): String = {
    val machines = parseMachines(lines).map(m => m.copy(prize = Prize(m.prize.x + 10000000000000d, m.prize.y + 10000000000000d)))
    machines.flatMap(_.solvePart2()).sum.toString
  }

  private def parseMachines(lines: List[String]): List[Machine] = lines.filter(_.nonEmpty).grouped(3).map(Machine.apply).toList

  // note: need bigdecimal precision here to get the right answer
  case class Button(x: BigDecimal, y: BigDecimal)
  case class Prize(x: BigDecimal, y: BigDecimal)

  case class Machine(a: Button, b: Button, prize: Prize) {
    def solvePart1(): Option[Long] = {
      var cost = Long.MaxValue
      for (i <- 0 to 100) {
        for (j <- 0 to 100) {
          if (a.x * i + b.x * j == prize.x && a.y * i + b.y * j == prize.y) {
            cost = math.min(cost, i * 3 + j)
          }
        }
      }
      if (cost == Long.MaxValue) { None } else { Some(cost) }
    }

    def solvePart2(): Option[Long] = {
      // find the intersection of the two lines defining the score
      val i = (((b.x * b.y * prize.x) / b.x) - ((b.x * b.y * prize.y) / b.y)) / (b.y * a.x - b.x * a.y)
      val j = (prize.x - i * a.x) / b.x
      if (i <= 0 || j <= 0 || (i - i.toLong > 0.0001) || (j - j.toLong > 0.0001)) {
        None
      } else {
        Some((i * 3 + j).toLong)
      }
    }
  }

  object Machine {

    private val ButtonRegex = """Button (A|B): X\+(\d+), Y\+(\d+)""".r
    private val PrizeRegex = """Prize: X=(\d+), Y=(\d+)""".r

    // Button A: X+57, Y+16
    // Button B: X+20, Y+74
    // Prize: X=3288, Y=1772

    def apply(lines: Seq[String]): Machine = {
      val a = lines.head match {
        case ButtonRegex(_, x, y) => Button(BigDecimal(x.toLong), BigDecimal(y.toLong))
      }
      val b = lines(1) match {
        case ButtonRegex(_, x, y) => Button(BigDecimal(x.toLong), BigDecimal(y.toLong))
      }
      val p = lines(2) match {
        case PrizeRegex(x, y) => Prize(BigDecimal(x.toLong), BigDecimal(y.toLong))
      }
      Machine(a, b, p)
    }
  }
}
