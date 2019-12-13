package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.utils.MathUtils.lcm
import scala.annotation.tailrec

object Day12 {
  final case class Point(x: Int, y: Int, z: Int)
  final case class Velocity(x: Int, y: Int, z: Int)
  final case class Moon(pos: Point, vel: Velocity = Velocity.empty)

  object Velocity {
    def empty = Velocity(0,0,0)
  }

  def main(args: Array[String]): Unit = {
    val moons = parseMoons()
    // println(part1(moons))
    println(part2(moons))
  }

  def part1(moons: List[Moon]): Int = {
    Iterator.iterate(moons)(step).drop(1000).next.map(energy).sum
  }

  def part2(moons: List[Moon]): Int = {
    val (x, y, z) = memoStep(moons)
    println(x, y ,z)
    lcm(x, lcm(y, z))
  }

  def calc(v: Int, o: Int): Int = if (o > v) 1 else if (o == v) 0 else -1

  def energy(moon: Moon): Int = {
    val Point(x,y,z) = moon.pos
    val Velocity(vX,vY,vZ) = moon.vel
    val kin = List(x,y,z).map(Math.abs).sum
    val pot = List(vX,vY,vZ).map(Math.abs).sum
    kin * pot
  }

  def step(moons: List[Moon]): List[Moon] = {
    moons.map { moon =>
      val otherMoons = moons.filter(_ != moon)
      val o1 = otherMoons(0)
      val o2 = otherMoons(1)
      val o3 = otherMoons(2)
      val Point(x,y,z) = moon.pos
      val Velocity(vX,vY,vZ) = moon.vel

      val xChange = calc(x, o1.pos.x) + calc(x, o2.pos.x) + calc(x, o3.pos.x)
      val yChange = calc(y, o1.pos.y) + calc(y, o2.pos.y) + calc(y, o3.pos.y)
      val zChange = calc(z, o1.pos.z) + calc(z, o2.pos.z) + calc(z, o3.pos.z)

      val nextVel = Velocity(moon.vel.x + xChange, moon.vel.y + yChange, moon.vel.z + zChange)
      val nextPos = Point(moon.pos.x + nextVel.x, moon.pos.y + nextVel.y, moon.pos.z + nextVel.z)

      Moon(nextPos, nextVel)
    }
  }

  @tailrec
  def memoStep(
    moons: List[Moon],
    memos: (Set[String], Set[String], Set[String]) = (Set.empty, Set.empty, Set.empty),
    founds: (Option[Int], Option[Int], Option[Int]) = (None, None, None),
    count: Int = 1
  ): (Int, Int, Int) = {
    val stepped = step(moons)

    val xs = stepped.map(_.pos.x).mkString("|")
    val ys = stepped.map(_.pos.y).mkString("|")
    val zs = stepped.map(_.pos.z).mkString("|")

    val (mX, mY, mZ) = memos
    val optX = if (mX(xs)) Some(count) else None
    val optY = if (mY(ys)) Some(count) else None
    val optZ = if (mZ(zs)) Some(count) else None

    founds match {
      case (Some(x), Some(y), Some(z)) => (x, y, z)
      case (x @ Some(_), None, None) =>
        memoStep(stepped, (mX, mY + ys, mZ + zs), (x, optY, optZ), count + 1)
      case (None, y @ Some(_), None) =>
        memoStep(stepped, (mX + xs, mY, mZ + zs), (optX, y, optZ), count + 1)
      case (None, None, z @ Some(_)) =>
        memoStep(stepped, (mX + xs, mY + ys, mZ), (optX, optY, z), count + 1)
      case (x @ Some(_), y @ Some(_), None) =>
        memoStep(stepped, (mX, mY, mZ + zs), (x, y, optZ), count + 1)
      case (x @ Some(_), None, z @ Some(_)) =>
        memoStep(stepped, (mX, mY + ys, mZ), (x, optY, z), count + 1)
      case (None, y @ Some(_), z @ Some(_)) =>
        memoStep(stepped, (mX + xs, mY, mZ), (optX, y, z), count + 1)
      case (None, None, None) =>
        memoStep(stepped, (mX + xs, mY + ys, mZ + zs), (optX, optY, optZ), count + 1)
    }

  }

  def parseMoons(): List[Moon] = {
    List(
      Moon(Point(-15,1,4)),
      Moon(Point(1,-10,-8)),
      Moon(Point(-5,4,9)),
      Moon(Point(4,6,-2)),
    )
  }
}
