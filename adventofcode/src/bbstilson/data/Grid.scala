package bbstilson.data

import bbstilson.data.Point._

object Grid {

  implicit class GridImpl(grid: Map[Point, Char]) {

    def at(point: Point): Char = grid.getOrElse(point, '.')

    def boundingBox: (Point, Point) = {
      val points = grid.keys.toVector.transpose.toVector
      (points.map(_.min), points.map(_.max))
    }
  }
}
