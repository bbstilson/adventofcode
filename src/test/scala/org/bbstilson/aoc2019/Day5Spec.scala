package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec

class Day5Spec extends UnitSpec {
  behavior of "Day5"

  it should "not regress" in {
    val program = Day7.getProgramFromResource("2019/day5/input.txt")
    Day7(program, LazyList(1)).head shouldBe 7566643 // part 1
    Day7(program, LazyList(5)).head shouldBe 9265694 // part 2
  }
}
