package org.bbstilson.aoc2019

import org.bbstilson.UnitSpec
import org.bbstilson.aoc2019.intcode.IntCodeComputer

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class Day9Spec extends UnitSpec {
  behavior of "Day9"

  "part 1" should "not regress" in {
    val program = IntCodeComputer.getProgramFromResource("2019/day9/input.txt")
    IntCodeComputer(program, LazyList(1)).foreach(println) // part 1
  }

  it should "test 1" in {
    val program = List(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99).map(_.toLong)
    Await.result(Future { IntCodeComputer(program).toList }, 1.second) shouldBe program
  }

  it should "test 2" in {
    val program = List(1102,34915192,34915192,7,4,7,99,0).map(_.toLong)
    IntCodeComputer(program, LazyList.empty).toList.head shouldBe 1219070632396864L
  }

  it should "test 3" in {
    val program = List(104,1125899906842624L,99).map(_.toLong)
    IntCodeComputer(program, LazyList.empty).toList.head shouldBe 1125899906842624L
  }
}
