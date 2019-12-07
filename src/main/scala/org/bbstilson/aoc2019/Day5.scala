package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode.IntCodeComputer

object Day5 {

  def main(args: Array[String]): Unit = {
    val program = parseInput("2019/day5/input.txt")
    println(IntCodeComputer(program, LazyList(1)).head == 7566643) // part 1
    println(IntCodeComputer(program, LazyList(5)).head == 9265694) // part 2
  }

  def parseInput(resource: String): List[Int] = {
    io.Source
      .fromResource(resource)
      .getLines.toList
      .head
      .split(",")
      .map(_.toInt)
      .toList
  }
}
