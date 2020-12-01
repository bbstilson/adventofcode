package bbstilson.aoc2018

/*
Thanks to many resources online, but these in particular:
https://www.seas.upenn.edu/~cis565/Lectures2011/Lecture15_SAT.pdf
https://blog.demofox.org/2018/04/16/prefix-sums-and-summed-area-tables/
https://gist.github.com/SiestaMadokaist/74e573365a02f5d914d2
 */

object Day11Part2 {

  case class Point(x: Int, y: Int) {
    def adjust(side: Int): Point = Point(x - side + 1, y - side + 1)
  }

  object Point {

    def apply(xy: (Int, Int)): Point = xy match {
      case (x, y) => Point(x, y)
    }
  }

  private val RANGE = 300
  private val SERIAL_NUMBER = 7400

  def main(args: Array[String]): Unit = {
    val cellMap = mkCellMap()
    val sat = mkSummedAreaTable(cellMap)
    val (largestPoint, largestSide, largestArea) = findLargestArea(sat)

    println(s"Largest Point = ${largestPoint.adjust(largestSide)}")
    println(s"Largest Side = $largestSide")
    println(s"Largest Area = $largestArea")
  }

  private def mkCellMap(): Map[Point, Int] = {
    (1 to RANGE)
      .flatMap(y => (1 to RANGE).map(x => (x, y)))
      .map(Point.apply)
      .map(p => (p, calcPowerLevel(p)))
      .toMap
  }

  private def calcPowerLevel(p: Point): Int = {
    val rackId = p.x + 10
    getHundred(((rackId * p.y) + SERIAL_NUMBER) * rackId) - 5
  }

  private def getHundred(n: Int): Int = n / 100 % 10

  private def mkSummedAreaTable(pointMap: Map[Point, Int]): Map[Point, Int] = {
    val emptySat = Map.empty[Point, Int]
    (1 to RANGE).foldLeft(emptySat) { case (sat, y) =>
      (1 to RANGE).foldLeft(sat) { case (xSat, x) =>
        val p = Point(x, y)
        val a = pointMap(p)
        val b = xSat.getOrElse(Point(x - 1, y), 0)
        val c = xSat.getOrElse(Point(x, y - 1), 0)
        val d = xSat.getOrElse(Point(x - 1, y - 1), 0)
        xSat + (p -> (a + b + c - d))
      }
    }
  }

  private def findLargestArea(sat: Map[Point, Int]): (Point, Int, Int) = {
    (1 to RANGE).foldLeft(Option.empty[(Point, Int, Int)]) { case (opt, side) =>
      (side to RANGE).foldLeft(opt) { case (xFound, y) =>
        (side to RANGE).foldLeft(xFound) { case (found, x) =>
          val p = Point(x, y)
          val d = sat(p)
          val a = sat.getOrElse(Point(x - side, y - side), 0)
          val b = sat.getOrElse(Point(x - side, y), 0)
          val c = sat.getOrElse(Point(x, y - side), 0)
          val sum = a + d - b - c

          found match {
            case lastFound @ Some((_, _, oSum)) => {
              if (sum > oSum) {
                Some((p, side, sum))
              } else {
                lastFound
              }
            }
            case None => Some((p, side, sum))
          }
        }
      }
    } match {
      case Some(found) => found
      case None        => (Point(-1, -1), -1, -1)
    }
  }
}
