package bbstilson.aoc2019

import bbstilsonimplicits.ListImplicits._

import scala.collection.immutable.WrappedString

object Day4 {

  def main(args: Array[String]): Unit = {
    println(part1(254032 to 789860))
    println(part2(254032 to 789860))
  }

  def part1(range: Range): Int = range.filter(isValid).size

  def part2(range: Range): Int = range.filter(isValid2).size

  def isValid(x: Int): Boolean = {
    val slider = x.toString().toSeq.sliding(2).toList
    allIncrease(slider) && slider.exists(s => s.head == s.last)
  }

  def isValid2(x: Int): Boolean = {
    val asStr = x.toString()
    val allInc = allIncrease(asStr.toSeq.sliding(2).toList)
    val hasAdj = asStr.toList.toFrequencyMap.values.find(_ == 2).isDefined
    allInc && hasAdj
  }

  private def allIncrease(xs: List[WrappedString]): Boolean =
    xs.forall(s => s.head.toInt <= s.last.toInt)
}
