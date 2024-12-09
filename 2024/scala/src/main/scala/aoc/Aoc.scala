package aoc

import scala.io.{Codec, Source}
import scala.util.Try

trait Aoc {

  def main(args: Array[String]): Unit = {
    doPart1()
    println()
    doPart2()
  }

  private def doPart1(): Unit = {
    println("Part 1")
    println("Sample: " + doPart(loadSample, part1))
    println("Input: " + doPart(loadInput, part1))
  }

  private def doPart2(): Unit = {
    println("Part 2")
    println("Sample: " + doPart(loadSample2, part2))
    println("Input: " + doPart(loadInput, part2))
  }

  private def doPart(source: () => Source, part: List[String] => String): String = {
    val input = source()
    val lines = try { input.getLines().toList } finally {
      input.close()
    }
    val start = System.nanoTime()
    val res = part(lines)
    s"$res in ${(System.nanoTime() - start) / 1000000} ms"
  }

  private def loadSample(): Source = load("sample")

  private def loadSample2(): Source = Try(load("sample2")).getOrElse(load("sample"))

  private def loadInput(): Source = load("input")

  private def load(name: String): Source = Source.fromResource(s"${getClass.getPackageName.replace('.', '/')}/$name")(Codec.UTF8)

  def part1(lines: List[String]): String

  def part2(lines: List[String]): String
}
