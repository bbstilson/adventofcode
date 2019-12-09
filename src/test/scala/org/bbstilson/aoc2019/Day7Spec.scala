package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec
import org.bbstilson.aoc2019.intcode.IntCodeComputer

class Day7Spec extends UnitSpec {
  behavior of "Day7"

  "part 1" should "not regress" in {
    val program = IntCodeComputer.getProgramFromResource("2019/day7/input.txt")
    Day7.part1(program) shouldBe 87138
  }

  "part 1 test" should "not regress" in {
    val program = List(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0).map(_.toLong)
    val output = List(4L,3L,2L,1L,0L).foldLeft(0L) { case (prev, phase) =>
      IntCodeComputer(program, LazyList(phase, prev)).head
    }
    output shouldBe 43210L
  }

  "part 2" should "not regress" ignore {
    val program = IntCodeComputer.getProgramFromResource("2019/day7/input.txt")
    Day7.part2(program) shouldBe 1L
  }

  "part 2 test" should "not regress" in {
    val program = List(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5).map(_.toLong)
    def a: LazyList[Long] = IntCodeComputer(program, 9L #:: 0L #:: e)
    def b: LazyList[Long] = IntCodeComputer(program, 8L #:: a)
    def c: LazyList[Long] = IntCodeComputer(program, 7L #:: b)
    def d: LazyList[Long] = IntCodeComputer(program, 6L #:: c)
    def e: LazyList[Long] = IntCodeComputer(program, 5L #:: d)

    e.last shouldBe 139629729L
  }

  it should "not regress - 2" in {
    val program = List(3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10).map(_.toLong)

    def a: LazyList[Long] = IntCodeComputer(program, 9L #:: 0L #:: e)
    def b: LazyList[Long] = IntCodeComputer(program, 7L #:: a)
    def c: LazyList[Long] = IntCodeComputer(program, 8L #:: b)
    def d: LazyList[Long] = IntCodeComputer(program, 5L #:: c)
    def e: LazyList[Long] = IntCodeComputer(program, 6L #:: d)

    e.last shouldBe 18216L
  }
}
