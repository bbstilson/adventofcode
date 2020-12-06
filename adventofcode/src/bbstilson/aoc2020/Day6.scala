package bbstilson.aoc2020

import bbstilson.implicits.ListImplicits._

object Day6 extends aocd.Problem(2020, 6) {

  def run(input: List[String]): Unit = {
    println(part1(input))
    println(part2(input))
  }

  private def part1(input: List[String]): Int = {
    val sizes = for {
      group <- input.groupedByNewlines
      answers = group.map(_.toCharArray().toSet).reduce(_ ++ _)
    } yield answers.size

    sizes.sum
  }

  private def part2(input: List[String]): Int = {
    val sizes = for {
      group <- input.groupedByNewlines
      answers = group.map(_.toCharArray().toSet)
      uniqAnswers = answers.reduce(_ intersect _)
    } yield uniqAnswers.size

    sizes.sum
  }
}
