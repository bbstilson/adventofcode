package bbstilson.aoc2020

object Day1 extends bbstilson.Problem("day1", _.toInt) {

  def run(input: List[Int]): Unit = {
    val total = for {
      x <- input
      y <- input
      z <- input
      if x + y + z == 2020
    } yield x * y * z

    println(total.head)
  }
}
