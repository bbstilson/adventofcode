package org.bbstilson.aoc2019

object Day5 {

  def main(args: Array[String]): Unit = {
    val program = Day7.getProgramFromResource("2019/day5/input.txt")
    println(Day7(program, LazyList(1)).head) // part 1
    println(Day7(program, LazyList(5)).head) // part 2
  }
}
