package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec

class Day9Spec extends UnitSpec {
  behavior of "Day9"

  "part 1" should "not regress" in {
    val program = Day9.getProgramFromResource("2019/day9/input.txt")
    Day9(program, LazyList(1L)).head shouldBe 3100786347L
  }

  "part 2" should "not regress" in {
    val program = Day9.getProgramFromResource("2019/day9/input.txt")
    Day9(program, LazyList(2L)).head shouldBe 87023L
  }

  it should "test 1" in {
    val program = List(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99).map(_.toLong)
    Day9(program).toList shouldBe program
  }

  it should "test 2" in {
    val program = List(1102,34915192,34915192,7,4,7,99,0).map(_.toLong)
    Day9(program).head shouldBe 1219070632396864L
  }

  it should "test 3" in {
    val program = List(104,1125899906842624L,99).map(_.toLong)
    Day9(program).head shouldBe 1125899906842624L
  }
}
