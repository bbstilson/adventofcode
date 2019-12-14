package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec
import org.bbstilson.aoc2019.intcode._

class Day13Spec extends UnitSpec {
  behavior of "Day13"

  val program = IntCodeHelpers.getProgramFromResource("2019/day13/input.txt")

  "part 1" should "not regress" in {
    Day13.part1(program) shouldBe 296L
  }

}
