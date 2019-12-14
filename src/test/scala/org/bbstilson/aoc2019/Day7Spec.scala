package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec
import org.bbstilson.aoc2019.intcode._

class Day7Spec extends UnitSpec {
  behavior of "Day7"

  "part 1" should "not regress" in {
    val program = IntCodeHelpers.getProgramFromResource("2019/day7/input.txt")
    Day7.part1(program) shouldBe 87138L
  }

  "part 2" should "not regress" in {
    val program = IntCodeHelpers.getProgramFromResource("2019/day7/input.txt")
    Day7.part2(program) shouldBe 17279674L
  }
}
