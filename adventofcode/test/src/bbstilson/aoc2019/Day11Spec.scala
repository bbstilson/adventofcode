package bbstilson.aoc2019

import utest._

object Day11Spec extends TestSuite {

  val tests = Tests {

    test("part 1 should not regress") {
      val program = IntCode.getProgramFromResource("2019/day11/input.txt")

      assert(Day11.part1(program) == 2418)
    }
  }
}
