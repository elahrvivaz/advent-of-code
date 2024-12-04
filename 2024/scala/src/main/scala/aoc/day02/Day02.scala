package aoc
package day02

object Day02 extends Aoc {

  override def part1(lines: Iterator[String]): String =
    lines.count(line => check(line.split(" +").map(_.toInt))).toString

  override def part2(lines: Iterator[String]): String = {
    lines.count { line =>
      val nums = line.split(" +").map(_.toInt)
      val removed = Array.ofDim[Int](nums.length - 1)
      check(nums) || Range(0, nums.length).exists { i =>
        System.arraycopy(nums, 0, removed, 0, i)
        System.arraycopy(nums, i + 1, removed, i, (nums.length - 1) - i)
        check(removed)
      }
    }.toString
  }

  private def check(in: Seq[Int]): Boolean = {
    var asc: java.lang.Boolean = null
    in.sliding(2).forall { case Seq(n1, n2) =>
      if (n1 == n2) {
        false
      } else if (n1 > n2) {
        (asc == null || asc == false) && {
          asc = false;
          n1 - n2 < 4
        }
      } else {
        (asc == null || asc == true) && {
          asc = true;
          n2 - n1 < 4
        }
      }
    }
  }
}
