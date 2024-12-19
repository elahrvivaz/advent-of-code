package aoc.day19

import aoc.Aoc

import scala.collection.mutable.ArrayBuffer

object Day19 extends Aoc {

  override def part1(lines: List[String]): Long = {
    return 0
    val towels = lines.head.split(",").map(_.trim)
    val designs = lines.drop(2)
    val matcher = DesignMatcher(towels)
    designs.count { d => /*println("trying: " + d);*/ matcher.canMakeDesign(d) }
  }

  override def part2(lines: List[String]): Long = {
    val towels = lines.head.split(",").map(_.trim)
    val designs = lines.drop(2)
    if (designs.length > 30) {
      return 0
    }
    val matcher = DesignCounter(towels)
    designs.map { d => println("trying: " + d); val s = matcher.countPatterns(d); println(d + " total: " + matcher.patterns.getOrElse(d, 0)); s }.sum
  }

  case class DesignMatcher(towels: Seq[String]) {

    private val patterns = ArrayBuffer.fill(100)(scala.collection.mutable.Set.empty[String])
    private val nonMatches = ArrayBuffer.fill(100)(scala.collection.mutable.Set.empty[String])

    towels.foreach(towel => patterns(towel.length) += towel)

    def canMakeDesign(design: String): Boolean = {
      if (patterns(design.length).contains(design)) {
        true
      } else if (design.length == 1) {
        false
      } else if (nonMatches(design.length).contains(design)) {
        false
      } else {
        var numSplits = 1
        while (numSplits < design.length) {
          val splits = Array(0) ++ Array.tabulate(numSplits)(_ + 1) ++ Array(design.length)
          while (splits(0) == 0) {
            if (splits.sliding(2).forall { case Array(s, e) => canMakeDesign(design.substring(s, e)) }) {
              patterns(design.length) += design
              return true
            }
            var splitToIncrement = splits.length - 2
            while (splitToIncrement > 0 && splits(splitToIncrement) + 1 == splits(splitToIncrement + 1)) {
              splitToIncrement -= 1
            }
            splits(splitToIncrement) = splits(splitToIncrement) + 1
          }
          numSplits += 1
        }
        nonMatches(design.length) += design
        false
      }
    }
  }


  case class DesignCounter(towels: Seq[String]) {

    val patterns = scala.collection.mutable.Map.empty[String, Int]

    towels.foreach(towel => patterns(towel) = 1)

    def countPatterns(design: String): Int = {
      println(s"$design counting")
      if (patterns.contains(design)) {
        println(s"cached: ${patterns(design)}")
        patterns(design)
      } else if (design.length == 1) {
        0
      } else {
        var numSplits = 1
        while (numSplits < design.length) {
          val splits = Array(0) ++ Array.tabulate(numSplits)(_ + 1) ++ Array(design.length)
          while (splits(0) == 0) {
            println(s"$design splits: ${splits.toSeq}")
            val count = splits.sliding(2).foldLeft(1) { case (p, Array(s, e)) =>
              if (p == 0) { 0 } else { countPatterns(design.substring(s, e)) * p }
            }
            println(s"$design count: $count")
            patterns(design) = patterns.getOrElse(design, 0) + count
            println(s"$design sum: ${patterns(design)}")
            var splitToIncrement = splits.length - 2
            while (splitToIncrement > 0 && splits(splitToIncrement) + 1 == splits(splitToIncrement + 1)) {
              splitToIncrement -= 1
            }
            splits(splitToIncrement) = splits(splitToIncrement) + 1
          }
          numSplits += 1
        }
        patterns.getOrElse(design, 0)
      }
    }
  }

}
