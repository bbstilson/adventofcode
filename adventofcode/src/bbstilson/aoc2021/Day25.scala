package bbstilson.aoc2021

object Day25 extends aocd.Problem(2021, 25) {
  def run(input: List[String]): Unit = {
    val xs = input.map(_.toInt)
    part1(xs)
    part2(xs)
    ()
  }

  def part1(xs: List[Int]): Int = part1 { xs.head }

  def part2(xs: List[Int]): Int = part2 { xs.head }
}
