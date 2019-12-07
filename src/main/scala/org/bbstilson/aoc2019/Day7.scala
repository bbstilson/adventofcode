package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode.IntCodeComputer

object Day7 {
  def main(args: Array[String]): Unit = {
    val program = parseInput("2019/day7/input.txt")
    val testProgram = List(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0)

    if (part1(program) != 87138) {
      throw new Error("WOAH STOP")
    }

    if (part1Test(testProgram) != 43210) {
      throw new Error("WOAH STOP")
    }

    // part2(mkMemory(input)).foreach(d => println(s"found $d"))
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

  // def part2(program: List[Int]): LazyList[Int] = {
  //   def a: LazyList[Int] = outputs(ProgramState(0, memory, 9 #:: 0 #:: e))
  //   def b: LazyList[Int] = outputs(ProgramState(0, memory, 8 #:: a))
  //   def c: LazyList[Int] = outputs(ProgramState(0, memory, 7 #:: b))
  //   def d: LazyList[Int] = outputs(ProgramState(0, memory, 6 #:: c))
  //   def e: LazyList[Int] = outputs(ProgramState(0, memory, 5 #:: d))
  //   e
  // }

  def parseInput(resource: String): List[Int] = {
    io.Source
      .fromResource(resource)
      .getLines.toList
      .head.split(",")
      .map(_.toInt)
      .toList
  }

}
