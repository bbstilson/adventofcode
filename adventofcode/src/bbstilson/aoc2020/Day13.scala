package bbstilson.aoc2020

object Day13 extends aocd.Problem(2020, 13) {

  def run(input: List[String]): Unit = {
    val canLeave = input.head.toInt
    val schedules = input.last.split(',').toList
    val buses = schedules.collect { case c if c != "x" => c.toInt }

    part1(canLeave, buses)
    ()
  }

  def part1(canLeave: Int, buses: List[Int]): Int = part1 {
    val (bus, depart) = LazyList
      .unfold(canLeave) { now =>
        Some(((buses.find { now % _ == 0 }, now), now + 1))
      }
      .dropWhile { case (found, _) => found.isEmpty }
      .head

    bus.get * (depart - canLeave)
  }
}
