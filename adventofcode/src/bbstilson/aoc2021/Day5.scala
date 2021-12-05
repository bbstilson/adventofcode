package bbstilson.aoc2021

import bbstilson.implicits.ListImplicits._

object Day5 extends aocd.Problem(2021, 5) {
  val LineRegex = """(\d+),(\d+) -> (\d+),(\d+)""".r
  def run(input: List[String]): Unit = {
    val lines = input
      .map { case LineRegex(x1, y1, x2, y2) =>
        (x1.toInt, y1.toInt, x2.toInt, y2.toInt)
      }

    val (straightLines, diagonalLines) = lines
      .partition { case (x1, y1, x2, y2) => x1 == x2 || y1 == y2 }

    val straightLinesMap = time(
      "straightLinesMap", {
        straightLines
          .foldLeft(List.empty[(Int, Int)]) {
            case (ps, (x1, y1, x2, y2)) => {
              val horizontalLine = y1 == y2
              val (min, max) =
                if (horizontalLine) (Math.min(x1, x2), Math.max(x1, x2))
                else (Math.min(y1, y2), Math.max(y1, y2))

              ps ++ (min to max).map { i => if (horizontalLine) (i, y1) else (x1, i) }
            }
          }
          .toFrequencyMap
      }
    )

    part1 {
      straightLinesMap.count { case (_, n) => n > 1 }
    }

    part2 {
      val diagonalLinesMap = diagonalLines
        .foldLeft(List.empty[(Int, Int)]) {
          case (ps, (x1, y1, x2, y2)) => {
            val xRange = (x1 to x2 by (if (x1 > x2) -1 else 1))
            val yRange = (y1 to y2 by (if (y1 > y2) -1 else 1))
            ps ++ xRange.zip(yRange)
          }
        }
        .toFrequencyMap

      (diagonalLinesMap.keySet ++ straightLinesMap.keySet)
        .foldLeft(Map.empty[(Int, Int), Int]) { case (m, p) =>
          m + (p -> (straightLinesMap.getOrElse(p, 0) + diagonalLinesMap.getOrElse(p, 0)))
        }
        .count { case (_, n) => n > 1 }
    }

    ()
  }
}
