package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode.IntCodeComputer

object Day7 {
  def main(args: Array[String]): Unit = {
    val program = IntCodeComputer.getProgramFromResource("2019/day7/input.txt")
    println(part1(program))
    // println(part2(program)) This doesn't work :(
  }


  def part1(program: List[Long]): Long = {
    List(0L,1L,2L,3L,4L).permutations.map { phases =>
      phases.foldLeft(0L) { case (prev, phase) =>
        IntCodeComputer(program, LazyList(phase, prev)).head
      }
    }.max
  }

  def part2(program: List[Long]): Long = {
    List(5L,6L,7L,8L,9L)
      .permutations
      .map { ps => lazyCompute(program, ps) }
      .max
  }

  def lazyCompute(program: List[Long], phases: List[Long]): Long = {
    lazy val a: LazyList[Long] = IntCodeComputer(program, phases(0) #:: 0L #:: e)
    lazy val b: LazyList[Long] = IntCodeComputer(program, phases(1) #:: a)
    lazy val c: LazyList[Long] = IntCodeComputer(program, phases(2) #:: b)
    lazy val d: LazyList[Long] = IntCodeComputer(program, phases(3) #:: c)
    lazy val e: LazyList[Long] = IntCodeComputer(program, phases(4) #:: d)
    e.last
  }
}
