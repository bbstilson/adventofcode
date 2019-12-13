package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode.IntCode

object Day7 {

  def main(args: Array[String]): Unit = {
    val program = IntCode.getProgramFromResource("2019/day7/input.txt")
    println(part1(program))
    println(part2(program))
  }


  def part1(program: List[Long]): Long = {
    List(0,1,2,3,4).permutations.map { phases =>
      phases.foldLeft(0L) { case (prev, phase) =>
        IntCode(program, LazyList(phase, prev)).head
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
    lazy val a: LazyList[Long] = IntCode(program, phases(0) #:: 0L #:: e)
    lazy val b: LazyList[Long] = IntCode(program, phases(1) #:: a)
    lazy val c: LazyList[Long] = IntCode(program, phases(2) #:: b)
    lazy val d: LazyList[Long] = IntCode(program, phases(3) #:: c)
    lazy val e: LazyList[Long] = IntCode(program, phases(4) #:: d)
    e.last
  }
}
