package bbstilson.data

object Point {
  type Point = Vector[Int]

  implicit class PointOps(point: Point) {

    /** Computes all neighbors in n-dimensional space that are at most $distance away.
      *
      * @param distance
      * @return all neighbors
      */
    def neighbors(distance: Int = 1): Vector[Point] = {
      def _neighbors(_point: Point): Vector[Point] = _point match {
        case head +: IndexedSeq() => (head - distance to head + distance).toVector.map(Vector(_))
        case head +: tail =>
          for {
            h <- (head - distance to head + distance).toVector
            t <- _neighbors(tail)
          } yield h +: t
        case _ => Vector(Vector()) // shut up compiler
      }

      _neighbors(point).filterNot(_ == point)
    }
  }

}
