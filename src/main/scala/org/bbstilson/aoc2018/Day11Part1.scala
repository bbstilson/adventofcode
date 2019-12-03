package org.bbstilson.aoc2018

/*
Thanks to many resources online, but these in particular:
https://www.seas.upenn.edu/~cis565/Lectures2011/Lecture15_SAT.pdf
https://blog.demofox.org/2018/04/16/prefix-sums-and-summed-area-tables/
https://gist.github.com/SiestaMadokaist/74e573365a02f5d914d2
*/

object Day11Part1 {
  case class Point(x: Int, y: Int) {
    def adjust(side: Int): Point = Point(x - side + 1, y - side + 1)
  }
  object Point {
    def apply(xy: (Int, Int)): Point = xy match {
      case (x, y) => Point(x, y)
    }
  }

  private val AREA_SIDE = 3
  private val RANGE = 300
  private val SERIAL_NUMBER = 7400

  def main(args: Array[String]): Unit = {
    val cellMap = mkCellMap()
    val sat = mkSummedAreaTable(cellMap)
    val (largestPoint, largestArea) = findLargestArea(sat)

    println(s"Largest Point = ${largestPoint.adjust(AREA_SIDE)}")
    println(s"Largest Area - $largestArea")
  }

  private def mkCellMap(): Map[Point, Int] = {
    (1 to RANGE).flatMap(y => (1 to RANGE).map(x => (x, y)))
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
        val cumSum = a + b + c - d
        xSat + (p -> cumSum)
      }
    }
  }

  private def findLargestArea(sat: Map[Point, Int]): (Point, Int) = {
    (AREA_SIDE to RANGE).foldLeft(Option.empty[(Point, Int)]) { case (xFound, y) =>
      (AREA_SIDE to RANGE).foldLeft(xFound) { case (found, x) =>
        val p = Point(x, y)
        val d = sat(p)
        val a = sat.getOrElse(Point(x - AREA_SIDE, y - AREA_SIDE), 0)
        val b = sat.getOrElse(Point(x - AREA_SIDE, y), 0)
        val c = sat.getOrElse(Point(x, y - AREA_SIDE), 0)
        val sum = a + d - b - c

        found match {
          case lastFound @ Some((pFound, pSum)) => {
            if (sum > pSum) {
              Some((p, sum))
            } else {
              lastFound
            }
          }
          case None => Some((p, sum))
        }
      }
    } match {
      case Some(pointSum) => pointSum
      case None => (Point(-1, -1), -1)
    }
  }
}
