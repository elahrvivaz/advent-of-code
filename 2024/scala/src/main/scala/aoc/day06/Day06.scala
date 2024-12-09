package aoc
package day06

object Day06 extends Aoc {

  override def part1(lines: List[String]): String = {
    val grid: Array[Array[Char]] = lines.map(_.toCharArray).toArray
    var i, j = 0
    var found = false
    while (!found && i < grid.length) {
      j = 0
      while (!found && j < grid(i).length) {
        if (grid(i)(j) == '^') {
          found = true
        } else {
          j += 1
        }
      }
      if (!found) {
        i += 1
      }
    }

    val result = scala.collection.mutable.Set.empty[(Int, Int)]

    var done = false
    var dir = Dir.Up
    while (!done) {
      result += i -> j
      dir match {
        case Dir.Up =>
          if (i == 0) {
            done = true
          } else if (grid(i-1)(j) == '#') {
            dir = Dir.Right
          } else {
            i -= 1
          }

        case Dir.Down =>
          if (i == grid.length - 1) {
            done = true
          } else if (grid(i + 1)(j) == '#') {
            dir = Dir.Left
          } else {
            i += 1
          }

        case Dir.Right =>
          if (j == grid(0).length - 1) {
            done = true
          } else if (grid(i)(j+1) == '#') {
            dir = Dir.Down
          } else {
            j += 1
          }

        case Dir.Left =>
          if (j == 0) {
            done = true
          } else if (grid(i)(j - 1) == '#') {
            dir = Dir.Up
          } else {
            j -= 1
          }
      }
    }

    result.size.toString
  }

  override def part2(lines: List[String]): String = {
    ""
  }

  object Dir extends Enumeration {
    type Dir = Value
    val Up, Left, Right, Down = Value
  }
}
