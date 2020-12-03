package bbstilson.aoc2020

import aocd.Problem

object Day1 extends Problem(2020, 1) {

  def run(in: List[String]): Unit = {
    val input = in.map(_.toInt)
    val total = for {
      x <- input
      y <- input
      z <- input
      if x + y + z == 2020
    } yield x * y * z

    println(total.head)
  }
}
