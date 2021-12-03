package bbstilson.aoc2021

import bbstilson.implicits.ListImplicits._

object Day3 extends aocd.Problem(2021, 3) {
  def run(input: List[String]): Unit = {
    val xs = input.map(_.split("").map(_.toInt).toList).toList

    part1(xs)
    part2(xs)

    ()
  }

  def part1(xs: List[List[Int]]): Int = part1 {
    val rotated = xs.transpose
    val gamma = Integer.parseInt(rotated.map(mostCommonBit).mkString, 2)
    val epsilon = Integer.parseInt(rotated.map(leastCommonBit).mkString, 2)

    gamma * epsilon
  }

  def part2(xs: List[List[Int]]): Int = part2 {
    def helper(xs: List[List[Int]], idx: Int, f: List[Int] => Int): Int = {
      val rotated = xs.transpose
      val bit = f(rotated(idx))

      xs.filter(_(idx) == bit) match {
        case Nil         => -1 // can this happen?
        case head :: Nil => Integer.parseInt(head.mkString, 2)
        case next        => helper(next, idx + 1, f)
      }
    }

    val o2 = helper(xs, 0, mostCommonBit)
    val co2 = helper(xs, 0, leastCommonBit)

    o2 * co2
  }

  private def counts(xs: List[Int], most: Boolean): Int = {
    val counts = xs.toFrequencyMap
    val mostCommon = if (counts.getOrElse(1, 0) >= counts.getOrElse(0, 0)) 1 else 0
    if (most) mostCommon else (1 - mostCommon)
  }

  private def mostCommonBit(xs: List[Int]): Int = counts(xs, true)
  private def leastCommonBit(xs: List[Int]): Int = counts(xs, false)
}
