package bbstilson.aoc2019

import utest._

object Day10Spec extends TestSuite {
  import Day10._

  val tests = Tests {

    test("slope should calculate slopes") {
      val s1 = Point(1, 2)
      val s2 = Point(3, 4)
      assert(getSlope(s1, s2) == PosReal(1d))

      val s3 = Point(13, 2)
      val s4 = Point(-30, -10)
      assert(getSlope(s3, s4) == NegReal(0.2790697674d))

      val s5 = Point(0.0, 0.0)
      val s6 = Point(0.0, 1.0)
      assert(getSlope(s5, s6) == PosConstant)

      val s7 = Point(0.0, 0.0)
      val s8 = Point(1.0, 0.0)
      assert(getSlope(s7, s8) == PosInfinite)
    }

    test("test 1 should not regress") {
      val test = mkSpace("""
      |.#..#
      |.....
      |#####
      |....#
      |...##
      |""")

      assert(part1(test) == (Point(3, 4) -> 8))
    }

    test("test 2 should not regress") {
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
      assert(part1(test) == (Point(5, 8) -> 33))
    }

    test("test 3 should not regress") {
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

      assert(part1(test) == (Point(1, 2) -> 35))
    }

    test("test 4 should not regress") {
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

      assert(part1(test) == (Point(6, 3) -> 41))
    }

    test("getAngle should calculate angles") {
      val center = Point(12, 12)
      val a = Point(18, 6) // I
      val b = Point(18, 18) // II
      val c = Point(6, 18) // III
      val d = Point(6, 6) // IV

      assert(getAngle(center, a) == 45d)
      assert(getAngle(center, b) == 135d)
      assert(getAngle(center, c) == 225d)
      assert(getAngle(center, d) == 315d)
    }

    test("part 1 should not regress") {
      val space = parseInput()
      assert(part1(space)._2 == 269)
    }

    test("part 2 should not regress") {
      val space = parseInput()
      val (best, _) = part1(space)
      assert(part2(space, best) == 612)
    }
  }

  private def mkSpace(str: String): Space = {
    str.stripMargin.split("\n").filterNot(_.isEmpty).map(_.split("").toVector).toVector
  }
}
