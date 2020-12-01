package bbstilson.aoc2019

import utest._

object Day9Spec extends TestSuite {

  val tests = Tests {
    test("part 1 should not regress") {
      val program = Day9.getProgramFromResource("2019/day9/input.txt")
      assert(Day9(program, LazyList(1L)).head == 3100786347L)
    }

    test("part 2 should not regress") {
      val program = Day9.getProgramFromResource("2019/day9/input.txt")
      assert(Day9(program, LazyList(2L)).head == 87023L)
    }

    test("it should test 1") {
      val program =
        List(109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99).map(_.toLong)
      assert(Day9(program).toList == program)
    }

    test("it should test 2") {
      val program = List(1102, 34915192, 34915192, 7, 4, 7, 99, 0).map(_.toLong)
      assert(Day9(program).head == 1219070632396864L)
    }

    test("it should test 3") {
      val program = List(104, 1125899906842624L, 99).map(_.toLong)
      assert(Day9(program).head == 1125899906842624L)
    }
  }
}
