package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec
import org.bbstilson.aoc2019.intcode._

class Day5Spec extends UnitSpec {
  behavior of "Day5"

  val program = IntCodeHelpers.getProgramFromResource("2019/day5/input.txt")

  "part 1" should "not regress" in {
    Day5.part1(program) shouldBe 7566643L
  }

  "part 2" should "not regress" in {
    Day5.part2(program) shouldBe 9265694L
  }
}
