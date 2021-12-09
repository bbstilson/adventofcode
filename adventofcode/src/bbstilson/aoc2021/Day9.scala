package bbstilson.aoc2021

import scala.collection.immutable.Queue
import scala.annotation.tailrec

object Day9 extends aocd.Problem(2021, 9) {
  def run(input: List[String]): Unit = {
    val heightMap: Vector[Vector[Int]] = input
      .map(_.split("").map(_.toInt).toVector)
      .toVector

    val maxY = heightMap.size
    val maxX = heightMap.head.size

    def validNeighbors(x: Int, y: Int): List[(Int, Int)] = List(
      (x + 1, y),
      (x - 1, y),
      (x, y + 1),
      (x, y - 1)
    ).filter { case (nX, nY) => isValid(nX, nY) }

    def isValid(x: Int, y: Int): Boolean =
      y >= 0 && y < maxY && x >= 0 && x < maxX

    def isLowPoint(x: Int, y: Int): Boolean =
      validNeighbors(x, y)
        .map { case (nX, nY) => heightMap(nY)(nX) }
        .forall(_ > heightMap(y)(x))

    @tailrec
    def floodFill(
      x: Int,
      y: Int,
      frontier: Queue[(Int, Int)],
      seen: Set[(Int, Int)]
    ): Set[(Int, Int)] = {
      val nbs = validNeighbors(x, y)
        .filterNot(seen)
        .filter { case (nX, nY) => heightMap(nY)(nX) > heightMap(y)(x) && heightMap(nY)(nX) != 9 }

      val nextSeen = seen + (x -> y)

      frontier.enqueueAll(nbs).dequeueOption match {
        case Some(((nX, nY), nextQueue)) => floodFill(nX, nY, nextQueue, nextSeen)
        case None                        => nextSeen
      }
    }

    val lowPoints = time(
      "lowPoints", {
        for {
          y <- 0 until maxY
          x <- 0 until maxX
          if isLowPoint(x, y)
        } yield x -> y

      }
    )

    part1 {
      lowPoints.map { case (x, y) =>
        heightMap(y)(x) + 1
      }.sum
    }

    part2 {
      lowPoints
        .map { case (x, y) => floodFill(x, y, Queue.empty, Set.empty).size }
        .sorted
        .reverse
        .take(3)
        .product
    }

    ()
  }
}
