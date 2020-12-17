package bbstilson.aoc2020

import bbstilson.data.Grid._
import bbstilson.data.Point._

object Day17 extends aocd.Problem(2020, 17) {

  def run(input: List[String]): Unit = {
    part1 {
      val grid = buildGrid(input, Vector(0))
      val done = (1 to 6).foldLeft(grid) { case (g, _) => cycle(g) }
      println(countActive(done))
    }
    part2 {
      val grid = buildGrid(input, Vector(0, 0))
      val done = (1 to 6).foldLeft(grid) { case (g, _) => cycle(g) }
      println(countActive(done))
    }
  }

  def cycle(grid: Map[Point, Char]): Map[Point, Char] = {
    pointsToCheck(grid.boundingBox).foldLeft(Map.empty[Point, Char]) { case (nextGrid, point) =>
      point.neighbors().count(grid.at(_) == '#') match {
        case 3                          => nextGrid + (point -> '#')
        case 2 if grid.at(point) == '#' => nextGrid + (point -> '#')
        case _                          => nextGrid + (point -> '.')
      }
    }
  }

  def countActive(grid: Map[_, Char]): Int = grid.count { case (_, c) => c == '#' }

  def pointsToCheck(boundingBox: (Point, Point)): Vector[Point] = {
    val (mins, maxs) = boundingBox
    val corners = mins.zip(maxs).map { case (min, max) => (min - 1) -> (max + 1) }

    def _neighbors(ranges: Vector[(Int, Int)]): Vector[Point] = ranges match {
      case range +: IndexedSeq() => (range._1 to range._2).toVector.map(Vector(_))
      case range +: tail =>
        for {
          h <- (range._1 to range._2).toVector
          t <- _neighbors(tail)
        } yield h +: t
      case _ => Vector(Vector()) // shut up compiler
    }

    _neighbors(corners)
  }

  def buildGrid(input: List[String], extraDimensions: Vector[Int]): Map[Point, Char] = {
    input.map(_.toCharArray().toList.zipWithIndex).zipWithIndex.foldLeft(Map.empty[Point, Char]) {
      case (map, (row, colIdx)) =>
        row.foldLeft(map) { case (m, (char, rowIdx)) =>
          m + ((Vector(colIdx, rowIdx) ++ extraDimensions) -> char)
        }
    }
  }
}
