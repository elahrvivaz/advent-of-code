package aoc.day16

import aoc.Aoc

import scala.collection.mutable.ArrayBuffer

object Day16 extends Aoc {

  import Direction.Direction

  override def part1(lines: List[String]): Long = {
    val (map, posI, posJ, endI, endJ) = parseInput(lines)
    findMinPath(map, posI, posJ, endI, endJ)
  }

  override def part2(lines: List[String]): Long = {
    val (map, posI, posJ, endI, endJ) = parseInput(lines)
    findBestTiles(map, posI, posJ, endI, endJ)
  }

  private def parseInput(lines: List[String]): (Array[Array[Boolean]], Int, Int, Int, Int) = {
    // map - false == wall, true == corridor
    val map: Array[Array[Boolean]] = Array.fill(lines.length)(Array.fill(lines.head.length)(false))

    // local state
    var posI = -1
    var posJ = -1

    var endI = -1
    var endJ = -1

    var i = 0
    while (i < map.length) {
      var j = 0
      while (j < map(i).length) {
        lines(i).charAt(j) match {
          case '.' => map(i)(j) = true
          case '#' => // already false
          case 'S' => map(i)(j) = true; posI = i; posJ = j
          case 'E' => map(i)(j) = true; endI = i; endJ = j
        }
        j += 1
      }
      i += 1
    }

    require(posI != -1 && posJ != -1 && endI != -1 && endJ != -1)

    (map, posI, posJ, endI, endJ)
  }

  private def findMinPath(map: Array[Array[Boolean]], startI: Int, startJ: Int, endI: Int, endJ: Int): Long = {
    val paths = new java.util.LinkedList[Path]()
    val minCostPerLocation = Array.fill(map.length)(Array.fill(map.head.length)(Long.MaxValue))
    paths.addFirst(Path(startI, startJ, Set.empty, Direction.E, 0))
    var cost = Long.MaxValue
    while (!paths.isEmpty) {
      val path = paths.removeFirst()
      if (path.i == endI && path.j == endJ) {
        if (cost > path.cost) {
          cost = path.cost
        }
      } else if (path.cost < cost && path.cost - 1000 < minCostPerLocation(path.i)(path.j)) {
        minCostPerLocation(path.i)(path.j) = path.cost
        if (path.i > 0 && map(path.i - 1)(path.j)) {
          path.north.foreach(paths.addFirst)
        }
        if (path.i < map.length - 1 && map(path.i + 1)(path.j)) {
          path.south.foreach(paths.addFirst)
        }
        if (path.j < map(path.i).length - 1 && map(path.i)(path.j + 1)) {
          path.east.foreach(paths.addFirst)
        }
        if (path.j > 0 && map(path.i)(path.j - 1)) {
          path.west.foreach(paths.addFirst)
        }
      }
    }
    cost
  }

  private def findBestTiles(map: Array[Array[Boolean]], startI: Int, startJ: Int, endI: Int, endJ: Int): Long = {
    val paths = new java.util.LinkedList[Path]()
    val minCostPerLocation = Array.fill(map.length)(Array.fill(map.head.length)(Long.MaxValue))
    paths.addFirst(Path(startI, startJ, Set.empty, Direction.E, 0))
    val bestPaths = ArrayBuffer.empty[Path]
    var cost = Long.MaxValue
    while (!paths.isEmpty) {
      val path = paths.removeFirst()
      if (path.i == endI && path.j == endJ) {
        if (cost > path.cost) {
          cost = path.cost
          bestPaths.clear()
          bestPaths += path
        } else if (cost == path.cost) {
          bestPaths += path
        }
      } else if (path.cost < cost && path.cost - 1000 <= minCostPerLocation(path.i)(path.j)) {
        minCostPerLocation(path.i)(path.j) = path.cost
        if (path.i > 0 && map(path.i - 1)(path.j)) {
          path.north.foreach(paths.addFirst)
        }
        if (path.i < map.length - 1 && map(path.i + 1)(path.j)) {
          path.south.foreach(paths.addFirst)
        }
        if (path.j < map(path.i).length - 1 && map(path.i)(path.j + 1)) {
          path.east.foreach(paths.addFirst)
        }
        if (path.j > 0 && map(path.i)(path.j - 1)) {
          path.west.foreach(paths.addFirst)
        }
      }
    }
    bestPaths.flatMap(_.steps).distinct.size + 1 // plus one for end pos
  }

  object Direction extends Enumeration {

    type Direction = Value
    val N, E, S, W = Value

    def cost(current: Direction, turn: Direction): Int = {
      if (current == turn) { 0 } else { 1000 } // we never turn more than 90 degrees b/c we don't backtrack
    }
  }

  case class Path(i: Int, j: Int, steps: Set[(Int, Int)], dir: Direction, cost: Long) {
    def north: Option[Path] = {
      if (dir == Direction.S || steps.contains(i-1 -> j)) { None } else {
        Some(Path(i-1, j, steps + (i -> j), Direction.N, cost + turnCost(Direction.N) + 1))
      }
    }

    def south: Option[Path] = {
      if (dir == Direction.N || steps.contains(i+1 -> j)) { None } else {
        Some(Path(i+1, j, steps + (i -> j), Direction.S, cost + turnCost(Direction.S) + 1))
      }
    }

    def east: Option[Path] = {
      if (dir == Direction.W || steps.contains(i -> (j+1))) { None } else {
        Some(Path(i, j+1, steps + (i -> j), Direction.E, cost + turnCost(Direction.E) + 1))
      }
    }

    def west: Option[Path] = {
      if (dir == Direction.E || steps.contains(i -> (j-1))) { None } else {
        Some(Path(i, j-1, steps + (i -> j), Direction.W, cost + turnCost(Direction.W) + 1))
      }
    }

    private def turnCost(turn: Direction): Int =
      if (dir == turn) { 0 } else { 1000 } // we never turn more than 90 degrees b/c we don't backtrack
  }
}
