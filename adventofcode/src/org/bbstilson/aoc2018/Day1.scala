package org.bbstilson.aoc2018

object Day1 {

  def main(args: Array[String]): Unit = {
    val source: List[Int] = io.Source
      .fromResource("2018/day1/input.txt")
      .getLines()
      .toList
      .map(_.toInt)

    println(findFirstRep(source))
  }

  private def findFirstRep(xs: List[Int]): Int = {
    findFirstRep(xs, Set.empty[Int], 0)
  }

  private def findFirstRep(xs: List[Int], seenSet: Set[Int], lV: Int): Int = {
    val (found, nextSeen, nextLastVal) = xs.foldLeft((Option.empty[Int], seenSet, lV)) {
      case ((found, seen, lastVal), x) => {
        found match {
          case Some(x) => (Some(x), seen, 0)
          case None => {
            val next = lastVal + x
            if (seen.contains(next)) {
              (Some(next), seen, 0)
            } else {
              (None, seen + next, next)
            }
          }
        }
      }
    }

    found match {
      case Some(x) => x
      case None    => findFirstRep(xs, nextSeen, nextLastVal)
    }
  }
}
