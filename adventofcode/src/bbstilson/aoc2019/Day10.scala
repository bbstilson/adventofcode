package bbstilson.aoc2019

import scala.math.BigDecimal

sealed trait Slope
final case class PosReal(s: Double) extends Slope
final case class NegReal(s: Double) extends Slope
case object PosInfinite extends Slope
case object NegInfinite extends Slope
case object PosConstant extends Slope
case object NegConstant extends Slope

final case class Point(x: Double, y: Double)

object Day10 {

  type Space = Vector[Vector[String]]

  def main(args: Array[String]): Unit = {
    val space = parseInput()
    val (bestAsteroid, viewable) = part1(space)
    println(viewable)
    println(part2(space, bestAsteroid))
  }

  def part1(space: Space): (Point, Int) = {
    val asteroids = asteroidsFromSpace(space)
    val max = asteroids.map { currentAsteroid =>
      val viewable = asteroids
        .collect {
          case a if a != currentAsteroid => getSlope(currentAsteroid, a)
        }
        .toSet
        .size

      (currentAsteroid, viewable)
    }

    max.maxBy { case (_, viewable) => viewable }
  }

  def part2(space: Space, bestAsteroid: Point): Int = {
    val asteroids = asteroidsFromSpace(space)
    val asteroidsToDestroy = asteroids
      .collect {
        case a if a != bestAsteroid => (getSlope(bestAsteroid, a), a)
      }
      .groupBy(_._1)
      .view
      .mapValues(_.head._2)
      .values
      .toList

    val Point(x, y) = asteroidsToDestroy
      .map(a => (a, getAngle(bestAsteroid, a)))
      .sortBy { case (_, angle) => angle }
      .map { case (asteroid, _) => asteroid }
      .drop(199)
      .head

    x.toInt * 100 + y.toInt
  }

  private def asteroidsFromSpace(input: Space): Iterable[Point] = {
    for {
      y <- (0 until input.size)
      x <- (0 until input(0).size)
      if input(y)(x) == "#"
    } yield Point(x.toDouble, y.toDouble)
  }

  def getAngle(a: Point, b: Point): Double = {
    var angleRadians = (Math.atan2(a.y - b.y, a.x - b.x) * 180) / Math.PI
    if (angleRadians < 0) angleRadians += 360
    angleRadians -= 90
    if (angleRadians < 0) angleRadians += 360
    angleRadians
  }

  def getSlope(a: Point, b: Point): Slope = {
    val num = b.y - a.y
    val den = b.x - a.x

    if (num == 0) {
      if (b.x > a.x) PosInfinite else NegInfinite
    } else if (den == 0) {
      if (b.y > a.y) PosConstant else NegConstant
    } else {
      val slope = BigDecimal(num / den).setScale(10, BigDecimal.RoundingMode.HALF_UP).toDouble

      if (b.y > a.y) {
        PosReal(slope)
      } else {
        NegReal(slope)
      }
    }
  }

  def parseInput(): Space = {
    io.Source
      .fromResource("2019/day10/input.txt")
      .getLines()
      .toVector
      .map(_.split("").toVector)
  }
}
