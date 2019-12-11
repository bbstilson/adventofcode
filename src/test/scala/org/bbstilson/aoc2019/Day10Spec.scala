package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec

class Day10Spec extends UnitSpec {
  behavior of "Day10"

  import Day10._

  "slope" should "calculate slopes" in {
    val s1 = Point(1,2)
    val s2 = Point(3,4)
    getSlope(s1,s2) shouldBe PosReal(1D)

    val s3 = Point(13,2)
    val s4 = Point(-30,-10)
    getSlope(s3,s4) shouldBe NegReal(0.2790697674D)

    val s5 = Point(0.0,0.0)
    val s6 = Point(0.0,1.0)
    getSlope(s5,s6) shouldBe PosConstant

    val s7 = Point(0.0,0.0)
    val s8 = Point(1.0,0.0)
    getSlope(s7,s8) shouldBe PosInfinite
  }

  "test 1" should "not regress" in {
    val test = mkSpace("""
      |.#..#
      |.....
      |#####
      |....#
      |...##
      |""")

    part1(test) shouldBe (Point(3,4),8)
  }

  "test 2" should "not regress" in {
    val test = mkSpace("""
    |......#.#.
    |#..#.#....
    |..#######.
    |.#.#.###..
    |.#..#.....
    |..#....#.#
    |#..#....#.
    |.##.#..###
    |##...#..#.
    |.#....####""")

    println(test(0)(4))
    part1(test) shouldBe (Point(5,8), 33)
  }

  "test 3" should "not regress" in {
    val test = mkSpace("""
    |#.#...#.#.
    |.###....#.
    |.#....#...
    |##.#.#.#.#
    |....#.#.#.
    |.##..###.#
    |..#...##..
    |..##....##
    |......#...
    |.####.###.""")

    part1(test) shouldBe (Point(1,2), 35)
  }

  "test 4" should "not regress" in {
    val test = mkSpace("""
    |.#..#..###
    |####.###.#
    |....###.#.
    |..###.##.#
    |##.##.#.#.
    |....###..#
    |..#.#..#.#
    |#..#.#.###
    |.##...##.#
    |.....#.#..""")

    part1(test) shouldBe (Point(6,3), 41)
  }

  "getAngle" should "calculate angles" in {
    val center = Point(12,12)
    val a = Point(18,6)  // I
    val b = Point(18,18) // II
    val c = Point(6,18)  // III
    val d = Point(6,6)   // IV

    getAngle(center, a) shouldBe 45D
    getAngle(center, b) shouldBe 135D
    getAngle(center, c) shouldBe 225D
    getAngle(center, d) shouldBe 315D
  }

  "part 1" should "not regress" in {
    val space = parseInput()
    part1(space)._2 shouldBe 269
  }

  "part 2" should "not regress" in {
    val space = parseInput()
    val (best, _) = part1(space)
    part2(space, best) shouldBe 612
  }

  private def mkSpace(str: String): Space = {
    str.stripMargin.split("\n").filterNot(_.isEmpty).map(_.split("").toVector).toVector
  }
}
