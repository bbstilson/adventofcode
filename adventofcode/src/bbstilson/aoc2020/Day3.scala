package bbstilson.aoc2020

import aocd.Problem

object Day3 extends Problem(2020, 3) {

  def run(input: List[String]): Unit = {
    println(part1(input, 3, 1))
    println(part2(input))
  }

  private def part1(input: List[String], rightStepSize: Int, downStepSize: Int): Long = {
    (0 until input.size by downStepSize)
      .zip((0 until (input.size * rightStepSize) by rightStepSize))
      .drop(1)
      .toVector
      .map { case (level, right) =>
        val row = input.drop(level).head
        row(right % input.head.length())
      }
      .count(_ == '#')
      .toLong
  }

  def part2(input: List[String]): Long = {
    val combos = List(
      (1 -> 1),
      (3 -> 1),
      (5 -> 1),
      (7 -> 1),
      (1 -> 2)
    )

    combos.map { case (right, down) => part1(input, right, down) }.product
  }
}
