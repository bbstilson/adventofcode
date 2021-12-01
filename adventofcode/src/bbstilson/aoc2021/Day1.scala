package bbstilson.aoc2021

object Day1 extends aocd.Problem(2021, 1) {
  def run(input: List[String]): Unit = {
    val xs = input.map(_.toInt)
    part1(xs)
    part2(xs)
    ()
  }

  def part1(xs: List[Int]): Int = part1 {
    xs.sliding(2).count {
      case List(x, y) => x < y
      case _ => false // silence my strict compiler
    }
  }

  def part2(xs: List[Int]): Int = part2 {
    val windows = xs.sliding(3).map(_.sum).toList
    part1(windows)
  }
}
