package aoc
package day03

object Day03 extends Aoc {

  override def part1(lines: List[String]): Long = {
    val regex = """mul\((\d{1,3}),(\d{1,3})\)""".r
    var sum = 0
    lines.foreach { line =>
      regex.findAllMatchIn(line).foreach { m =>
        sum += (m.group(1).toInt * m.group(2).toInt)
      }
    }
    sum
  }

  override def part2(lines: List[String]): Long = {
    val regex = """mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""".r
    var sum = 0
    var enabled = true
    lines.foreach { line =>
      regex.findAllMatchIn(line).foreach { m =>
        if (m.matched.startsWith("don")) {
          enabled = false
        } else if (m.matched.startsWith("do")) {
          enabled = true
        } else if (enabled) {
          sum += (m.group(1).toInt * m.group(2).toInt)
        }
      }
    }
    sum
  }
}
