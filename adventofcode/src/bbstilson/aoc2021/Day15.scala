package bbstilson.aoc2021

import scala.annotation.tailrec

object Day15 extends aocd.Problem(2021, 15) {
  type Point = (Int, Int)

  def run(input: List[String]): Unit = {
    val xxs = input.map(_.split("").map(_.toInt).toVector).toVector

    part1 {
      val costs = dijkstra(xxs)
      costs((xxs.head.size - 1, xxs.size - 1)) == 398
    }

    part2 {
      val maxX = xxs.head.size
      val maxY = xxs.size
      val beeg = Vector
        .fill(maxX * 5, maxY * 5)(0)
        .zipWithIndex
        .map { case (xs, row) =>
          xs.zipWithIndex.map { case (_, col) =>
            val rowMod = row / maxX
            val colMod = col / maxY
            val newValue = (xxs(row % maxX)(col % maxY) + rowMod + colMod)
            if (newValue > 9) newValue % 9 else newValue
          }
        }

      val costs = dijkstra(beeg)
      costs(((maxX * 5) - 1, (maxY * 5) - 1))
    }

    ()
  }

  // Implementation roughly done by following this video:
  // https://www.youtube.com/watch?v=pVfj6mxhdMw
  def dijkstra(xxs: Vector[Vector[Int]]): Map[Point, Int] = {
    val maxX = xxs.head.size
    val maxY = xxs.size

    val edgeCosts = xxs.zipWithIndex.foldLeft(Map.empty[Point, Int]) { case (costs, (xs, row)) =>
      xs.zipWithIndex.foldLeft(costs) { case (cs, (x, col)) =>
        cs + ((row -> col) -> x)
      }
    }

    def getNeighbors(p: Point): List[Point] = List(
      (p._1, p._2 + 1), // up
      (p._1, p._2 - 1), // down
      (p._1 - 1, p._2), // left
      (p._1 + 1, p._2) // right
    )
      .filter { case (nX, nY) => nX >= 0 && nX < maxX && nY >= 0 && nY < maxY }

    @tailrec
    def helper(
      costs: Map[Point, Int],
      visited: Set[Point],
      frontier: Set[Point]
    ): Map[Point, Int] = {
      if (frontier.isEmpty) costs
      else {
        // Visit the frontier vertex with the smallest known distance from the
        // start vertex.
        val currentPoint = frontier.minBy(p => costs.getOrElse(p, Integer.MAX_VALUE))
        val currentCost = costs.getOrElse(currentPoint, Integer.MAX_VALUE)

        // For the current vertex, examine its frontier neighbors.
        val neighbors = getNeighbors(currentPoint).filterNot(visited)

        // For the current vertex, calculate the distance of each neighbor
        // from the start vertex. If the calculated distance of a vertex is
        // less than the known distance, update the shortest distance.
        //
        // Update the previous vertex for each of the updated distances.
        val nextCosts = neighbors.foldLeft(costs) {
          case (cs, point) => {
            val newCost = currentCost + edgeCosts(point)
            val nextCost = cs.get(point) match {
              case Some(existing) if newCost < existing => newCost
              case Some(existing)                       => existing
              case None                                 => newCost
            }

            cs + (point -> nextCost)
          }
        }

        helper(
          nextCosts,
          visited + currentPoint,
          (frontier - currentPoint) ++ neighbors
        )
      }
    }

    val start = (0, 0)

    helper(Map(start -> 0), Set.empty, Set(start))
  }
}
