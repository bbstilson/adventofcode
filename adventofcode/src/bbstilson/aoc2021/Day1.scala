package bbstilson.aoc2021

object Day1 extends aocd.Problem(2021, 1) {
  def run(input: List[String]): Unit = {
    val xs = input.map(_.toInt)
    part1(xs)
    part2(xs)
    ()
  }

  def part1(xs: List[Int]): Int = part1 {
    xs.tail.foldLeft((xs.head, 0)) { case ((prev, incs), x) => (x, incs + (if (prev < x) 1 else 0)) }._2
  }

  def part2(xs: List[Int]): Int = part2 {
    val windows = xs.sliding(3).map(_.sum).toList
    windows.tail.foldLeft((windows.head, 0)) {
      case ((prev, incs), window) => (window, incs + (if (prev < window) 1 else 0))
    }._2
  }
}
