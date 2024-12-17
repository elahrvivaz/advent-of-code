package aoc.day10

import aoc.Aoc

import scala.collection.mutable.ArrayBuffer

object Day10 extends Aoc {

  override def part1(lines: List[String]): Long = {
    val map = lines.map(_.toCharArray.map(_.toInt - 48)).toArray
    val trailheads = findTrailheads(map)
    val trails = ArrayBuffer(trailheads.map(Seq(_)): _*)
    val scores = scala.collection.mutable.Set.empty[(Int, Int, Int, Int)]
    while (trails.nonEmpty) {
      val trail = trails.remove(0)
      val next = trail.length
//      println(trail -> next)
      if (next == 10) {
        scores += ((trail.head._1, trail.head._2, trail.last._1, trail.last._2))
      } else {
        val (i, j) = trail.last
        if (i > 0 && map(i - 1)(j) == next) {
          trails += (trail :+ (i - 1, j))
        }
        if (i < map.length - 1 && map(i + 1)(j) == next) {
          trails += (trail :+ (i + 1, j))
        }
        if (j > 0 && map(i)(j - 1) == next) {
          trails += (trail :+ (i, j - 1))
        }
        if (j < map(i).length - 1 && map(i)(j + 1) == next) {
          trails += (trail :+ (i, j + 1))
        }
      }
    }
    scores.size
  }

  override def part2(lines: List[String]): Long = {
    val map = lines.map(_.toCharArray.map(_.toInt - 48)).toArray
    val trailheads = findTrailheads(map)
    val trails = ArrayBuffer(trailheads.map(Seq(_)): _*)
    var count = 0
    while (trails.nonEmpty) {
      val trail = trails.remove(0)
      val next = trail.length
      //      println(trail -> next)
      if (next == 10) {
        count += 1
      } else {
        val (i, j) = trail.last
        if (i > 0 && map(i - 1)(j) == next) {
          trails += (trail :+ (i - 1, j))
        }
        if (i < map.length - 1 && map(i + 1)(j) == next) {
          trails += (trail :+ (i + 1, j))
        }
        if (j > 0 && map(i)(j - 1) == next) {
          trails += (trail :+ (i, j - 1))
        }
        if (j < map(i).length - 1 && map(i)(j + 1) == next) {
          trails += (trail :+ (i, j + 1))
        }
      }
    }
    count
  }

  private def findTrailheads(map: Array[Array[Int]]): Seq[(Int, Int)] = {
    val trailheads = ArrayBuffer.empty[(Int, Int)]
    var i = 0
    while (i < map.length) {
      var j = 0
      while (j < map(i).length) {
        if (map(i)(j) == 0) {
          trailheads += i -> j
        }
        j += 1
      }
      i += 1
    }
    trailheads.toSeq
  }
}
