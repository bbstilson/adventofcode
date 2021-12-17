package bbstilson.aoc2021

import utest._

object Day17Spec extends TestSuite {
  import Day17._

  val tests = Tests {
    test("part1") {
      val target = Area((20, -10), (30, -5))
      canHit(Projectile(6 -> 9))(target) ==> true
      target.contains((21, -10)) ==> true
    }
  }
}
