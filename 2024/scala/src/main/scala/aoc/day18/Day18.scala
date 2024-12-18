package aoc.day18

import aoc.Aoc

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

object Day18 extends Aoc {

  override def part1(lines: List[String]): Long = {
    return 0
    val gridSize = if (lines.lengthCompare(50) < 0) { 7 } else { 71 }
    val numBytes = if (lines.lengthCompare(50) < 0) { 12 } else { 1024 }
    val grid = Array.fill(gridSize)(Array.fill(gridSize)(true))
    lines.take(numBytes).foreach { line =>
      val Array(x, y) = line.split(",")
      grid(y.trim.toInt)(x.trim.toInt) = false
    }
    findMinPath(grid, 0, 0, grid.length - 1, grid.length - 1)
  }

  override def part2(lines: List[String]): Long = {
    val gridSize = if (lines.lengthCompare(50) < 0) { 7 } else { 71 }
    val numBytes = if (lines.lengthCompare(50) < 0) { 12 } else { 1024 }
    val grid = Array.fill(gridSize)(Array.fill(gridSize)(true))
    lines.take(numBytes).foreach { line =>
      val Array(x, y) = line.split(",")
      grid(y.trim.toInt)(x.trim.toInt) = false
    }
    val iter = lines.drop(numBytes).iterator
    def drop(): String = {
      var blocked = false
      var line: String = null
      while (!blocked) {
        line = iter.next()
        val Array(x, y) = line.split(",").map(_.trim.toInt)
        grid(y)(x) = false
        blocked = Seq(Try(grid(y-1)(x)).getOrElse(false), Try(grid(y+1)(x)).getOrElse(false), Try(grid(y)(x+1)).getOrElse(false), Try(grid(y)(x-1)).getOrElse(false)).count(_ == false) > 1
      }
      line
    }

    var line = drop()
    while (findAnyPath(grid, 0, 0, grid.length - 1, grid.length - 1)) {
      line = drop()
    }
    println(line)
    0
  }

  private def parseInput(lines: List[String]): Array[Array[Boolean]] = {
    val gridSize = if (lines.lengthCompare(50) < 0) { 7 } else { 71 }
    val numBytes = if (lines.lengthCompare(50) < 0) { 12 } else { 1024 }
    val grid = Array.fill(gridSize)(Array.fill(gridSize)(true))
    lines.take(numBytes).foreach { line =>
      val Array(x, y) = line.split(",")
      grid(y.trim.toInt)(x.trim.toInt) = false
    }
    grid
  }

  private def findMinPath(map: Array[Array[Boolean]], startI: Int, startJ: Int, endI: Int, endJ: Int): Long = {
    val paths = new java.util.LinkedList[Path]()
    val minCostPerLocation = Array.fill(map.length)(Array.fill(map.head.length)(Int.MaxValue))
    paths.addFirst(Path(startI, startJ))
    while (!paths.isEmpty) {
      val path = paths.removeFirst()
      if (path.steps.lengthCompare(minCostPerLocation(path.i)(path.j)) < 0) {
        minCostPerLocation(path.i)(path.j) = path.steps.length
        if (path.i != endI || path.j != endJ) {
          if (path.i > 0 && map(path.i - 1)(path.j)) {
            path.move(-1, 0).foreach(paths.addFirst)
          }
          if (path.i < map.length - 1 && map(path.i + 1)(path.j)) {
            path.move(1, 0).foreach(paths.addFirst)
          }
          if (path.j < map(path.i).length - 1 && map(path.i)(path.j + 1)) {
            path.move(0, 1).foreach(paths.addFirst)
          }
          if (path.j > 0 && map(path.i)(path.j - 1)) {
            path.move(0, -1).foreach(paths.addFirst)
          }
        }
      }
    }
    minCostPerLocation.last.last - 1 // don't count starting point as a step
  }

  private def findAnyPath(map: Array[Array[Boolean]], startI: Int, startJ: Int, endI: Int, endJ: Int): Boolean = {
    val paths = new java.util.LinkedList[Path]()
    paths.addFirst(Path(startI, startJ))
    val visited = new java.util.HashSet[(Int, Int)]()
    visited.add(startI -> startJ)
    while (!paths.isEmpty) {
      val path = paths.removeFirst()
      if ((path.i == endI - 1 && path.j == endJ) || (path.i == endI && path.j == endJ - 1)) {
        return true
      }
      if (visited.add((path.i + 1, path.j)) && path.i < map.length - 1 && map(path.i + 1)(path.j)) {
        path.move(1, 0).foreach(paths.addFirst)
      }
      if (visited.add((path.i, path.j + 1)) && path.j < map(path.i).length - 1 && map(path.i)(path.j + 1)) {
        path.move(0, 1).foreach(paths.addFirst)
      }
      if (visited.add((path.i - 1, path.j)) && path.i > 0 && map(path.i - 1)(path.j)) {
        path.move(-1, 0).foreach(paths.addFirst)
      }
      if (visited.add((path.i, path.j - 1)) && path.j > 0 && map(path.i)(path.j - 1)) {
        path.move(0, -1).foreach(paths.addFirst)
      }
    }
    false
  }

  case class Path(i: Int, j: Int, steps: Seq[(Int, Int)]) {
    def move(deltaI: Int, deltaJ: Int): Option[Path] = {
      val dest = (i + deltaI, j + deltaJ)
      if (steps.contains(dest)) { None } else {
        Some(Path(i + deltaI, j + deltaJ, steps :+ dest))
      }
    }
  }

  object Path {
    def apply(i: Int, j: Int): Path = Path(i, j, Seq(i -> j))
  }
}
