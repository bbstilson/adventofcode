package bbstilson.aoc2020

import scala.annotation.tailrec

object Day5 extends aocd.Problem(2020, 5) {

  def run(input: List[String]): Unit = {
    val ids = input.map(parseAssignment).sorted
    println(ids.last)
    println(findMySeat(ids))
  }

  private def parseAssignment(ass: String): Int = {
    val row = bsearch(0, 127, ass.take(7))
    val col = bsearch(0, 7, ass.drop(7))
    (row * 8) + col
  }

  @tailrec
  private def bsearch(min: Int, max: Int, plan: String): Int = {
    val mid = (max - min) / 2
    val (nextMin, nextMax) = plan.head match {
      case 'F' | 'L' => (min, mid + min)
      case 'B' | 'R' => (min + mid + 1, max)
    }

    plan.tail match {
      case "" => nextMin
      case xs => bsearch(nextMin, nextMax, xs)
    }
  }

  private def findMySeat(ids: List[Int]): Int =
    ids
      .sliding(2)
      .collectFirst { case List(a, b) if a + 1 != b => a + 1 }
      .get
}
