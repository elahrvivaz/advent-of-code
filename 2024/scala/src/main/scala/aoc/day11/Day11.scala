package aoc.day11

import aoc.Aoc

object Day11 extends Aoc {

  private val cache = scala.collection.mutable.Map.empty[(Long, Int), Long]

  override def part1(lines: List[String]): String = {
    val stones = lines.head.split(" +").map(_.toLong).toSeq
    stones.map(_.blink(25)).sum.toString
  }

  override def part2(lines: List[String]): String = {
    val stones = lines.head.split(" +").map(_.toLong).toSeq
    stones.map(_.blink(75)).sum.toString
  }

  implicit class Stone(val num: Long) extends AnyRef {
    def blink(count: Int): Long = {
      if (count == 0) {
        1
      } else if (num == 0) {
        1.blink(count - 1)
      } else {
        cache.getOrElseUpdate((num, count), {
          var len = 0
          var n = num
          while (n > 0) {
            n = n / 10
            len += 1
          }
          if (len % 2 == 0) {
            val exp = math.pow(10, len / 2)
            val left = (num / exp).toLong
            val right = (num % exp).toLong
            left.blink(count - 1) + right.blink(count - 1)
          } else {
            (num * 2024).blink(count - 1)
          }
        })
      }
    }
  }
}
