package org.bbstilson.aoc2019

import utest._

object Day5Spec extends TestSuite {

  val tests = Tests {

    test("it should not regress") {
      val program = Day7.getProgramFromResource("2019/day5/input.txt")
      assert(Day7(program, LazyList(1)).head == 7566643) // part 1
      assert(Day7(program, LazyList(5)).head == 9265694) // part 2
    }
  }
}
