package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec

class Day12Spec extends UnitSpec {
  behavior of "Day12"

  import Day12._

  "calc" should "return expected values" in {
    calc(3, 5) shouldBe 1
    calc(3, 3) shouldBe 0
    calc(5, 3) shouldBe -1
  }

  "step" should "return expected calculations" in {
    val m1 = Moon(Point(-1,0,2))
    val m2 = Moon(Point(2,-10,-7))
    val m3 = Moon(Point(4,-8,8))
    val m4 = Moon(Point(3,5,-1))

    val stepped = step(List(m1, m2, m3, m4))

    stepped(0) shouldBe Moon(Point(2,-1,1), Velocity(3,-1,-1))
    stepped(1) shouldBe Moon(Point(3,-7,-4), Velocity(1,3,3))
    stepped(2) shouldBe Moon(Point(1,-7,5), Velocity(-3,1,-3))
    stepped(3) shouldBe Moon(Point(2,2,0), Velocity(-1,-3,1))

    val stepped2 = step(stepped)

    stepped2(0) shouldBe Moon(Point(5,-3,-1), Velocity(3,-2,-2))
    stepped2(1) shouldBe Moon(Point(1,-2,2), Velocity(-2,5,6))
    stepped2(2) shouldBe Moon(Point(1,-4,-1), Velocity(0,3,-6))
    stepped2(3) shouldBe Moon(Point(1,-4,2), Velocity(-1,-6,2))
  }

  "step" should "return expected calculations for many steps" in {
    val m1 = Moon(Point(-8,-10,0))
    val m2 = Moon(Point(5,5,10))
    val m3 = Moon(Point(2,-7,3))
    val m4 = Moon(Point(9,-8,-3))
    val moons = List(m1, m2, m3, m4)
    val stepped = Iterator.iterate(moons)(step).drop(100).next

    stepped(0) shouldBe Moon(Point(8,-12,-9), Velocity(-7,3,0))
    stepped(1) shouldBe Moon(Point(13,16,-3), Velocity(3,-11,-5))
    stepped(2) shouldBe Moon(Point(-29,-11,-1), Velocity(-3,7,4))
    stepped(3) shouldBe Moon(Point(16,-13,23), Velocity(7,1,1))
  }

  "energy" should "calculate energy" in {
    val m1 = Moon(Point(-8,-10,0))
    val m2 = Moon(Point(5,5,10))
    val m3 = Moon(Point(2,-7,3))
    val m4 = Moon(Point(9,-8,-3))
    val moons = List(m1, m2, m3, m4)
    val totalEnergy = Iterator.iterate(moons)(step).drop(100).next.map(energy).sum
    totalEnergy shouldBe 1940
  }
}
