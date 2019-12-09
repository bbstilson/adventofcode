package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec
import org.bbstilson.aoc2019.intcode.IntCodeComputer

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class Day9Spec extends UnitSpec {
  behavior of "Day9"

  "part 1 test" should "not regress" in {
    val input = List(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99).map(_.toLong)
    val answer = Future { IntCodeComputer(input).toList }
    Await.result(answer, 1.second) shouldBe input
  }

  // "part 1 test 2" should "not regress" in {
  //   val input: List[Long] = List(1102,34915192,34915192,7,4,7,99,0)
  //   IntCodeComputer(input, LazyList.empty).toList shouldBe input
  // }

  // "part 1 test 3" should "not regress" in {
  //   val input = List(104L,1125899906842624L,99L)
  //   IntCodeComputer(input, LazyList.empty).toList shouldBe 1125899906842624L
  // }
}
