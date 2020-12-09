package bbstilson.aoc2020

object Day9 extends aocd.Problem(2020, 9) {

  val WINDOW_SIZE = 25

  def run(input: List[String]): Unit = {
    val xs = input.map(_.toLong)
    val p1 = part1(xs)
    println(p1)
    println(part2(xs, p1))
  }

  def part1(input: List[Long]): Long =
    input
      .sliding(WINDOW_SIZE + 1)
      .find { window =>
        window.init.find(x1 => window.init.find(_ + x1 == window.last).nonEmpty).isEmpty
      }
      .get
      .last

  def part2(input: List[Long], target: Long): Long =
    (0 until input.size)
      .to(Iterator)
      .map { idx =>
        val sums: List[Long] = input
          .to(Iterator)
          .drop(idx)
          .scanLeft(0L)(_ + _)
          .takeWhile(_ <= target)
          .toList

        sums.last match {
          case `target` => {
            val range = input.drop(idx).take(sums.size - 1).sorted
            (true, range.head + range.last)
          }
          case _ => (false, -1L)
        }
      }
      .find(_._1)
      .get
      ._2
}
