package org.bbstilson.aoc2018

object Day6Part2 {
  case class Point(x: Int, y: Int)
  type BoundingBox = (Point, Point)

  val pointLineRegex = """(\d+),\s+(\d+)""".r

  def main(args: Array[String]): Unit = {
    val sites: Seq[Point] = io.Source
      .fromResource("2018/day6/input.txt")
      .getLines()
      .toSeq
      .map(toPoint _)

    val boundingBox = getBoundingBox(sites)
    val allPoints = getPointsInBoundingBox(boundingBox)
    val pointsInRange = allPoints
      .map(p => (p, totalDist(sites, p)))
      .toMap
      .filter({ case (_, dist) => dist <= 10000 })
      .size

    println(pointsInRange)
  }

  private def toPoint(line: String): Point = {
    val pointLineRegex(x, y) = line
    Point(x.toInt, y.toInt)
  }

  // Returns lower right corner.
  private def getBoundingBox(ps: Seq[Point]): BoundingBox = {
    val xs = ps.map(_.x)
    val ys = ps.map(_.y)

    (Point(xs.min, ys.min), Point(xs.max, ys.max))
  }

  private def getPointsInBoundingBox(boundingBox: BoundingBox): List[Point] = {
    val (lowerLeft, topRight) = boundingBox

    (lowerLeft.x to topRight.x).flatMap { x =>
      (lowerLeft.y to topRight.y).map { y =>
        (Point(x, y))
      }
    }.toList
  }

  private def totalDist(sites: Seq[Point], point: Point): Int = {
    sites.map(s => mDistance(s, point)).sum
  }

  // Compute Manhattan distance for two points.
  // https://math.stackexchange.com/a/139604
  private def mDistance(site: Point, point: Point): Int = {
    Math.abs(site.x - point.x) + Math.abs(site.y - point.y)
  }
}
