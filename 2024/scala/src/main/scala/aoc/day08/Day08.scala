package aoc
package day08

import java.math.BigInteger
import scala.collection.mutable.ArrayBuffer

object Day08 extends Aoc {

  override def part1(lines: List[String]): String = {
    val grid: Array[Array[Char]] = lines.map(_.toCharArray).toArray
    val antinodes = scala.collection.mutable.Set.empty[(Int, Int)]
    val freqs = parseGrid(grid)
    freqs.foreach { case (_, locations) =>
      var i = 0
      while (i < locations.length) {
        var j = i + 1
        while (j < locations.length) {
          antinodes ++= findAntiNodes(locations(i), locations(j)).filter { case (i, j) => i >= 0 && i < grid.length && j >= 0 && j < grid(0).length }
          j += 1
        }
        i += 1
      }
    }
    antinodes.size.toString
  }

  override def part2(lines: List[String]): String = {
    val grid: Array[Array[Char]] = lines.map(_.toCharArray).toArray
    val antinodes = scala.collection.mutable.Set.empty[(Int, Int)]
    val freqs = parseGrid(grid)
    freqs.foreach { case (_, locations) =>
      var i = 0
      while (i < locations.length) {
        var j = i + 1
        while (j < locations.length) {
          antinodes ++= findAntiNodesPart2(locations(i), locations(j), grid.length, grid(0).length)
          j += 1
        }
        i += 1
      }
    }
    antinodes.size.toString
  }

  private def findAntiNodes(n1: (Int, Int), n2: (Int, Int)): Seq[(Int, Int)] = {
    val (i1, j1) = n1
    val (i2, j2) = n2
    if (i1 < i2) {
      val deltaI = i2 - i1
      if (j1 < j2) {
        val deltaJ = j2 - j1
        Seq((i1 - deltaI, j1 - deltaJ), (i2 + deltaI, j2 + deltaJ))
      } else {
        val deltaJ = j1 - j2
        Seq((i1 - deltaI, j1 + deltaJ), (i2 + deltaI, j2 - deltaJ))
      }
    } else {
      val deltaI = i1 - i2
      if (j1 < j2) {
        val deltaJ = j2 - j1
        Seq((i1 + deltaI, j1 - deltaJ), (i2 - deltaI, j2 + deltaJ))
      } else {
        val deltaJ = j1 - j2
        Seq((i1 + deltaI, j1 + deltaJ), (i2 - deltaI, j2 - deltaJ))
      }
    }
  }

  private def findAntiNodesPart2(n1: (Int, Int), n2: (Int, Int), iSize: Int, jSize: Int): Seq[(Int, Int)] = {
    val (i1, j1) = n1
    val (i2, j2) = n2
    val deltaI = i2 - i1
    val deltaJ = j2 - j1
    val gcd = BigInteger.valueOf(deltaI).gcd(BigInteger.valueOf(deltaJ))
    computeLine(i1, j1, deltaI / gcd.intValue(), deltaJ / gcd.intValue(), iSize, jSize)
  }

  private def computeLine(startI: Int, startJ: Int, deltaI: Int, deltaJ: Int, iSize: Int, jSize: Int): Seq[(Int, Int)] = {
    val result = ArrayBuffer.empty[(Int, Int)]
    var i = startI
    var j = startJ
    var checkI = if (deltaI > 0) { () => i >= 0 } else { () => i < iSize }
    var checkJ = if (deltaJ > 0) { () => j >= 0 } else { () => j < jSize }
    while (checkI() && checkJ()) {
      result += i -> j
      i -= deltaI
      j -= deltaJ
    }
    i = startI + deltaI
    j = startJ + deltaJ
    checkI = if (deltaI < 0) { () => i >= 0 } else { () => i < iSize }
    checkJ = if (deltaJ < 0) { () => j >= 0 } else { () => j < jSize }
    while (checkI() && checkJ()) {
      result += i -> j
      i += deltaI
      j += deltaJ
    }
    result.toSeq
  }

  private def parseGrid(grid: Array[Array[Char]]): Map[Char, List[(Int, Int)]] = {
    val freqs = scala.collection.mutable.Map.empty[Char, ArrayBuffer[(Int, Int)]]
    var i = 0
    while (i < grid.length) {
      var j = 0
      while (j < grid.length) {
        val f = grid(i)(j)
        if (f != '.') {
          freqs.getOrElseUpdate(f, ArrayBuffer.empty) += i -> j
        }
        j += 1
      }
      i += 1
    }
    freqs.toMap.map { case (k, v) => k -> v.toList }
  }
}
