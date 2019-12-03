package org.bbstilson.aoc2017

object Day1 {
  def main(args: Array[String]): Unit = {
    val xs = parseInput()

    println(part1(xs))
    println(part2(xs))
  }

  def part1(xs: List[Int]): Int = {
    val headLast = if (xs.head == xs.last) xs.head else 0
    val center = xs
      .sliding(2)
      .collect { case x :: y :: Nil if x == y => x }
      .sum

    headLast + center
  }

  def part2(xs: List[Int]): Int = {
    val size = xs.size
    val half = size / 2

    val xsWithIndex = xs.zipWithIndex
    val idxMap = xsWithIndex.map(_.swap).toMap

    xsWithIndex
      .collect { case (x, idx) if (idxMap((idx + half) % size) == x) => x }
      .sum
  }

  private def parseInput(): List[Int] = io.Source
    .fromResource("2017/day1/input.txt")
    .getLines.toList.head
    .split("").toList.map(_.toInt)
}
