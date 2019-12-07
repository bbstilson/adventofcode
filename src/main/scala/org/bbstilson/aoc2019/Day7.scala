package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode.IntCodeComputer

object Day7 {
  def main(args: Array[String]): Unit = {
    val program = parseInput("2019/day7/input.txt")
    println(part1(program))
    println(part2(program))
  }


  def part1(program: List[Int]): Int = {
    List(0,1,2,3,4).permutations.map { phases =>
      phases.foldLeft(0) { case (prev, phase) =>
        IntCodeComputer(program, LazyList(phase, prev)).head
      }
    }.max
  }

  def part2(program: List[Int]): Int = {
    List(5,6,7,8,9)
      .permutations
      .map { ps => lazyCompute(program, ps) }
      .max
  }

  def lazyCompute(program: List[Int], phases: List[Int]): Int = {
    lazy val a: LazyList[Int] = IntCodeComputer(program, phases(0) #:: 0 #:: e)
    lazy val b: LazyList[Int] = IntCodeComputer(program, phases(1) #:: a)
    lazy val c: LazyList[Int] = IntCodeComputer(program, phases(2) #:: b)
    lazy val d: LazyList[Int] = IntCodeComputer(program, phases(3) #:: c)
    lazy val e: LazyList[Int] = IntCodeComputer(program, phases(4) #:: d)
    e.last
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
