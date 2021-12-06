package bbstilson.aoc2021

import scala.annotation.tailrec

object Day6 extends aocd.Problem(2021, 6) {
  def run(input: List[String]): Unit = {
    val xs = input.head
      .split(",")
      .map(_.toLong)
      .toList
      .groupMapReduce(identity)(_ => 1L)(_ + _)

    part1 {
      generation(xs, 1, 80)
    }

    part2 {
      generation(xs, 1, 256)
    }

    ()
  }

  @tailrec
  def generation(fishies: Map[Long, Long], day: Int, until: Int): Long = {
    if (day > until) fishies.values.sum
    else {
      val next = fishies.foldLeft(Map.empty[Long, Long]) {
        case (nextGen, (daysUntilBirth, nFish)) => {
          if (daysUntilBirth == 0) {
            nextGen ++ Map(
              6L -> (nFish + nextGen.getOrElse(6, 0L)), // parents
              8L -> (nFish + nextGen.getOrElse(8, 0L)) // children
            )
          } else {
            nextGen + (daysUntilBirth - 1 -> (nFish + nextGen.getOrElse(daysUntilBirth - 1, 0L)))
          }
        }
      }

      generation(next, day + 1, until)
    }
  }
}
