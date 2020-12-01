package org.bbstilson.aoc2019

object Day1 {

  def main(args: Array[String]): Unit = {
    val masses = parseInput()
    println(part1(masses))
    println(part2(masses))
  }

  def part1(masses: List[Int]): Int = masses.map(m => (m / 3) - 2).sum

  def part2(masses: List[Int]): Int = masses.map(calcFuel).sum

  private def calcFuel(mass: Int): Int = (mass / 3) - 2 match {
    case m if m <= 0 => 0
    case m           => m + calcFuel(m)
  }

  private def parseInput(): List[Int] = io.Source
    .fromResource("2019/day1/input.txt")
    .getLines()
    .map(_.toInt)
    .toList
}
