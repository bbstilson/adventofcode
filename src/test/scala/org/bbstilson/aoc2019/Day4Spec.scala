package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec

class Day4Spec extends UnitSpec {
  behavior of "Day4"

  "isValid" should "work for test cases" in {
    Day4.isValid(111111) shouldBe true
    Day4.isValid(223450) shouldBe false
    Day4.isValid(123789) shouldBe false
    Day4.isValid(786667) shouldBe false
  }
}
