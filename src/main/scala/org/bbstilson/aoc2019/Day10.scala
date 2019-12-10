package org.bbstilson.aoc2019

import scala.math.BigDecimal

import scala.util._


object Day10 {

  type Space = Vector[Vector[String]]

  sealed trait Slope
  final case class PosReal(s: Double) extends Slope
  final case class NegReal(s: Double) extends Slope
  case object PosInfinite extends Slope
  case object NegInfinite extends Slope
  case object PosConstant extends Slope
  case object NegConstant extends Slope

  final case class Point(x: Double, y: Double) {
    def slope(o: Point): Slope = {
      val num = o.y - y
      val den = o.x - x

      if (num == 0) {
        if (o.x > x) PosInfinite else NegInfinite
      } else if (den == 0) {
        if (o.y > y) PosConstant else NegConstant
      } else {
        val slope = BigDecimal(num / den).setScale(10, BigDecimal.RoundingMode.HALF_UP).toDouble

        if (o.y > y) {
          PosReal(slope)
        } else {
          NegReal(slope)
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val input = parseInput()
    println(part1(input))
    println(part2(input))
  }

  def part1(input: Space): (Point, Int) = {
    val asteroids = for {
      y <- (0 until input.size)
      x <- (0 until input(0).size)
      if input(y)(x) == "#"
    } yield Point(x, y)

    val max = asteroids.map { currentAsteroid =>
      val dists = asteroids.collect {
        case a if a != currentAsteroid => currentAsteroid.slope(a)
      }.toSet.size

      (currentAsteroid, dists)
    }

    max.maxBy(_._2)
  }

  def part2(input: Space): Int = {
    val asteroids = for {
      y <- (0 until input.size)
      x <- (0 until input(0).size)
      if input(y)(x) == "#"
    } yield Point(x, y)

    val bestAsteroid = asteroids.map { currentAsteroid =>
      val dists = asteroids.collect {
        case a if a != currentAsteroid => currentAsteroid.slope(a)
      }.toSet.size

      (currentAsteroid, dists)
    }.maxBy(_._2)._1

    println(bestAsteroid)

    val asteroidsToDestroy = asteroids.collect {
      case a if a != bestAsteroid => (bestAsteroid.slope(a), a)
    }.groupBy(_._1).view.mapValues(_.head._2).values.toList

    val sorted = asteroidsToDestroy.map(a => (a, tan(bestAsteroid, a))).sortBy(_._2)

    println(s"found ${asteroidsToDestroy.size}")
    asteroidsToDestroy.take(10).foreach(a => println(tan(bestAsteroid, a)))
    println("~~")
    println(sorted.drop(199).head)

    1

  }

  def distance(a: Point, b: Point): Double = {
    Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2))
  }

  def tan(a: Point, b: Point): Double = {
    val side = distance(a, Point(a.x, b.y))
    val hypo = distance(a, b)

    Math.cos(side / hypo)
  }

  private def parseInput(): Space = {
    io.Source.fromResource("2019/day10/input.txt")
      .getLines.toVector
      .map(_.split("").toVector)
  }
}
