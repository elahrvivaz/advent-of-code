import scala.io.{Codec, Source}
import scala.util.Try

package object aoc {

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

    private def doPart(source: () => Source, part: Iterator[String] => String): String = {
      val input = source()
      try { part(input.getLines()) } finally {
        input.close()
      }
    }

    private def loadSample(): Source = load("sample")
    private def loadSample2(): Source = Try(load("sample2")).getOrElse(load("sample"))
    private def loadInput(): Source = load("input")
    private def load(name: String): Source = Source.fromResource(s"${getClass.getPackageName.replace('.', '/')}/$name")(Codec.UTF8)

    def part1(lines: Iterator[String]): String
    def part2(lines: Iterator[String]): String
  }
}
