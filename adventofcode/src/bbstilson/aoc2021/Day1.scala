package bbstilson.aoc2021

object Day1 extends aocd.Problem(2021, 1) {
  def run(input: List[String]): Unit = {
    val xs = input.map(_.toInt)
    part1(xs)
    part2(xs)
    ()
  }

  def part1(xs: List[Int]): Int = part1 {
    xs.sliding(2).foldLeft(0) {
      case (acc, t) if t.head < t.last => acc + 1
      case (acc, _) => acc
    }
  }

  def part2(xs: List[Int]): Int = part2 {
    val windows = xs.sliding(3).map(_.sum).toList
    part1(windows)
  }
}
