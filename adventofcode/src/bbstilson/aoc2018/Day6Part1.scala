package bbstilson.aoc2018

object Day6Part1 {
  case class Point(x: Int, y: Int)

  trait PointState
  case object Init extends PointState
  case object Shared extends PointState
  case class Unique(val site: Point) extends PointState

  type VoronoiDiagram = Map[Point, PointState]
  type BoundingBox = (Point, Point)

  val pointLineRegex = """(\d+),\s+(\d+)""".r

  def main(args: Array[String]): Unit = {
    val sites: Seq[Point] = io.Source
      .fromResource("2018/day6/input.txt")
      .getLines()
      .toSeq
      .map(line => {
        val pointLineRegex(x, y) = line
        Point(x.toInt, y.toInt)
      })

    val boundingBox = getBoundingBox(sites)
    val voronoiDiagram = mkVoronoiDiagram(boundingBox, sites)
    val (biggestSite, biggestFiniteArea) = query(boundingBox, voronoiDiagram)

    println(biggestSite)
    println(biggestFiniteArea)
  }

  // Returns lower right corner.
  private def getBoundingBox(ps: Seq[Point]): BoundingBox = {
    val xs = ps.map(_.x)
    val ys = ps.map(_.y)

    (Point(xs.min, ys.min), Point(xs.max, ys.max))
  }

  private def mkVoronoiDiagram(
    boundingBox: BoundingBox,
    sites: Seq[Point]
  ): VoronoiDiagram = {
    val (lowerLeft, topRight) = boundingBox

    (lowerLeft.x to topRight.x)
      .flatMap { x =>
        (lowerLeft.y to topRight.y).map { y =>
          (Point(x, y), Init)
        }
      }
      .toMap
      .map { case (p, _) => (p, setPointState(sites, p)) }
  }

  private def setPointState(sites: Seq[Point], point: Point): PointState = {
    val (_, closestSites) = sites
      .map(site => (site, mDistance(site, point)))
      .groupBy({ case (_, dist) => dist })
      .view
      .mapValues(_.map { case (site, _) => site })
      .toMap
      .toList
      .sortBy({ case (dist, _) => dist })
      .head

    if (closestSites.size == 1) {
      Unique(closestSites.head)
    } else {
      Shared
    }
  }

  // Compute Manhattan distance for two points.
  // https://math.stackexchange.com/a/139604
  private def mDistance(site: Point, point: Point): Int = {
    Math.abs(site.x - point.x) + Math.abs(site.y - point.y)
  }

  private def query(
    boundingBox: BoundingBox,
    vDiagram: VoronoiDiagram
  ): (Point, Int) = {
    vDiagram.toList
      .map({ case (point, pointState) =>
        pointState match {
          case Unique(site) => (point, Some(site))
          case _            => (point, None)
        }
      })
      .filter({ case (_, site) => site.isDefined })
      .map({ case (point, site) => (point, site.get) })
      .groupBy({ case (_, site) => site })
      .toList
      .map({ case (site, xs) => (site, xs.map { case (p, _) => p }) })
      // Filter out any sites that have at least one infinite point.
      .filterNot({ case (_, points) => points.exists(p => isInfinite(boundingBox, p)) })
      .map({ case (site, points) => (site, points.size) })
      .sortBy({ case (_, points) => points })
      .last
  }

  private def isInfinite(boundingBox: BoundingBox, point: Point): Boolean = {
    val (bottom, top) = boundingBox
    (
      point.x == bottom.x ||
      point.x == top.x ||
      point.y == bottom.y ||
      point.y == top.y
    )
  }
}
