package aoc
package day04

object Day04 extends Aoc {

  override def part1(lines: List[String]): String = {
    val matrix: Array[Array[Char]] = lines.map(_.toCharArray).toArray
    var count = 0
    var i, j = 0
    while (i < matrix.length) {
      while (j < matrix(0).length) {
        if (matrix(i)(j) == 'X') {
          count += search1(matrix, i, j)
        }
        j += 1
      }
      i += 1
      j = 0
    }
    count.toString
  }

  private def search1(matrix: Array[Array[Char]], i: Int, j: Int): Int = {
    var count = 0
    // N
    if (j > 2 && matrix(i)(j - 1) == 'M' && matrix(i)(j - 2) == 'A' && matrix(i)(j - 3) == 'S') {
      count += 1
    }
    // S
    if (j < matrix.head.length - 3 && matrix(i)(j + 1) == 'M' && matrix(i)(j + 2) == 'A' && matrix(i)(j + 3) == 'S') {
      count += 1
    }
    // E
    if (i < matrix.length - 3 && matrix(i + 1)(j) == 'M' && matrix(i + 2)(j) == 'A' && matrix(i + 3)(j) == 'S') {
      count += 1
    }
    // W
    if (i > 2 && matrix(i - 1)(j) == 'M' && matrix(i - 2)(j) == 'A' && matrix(i - 3)(j) == 'S') {
      count += 1
    }
    // NE
    if (i < matrix.length - 3 && j > 2 && matrix(i + 1)(j - 1) == 'M' && matrix(i + 2)(j - 2) == 'A' && matrix(i + 3)(j - 3) == 'S') {
      count += 1
    }
    // SE
    if (i < matrix.length - 3 && j < matrix.head.length - 3  && matrix(i + 1)(j + 1) == 'M' && matrix(i + 2)(j + 2) == 'A' && matrix(i + 3)(j + 3) == 'S') {
      count += 1
    }
    // SW
    if (i > 2 && j < matrix.head.length - 3 && matrix(i - 1)(j + 1) == 'M' && matrix(i - 2)(j + 2) == 'A' && matrix(i - 3)(j + 3) == 'S') {
      count += 1
    }
    // NW
    if (i > 2 && j  > 2 && matrix(i - 1)(j - 1) == 'M' && matrix(i - 2)(j - 2) == 'A' && matrix(i - 3)(j - 3) == 'S') {
      count += 1
    }
    count
  }

  override def part2(lines: List[String]): String = {
    val matrix: Array[Array[Char]] = lines.map(_.toCharArray).toArray
    var count = 0
    var i, j = 1
    while (i < matrix.length - 1) {
      while (j < matrix(0).length - 1) {
        if (matrix(i)(j) == 'A') {
          count += search2(matrix, i, j)
        }
        j += 1
      }
      i += 1
      j = 1
    }
    count.toString
  }

  private def search2(matrix: Array[Array[Char]], i: Int, j: Int): Int = {
    val NE = matrix(i + 1)(j - 1)
    val SW = matrix(i - 1)(j + 1)
    val NW = matrix(i - 1)(j - 1)
    val SE = matrix(i + 1)(j + 1)

    if (((NE == 'M' && SW == 'S') || (NE == 'S' && SW == 'M')) && ((NW == 'M' && SE == 'S') || (NW == 'S' && SE == 'M'))) {
      1
    } else {
      0
    }
  }
}
