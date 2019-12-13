package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode.IntCode

object Day5 {

  def main(args: Array[String]): Unit = {
    val program = IntCode.getProgramFromResource("2019/day5/input.txt")
    println(IntCode(program, LazyList(1)).head) // part 1
    println(IntCode(program, LazyList(5)).head) // part 2
  }
}
