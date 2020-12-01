package bbstilson.aoc2019

import utest._

object Day4Spec extends TestSuite {

  val tests = Tests {

    test("isValid should work for test cases") {
      assert(Day4.isValid(111111))
      assert(!Day4.isValid(223450))
      assert(!Day4.isValid(123789))
      assert(!Day4.isValid(786667))
    }
  }
}
