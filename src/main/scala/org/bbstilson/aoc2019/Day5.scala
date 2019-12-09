package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode.IntCodeComputer

object Day5 {

  def main(args: Array[String]): Unit = {
    val program = IntCodeComputer.getProgramFromResource("2019/day5/input.txt")
    println(IntCodeComputer(program, LazyList(1)).head) // part 1
    println(IntCodeComputer(program, LazyList(5)).head) // part 2
  }
}
