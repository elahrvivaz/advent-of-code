package aoc.day12

import aoc.Aoc

import scala.collection.mutable.ArrayBuffer

object Day12 extends Aoc {

  override def part1(lines: List[String]): Long = {
    val map = lines.map(_.toCharArray).toArray
    val regions = Array.fill(map.length)(Array.fill(map.head.length)(-1))
    val numRegions = populateRegions(map, regions)

    val areas = ArrayBuffer.fill(numRegions)(0)
    val perimeters = ArrayBuffer.fill(numRegions)(0)
    var i = 0
    while (i < regions.length) {
      var j = 0
      while (j < regions(i).length) {
        val r = regions(i)(j)
        var sides = 0
        if (i == 0 || regions(i - 1)(j) != r) {
          sides += 1
        }
        if (i == regions.length - 1 || regions(i + 1)(j) != r) {
          sides += 1
        }
        if (j == 0 || regions(i)(j - 1) != r) {
          sides += 1
        }
        if (j == regions(i).length - 1 || regions(i)(j + 1) != r) {
          sides += 1
        }
        perimeters(r) = perimeters(r) + sides
        areas(r) = areas(r) + 1
        j += 1
      }
      i += 1
    }

    var price = 0
    i = 0
    while (i < areas.length) {
      price += (areas(i) * perimeters(i))
      i += 1
    }
    price
  }

  override def part2(lines: List[String]): Long = {
    val map = lines.map(_.toCharArray).toArray
    val regions = Array.fill(map.length)(Array.fill(map.head.length)(-1))
    val numRegions = populateRegions(map, regions)

    val areas = ArrayBuffer.fill(numRegions)(0)
    val sides = ArrayBuffer.fill(numRegions)(0)
    var i = 0
    while (i < regions.length) {
      var j = 0
      while (j < regions(i).length) {
        val r = regions(i)(j)
        // count corners to count total sides
        var edges = 0
        if ((i == 0 || regions(i - 1)(j) != r) && (j == 0 || regions(i)(j - 1) != r)) {
          edges += 2 // external corner
        }
        if ((i == regions.length - 1 || regions(i + 1)(j) != r) && (j == regions(i).length - 1 || regions(i)(j + 1) != r)) {
          edges += 2 // external corner
        }
        if (i > 0 && j < regions(i).length - 1 && regions(i - 1)(j + 1) != r && regions(i)(j + 1) == r && regions(i - 1)(j) == r) {
          edges += 2 // internal corner
        }
        if (i < regions.length - 1 && j > 0 && regions(i + 1)(j - 1) != r && regions(i)(j - 1) == r && regions(i + 1)(j) == r) {
          edges += 2 // internal corner
        }
        sides(r) = sides(r) + edges
        areas(r) = areas(r) + 1
        j += 1
      }
      i += 1
    }

    var price = 0
    i = 0
    while (i < areas.length) {
      price += (areas(i) * sides(i))
      i += 1
    }
    price
  }

  /**
   * Populate which region each cell belongs to
   *
   * @param map map
   * @param regions regions to populate
   * @return number of regions
   */
  private def populateRegions(map: Array[Array[Char]], regions: Array[Array[Int]]): Int = {
    var curRegion = 0

    def traceRegion(i: Int, j: Int): Unit = {
      regions(i)(j) = curRegion
      val c = map(i)(j)
      if (i > 0 && regions(i - 1)(j) == -1 && map(i - 1)(j) == c) {
        traceRegion(i - 1, j)
      }
      if (i < regions.length - 1 && regions(i + 1)(j) == -1 && map(i + 1)(j) == c) {
        traceRegion(i + 1, j)
      }
      if (j > 0 && regions(i)(j - 1) == -1 && map(i)(j - 1) == c) {
        traceRegion(i, j - 1)
      }
      if (j < regions(i).length - 1 && regions(i)(j + 1) == -1 && map(i)(j + 1) == c) {
        traceRegion(i, j + 1)
      }
    }

    var i = 0
    while (i < map.length) {
      var j = 0
      while (j < map(i).length) {
        if (regions(i)(j) == -1) {
          traceRegion(i, j)
          curRegion += 1
        }
        j += 1
      }
      i += 1
    }

    curRegion
  }
}
