package org.bbstilson.aoc2019

import scala.collection.immutable.WrappedString

/*
Two adjacent digits are the same (like 22 in 122345).
Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or 135679).
*/
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
    val freqMap = asStr.groupMapReduce(identity)(_ => 1)(_ + _)
    val hasAdjPair = freqMap.values.find(_ == 2).isDefined
    allIncrease(asStr.toSeq.sliding(2).toList) && hasAdjPair
  }

  private def allIncrease(xs: List[WrappedString]): Boolean = xs.forall(s => s.head.toInt <= s.last.toInt)
}
