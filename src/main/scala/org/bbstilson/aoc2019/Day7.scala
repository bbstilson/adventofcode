package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode.IntCodeComputer

object Day7 {
  def main(args: Array[String]): Unit = {
    val program = parseInput("2019/day7/input.txt")
    println(part1(program))
  }


  def part1(program: List[Int]): Int = {
    List(0,1,2,3,4).permutations.map { phaseSettings =>
      phaseSettings.foldLeft(0) { case (prev, phase) =>
        IntCodeComputer(program, LazyList(phase, prev)).head
      }
    }.max
  }

  def part1Test(program: List[Int]): Int = {
    List(4,3,2,1,0).foldLeft(0) { case (prev, phase) =>
      IntCodeComputer(program, LazyList(phase, prev)).head
    }
  }

  def parseInput(resource: String): List[Int] = {
    io.Source
      .fromResource(resource)
      .getLines.toList
      .head.split(",")
      .map(_.toInt)
      .toList
  }

}
