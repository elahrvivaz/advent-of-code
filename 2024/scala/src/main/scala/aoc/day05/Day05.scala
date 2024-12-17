package aoc
package day05

import scala.collection.mutable.ArrayBuffer

object Day05 extends Aoc {

  override def part1(lines: List[String]): Long = {
    val split = lines.indexOf("")
    val rules = lines.take(split).map(Rule.apply)
    val instructions = lines.drop(split + 1).map(_.split(',').map(_.toInt).toList)
    val valid = instructions.filter(i => rules.forall(_.verify(i)))
    valid.map(nums => nums(nums.length / 2)).sum
  }

  override def part2(lines: List[String]): Long = {
    val split = lines.indexOf("")
    val rules = Rules(lines.take(split).map(Rule.apply))
    val instructions = lines.drop(split + 1).map(_.split(',').map(_.toInt).toList)
    val invalid = instructions.filterNot(i => rules.forall(_.verify(i)))
    val fixed = invalid.map(i => fix(i, rules.filter(r => i.contains(r.before) && i.intersect(r.after).nonEmpty)))
    fixed.map(nums => nums(nums.length / 2)).sum
  }

  def fix(inst: Seq[Int], rules: Seq[Rules]): Seq[Int] = {
    val result = ArrayBuffer.empty[Int]
    val remainingRules = scala.collection.mutable.Map(rules.map(r => r.before -> r.after): _*)
    val remaining = scala.collection.mutable.Set(inst: _*)
    while (remaining.nonEmpty) {
      val next = remaining.find(r => !remainingRules.exists(_._2.contains(r))).getOrElse(throw new RuntimeException())
      remaining.remove(next)
      result += next
      remainingRules.remove(next)
    }
    result.toSeq
  }

  case class Rule(before: Int, after: Int) {
    def verify(instruction: Seq[Int]): Boolean = {
      val i = instruction.indexOf(before)
      i == -1 || {
        val j = instruction.indexOf(after)
        j == -1 || j > i
      }
    }
  }

  object Rule {
    def apply(line: String): Rule = {
      val Array(left, right) = line.split('|')
      Rule(left.toInt, right.toInt)
    }
  }

  case class Rules(before: Int, after: Seq[Int]) extends Comparable[Rules] {
    def verify(instruction: Seq[Int]): Boolean = {
      val i = instruction.indexOf(before)
      i == -1 || after.forall { a =>
        val j = instruction.indexOf(a)
        j == -1 || j > i
      }
    }

    override def compareTo(o: Rules): Int =
      if (o.after.contains(before)) { 1 } else if (after.contains(o.before)) { -1 } else { 0 }
  }

  object Rules {
    def apply(rules: Seq[Rule]): Seq[Rules] = {
      rules.groupBy(_.before).toSeq.map { case (before, afters) => Rules(before, afters.map(_.after)) }.toSeq
    }
  }
}
