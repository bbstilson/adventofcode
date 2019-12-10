package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec

class Day10Spec extends UnitSpec {
  behavior of "Day10"

  import Day10._

  "slope" should "calculate slopes" in {
    val s1 = Point(1,2)
    val s2 = Point(3,4)
    s1.slope(s2) shouldBe PosReal(1D)

    val s3 = Point(13,2)
    val s4 = Point(-30,-10)
    s3.slope(s4) shouldBe NegReal(0.2790697674D)

    val s5 = Point(0.0,0.0)
    val s6 = Point(0.0,1.0)
    s5 slope s6 shouldBe PosConstant

    val s7 = Point(0.0,0.0)
    val s8 = Point(1.0,0.0)
    s7 slope s8 shouldBe PosInfinite
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

  private def mkSpace(str: String): Space = {
    str.stripMargin.split("\n").filterNot(_.isEmpty).map(_.split("").toVector).toVector
  }
}
