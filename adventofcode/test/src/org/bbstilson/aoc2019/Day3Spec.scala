package org.bbstilson.aoc2019

import utest._

object Day3Spec extends TestSuite {

  val tests = Tests {

    test("part1 - work for test cases") {
      assert(Day3.part1(testInput1) == 6)
      assert(Day3.part1(testInput2) == 159)
      assert(Day3.part1(testInput3) == 135)
    }
  }

  val testInput1 = List(
    List("R8", "U5", "L5", "D3"),
    List("U7", "R6", "D4", "L4")
  )

  val testInput2 = List(
    List("R75", "D30", "R83", "U83", "L12", "D49", "R71", "U7", "L72"),
    List("U62", "R66", "U55", "R34", "D71", "R55", "D58", "R83")
  )

  val testInput3 = List(
    List("R98", "U47", "R26", "D63", "R33", "U87", "L62", "D20", "R33", "U53", "R51"),
    List("U98", "R91", "D20", "R16", "D67", "R40", "U7", "R15", "U6", "R7")
  )
}
