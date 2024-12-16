package aoc

import java.net.URL
import scala.io.{Codec, Source}

trait Aoc {

  def main(args: Array[String]): Unit = {
    doPart1()
    println()
    doPart2()
  }

  private def doPart1(): Unit = {
    println("Part 1")
    var i = 1
    doPart(loadSamples, part1).foreach { output =>
      println(s"Sample $i: $output")
      i += 1
    }
    println("Input: " + doPart(Seq(loadInput), part1).head)
  }

  private def doPart2(): Unit = {
    println("Part 2")
    var i = 1
    doPart(loadSamples, part2).foreach { output =>
      println(s"Sample $i: $output")
      i += 1
    }
    println("Input: " + doPart(Seq(loadInput), part2).head)
  }

  private def doPart(sources: Seq[() => Source], part: List[String] => String): Seq[String] = {
    sources.map { source =>
      val input = source()
      val lines = try {
        input.getLines().toList
      } finally {
        input.close()
      }
      val start = System.nanoTime()
      val res = part(lines)
      s"$res in ${(System.nanoTime() - start) / 1000000} ms"
    }
  }

  private def loadSamples: Seq[() => Source] =
    (Iterator.single("") ++ Iterator.tabulate(10)(_.toString))
      .map(s => getClass.getResource(s"sample$s"))
      .filter(_ != null)
      .map(u => () => load(u))
      .toSeq

  private def loadInput(): Source = load(getClass.getResource("input"))

  private def load(resource: URL): Source = Source.fromURL(resource)(Codec.UTF8)

  def part1(lines: List[String]): String

  def part2(lines: List[String]): String
}
