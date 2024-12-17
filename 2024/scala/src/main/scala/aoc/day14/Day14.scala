package aoc.day14

import aoc.Aoc

import scala.collection.mutable.ArrayBuffer

object Day14 extends Aoc {

  override def part1(lines: List[String]): String = {
    val robots = lines.map(Robot.apply)
    val grid = if (robots.lengthCompare(20) < 0) {
      // sample
      Grid(11, 7, robots)
    } else {
      Grid(101, 103, robots)
    }
    val elapsed = grid.moveRobots(100)
    val quadrants = elapsed.robotsPerQuadrant
    val score = quadrants.reduceLeft { case (left, right) => left * right }
    score.toString
  }

  override def part2(lines: List[String]): String = {
    val robots = lines.map(Robot.apply)
    if (robots.lengthCompare(20) < 0) {
      // sample
      ""
    } else {
      val grids = Range(1, 10000).foldLeft(Seq(0 -> Grid(101, 103, robots))) { case (grids, i) => grids :+ i -> grids.last._2.moveRobots(1) }
      val (i, grid) = grids.minBy(_._2.adjacencyScore)
      grid.printGrid()
      i.toString
    }

  }

  case class Robot(px: Int, py: Int, vx: Int, vy: Int) {
    def move(secs: Int, gridX: Int, gridY: Int): Robot = {
      val x = (px + vx * secs) % gridX
      val fx = if (x >= 0) { x } else { gridX + x }
      val y = (py + vy * secs) % gridY
      val fy = if (y >= 0) { y } else { gridY + y }
      Robot(fx, fy, vx, vy)
    }
  }

  case class Grid(x: Int, y: Int, robots: Seq[Robot]) {

    def moveRobots(secs: Int): Grid = Grid(x, y, robots.map(_.move(secs, x, y)))

    def robotsPerQuadrant: Seq[Int] = {
      // note: assumes x and y are odd
      val midX = x / 2
      val midY = y / 2
      var c0, c1, c2, c3 = 0
      robots.foreach { r =>
        if (r.px < midX && r.py < midY) {
          c0 += 1
        } else if (r.px < midX && r.py > midY) {
          c3 += 1
        } else if (r.px > midX && r.py < midY) {
          c1 += 1
        } else if (r.px > midX && r.py > midY) {
          c2 += 1
        }
      }
      Seq(c0, c1, c2, c3)
    }

    lazy val adjacencyScore: Int = {
      val grid = Array.fill(x)(Array.fill(y)(false))
      robots.foreach(r => grid(r.px)(r.py) = true)
      var adjacent = 0
      var j = 1
      while (j < y - 1) {
        var i = 1
        while (i < x - 1) {
          if (grid(i)(j) &&
            grid(i-1)(j) || grid(i+1)(j) || grid(i)(j-1) || grid(i)(j+1) ||
            grid(i-1)(j-1) || grid(i+1)(j-1) || grid(i-1)(j+1) || grid(i+1)(j+1)) {
            adjacent += 1
          }
          i += 1
        }
        j += 1
      }
      adjacent
    }

//    def runs(): Int = {
//      val grid = Array.fill(x)(Array.fill(y)(false))
//      robots.foreach(r => grid(r.px)(r.py) = true)
//
//      var hasRobot = false
//      var runs = 0
//      var j = 0
//      while (j < y) {
//        var i = 0
//        while (i < x) {
//          if (hasRobot != grid(i)(j)) {
//            hasRobot = !hasRobot
//            runs += 1
//          }
//          i += 1
//        }
//        j += 1
//      }
//      runs
//    }
//
//    def mightBeATree(): Boolean = {
//      val midX = x / 2
//      val midY = y / 2
//      def hasRobot(xy: (Int, Int)): Boolean = robots.exists(r => r.px == midX + xy._1 && r.py == midY + xy._2)
//      Seq((0, -1), (0, 0), (0, 1), (-1, 0), (-1, -1), (-1, 1), (1, 0), (1, -1), (1, 1)).count(hasRobot) > 7
//    }

    def printGrid(): Unit = {
      var j = 0
      while (j < y) {
        var i = 0
        while (i < x) {
          val count = robots.count(r => r.px == i && r.py == j)
          print(if (count == 0) '.' else count)
          i += 1
        }
        println
        j += 1
      }
    }
  }

  object Robot {
    private val regex = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".r
    // p=0,4 v=3,-3
    def apply(line: String): Robot = {
      line match {
        case regex(px, py, vx, vy) => new Robot(px.toInt, py.toInt, vx.toInt, vy.toInt)
      }
    }
  }
}
