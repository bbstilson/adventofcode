package org.bbstilson.aoc2019

object Day7 {
  import Day5._

  def main(args: Array[String]): Unit = {
    val input = parseInput("2019/day7/input.txt")
    val memory = mkMemory(input)

    println(part1(memory))
  }

  def part1(memory: Map[Int, Int]): Int = {
    List(0,1,2,3,4).permutations.map { phaseSettings =>
      phaseSettings.foldLeft(0) { case (prevOutput, phase) =>
        run(0, memory, IOState(List(phase, prevOutput))).output.head
      }
    }.max
  }
}
