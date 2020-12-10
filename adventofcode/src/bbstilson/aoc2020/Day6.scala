package bbstilson.aoc2020

import bbstilson.implicits.ListImplicits._

object Day6 extends aocd.Problem(2020, 6) {

  def run(input: List[String]): Unit = {
    val groups = input.groupedBy(_ == "")
    println(part1(groups))
    println(part2(groups))
  }

  private def part1(groups: List[List[String]]): Int = {
    val sizes = for {
      group <- groups
      answers = group.map(_.toCharArray().toSet).reduce(_ ++ _)
    } yield answers.size

    sizes.sum
  }

  private def part2(groups: List[List[String]]): Int = {
    val sizes = for {
      group <- groups
      answers = group.map(_.toCharArray().toSet)
      uniqAnswers = answers.reduce(_ intersect _)
    } yield uniqAnswers.size

    sizes.sum
  }
}
