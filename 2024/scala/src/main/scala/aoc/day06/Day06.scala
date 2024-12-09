package aoc
package day06

object Day06 extends Aoc {

  override def part1(lines: List[String]): String = {
    val grid: Array[Array[Char]] = lines.map(_.toCharArray).toArray
    var (i, j) = findStart(grid)

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
    val grid: Array[Array[Char]] = lines.map(_.toCharArray).toArray
    val start = findStart(grid)

    var loops = 0

    var i = 0
    while (i < grid.length) {
      var j = 0
      while (j < grid(0).length) {
        if (grid(i)(j) == '.') {
          try {
            grid(i)(j) = '#'
            if (checkLoop(grid, start)) {
              loops += 1
            }
          } finally {
            grid(i)(j) = '.'
          }
        }
        j += 1
      }
      i += 1
    }

    loops.toString
  }

  private def findStart(grid: Array[Array[Char]]): (Int, Int) = {
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
    (i, j)
  }

  private def checkLoop(grid: Array[Array[Char]], start: (Int, Int)): Boolean = {
    val positions = scala.collection.mutable.Set.empty[(Int, Int, Dir.Value)]

    var (i, j) = start
    var done = false
    var dir = Dir.Up
    while (!done) {
      if (!positions.add((i, j, dir))) {
        return true
      }
      dir match {
        case Dir.Up =>
          if (i == 0) {
            done = true
          } else if (grid(i - 1)(j) == '#') {
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
          } else if (grid(i)(j + 1) == '#') {
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

    false
  }

  object Dir extends Enumeration {
    type Dir = Value
    val Up, Left, Right, Down = Value
  }
}
