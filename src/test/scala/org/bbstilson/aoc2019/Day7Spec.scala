package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec
import org.bbstilson.aoc2019.intcode.IntCodeComputer

class Day7Spec extends UnitSpec {
  behavior of "Day7"

  it should "not regress" in {
    val program = Day7.parseInput("2019/day7/input.txt")
    val testProgram = List(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0)
    Day7.part1(program) shouldBe 87138 // part 1
    Day7.part1Test(testProgram) shouldBe 43210 // part 1 test
  }
}
