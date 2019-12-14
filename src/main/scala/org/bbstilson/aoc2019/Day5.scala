package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode._

object Day5 {

  val program = IntCodeHelpers.getProgramFromResource("2019/day5/input.txt")

  def main(args: Array[String]): Unit = {
    println(part1(program))
    println(part2(program))
  }

  def part1(program: List[Long]): Long = IntCode(program, LazyList(1)).last

  def part2(program: List[Long]): Long = IntCode(program, LazyList(5)).last
}
