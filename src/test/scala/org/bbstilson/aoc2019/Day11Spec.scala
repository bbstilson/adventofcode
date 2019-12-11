package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec

class Day11Spec extends UnitSpec {
  behavior of "Day11"

  "part 1" should "not regress" in {
    val program = IntCode.getProgramFromResource("2019/day11/input.txt")

    Day11.part1(program) shouldBe 2418
  }

}
