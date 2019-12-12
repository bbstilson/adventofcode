package org.bbstilson.aoc2019

object Day12 {
  final case class Point(x: Int, y: Int, z: Int)
  final case class Velocity(x: Int, y: Int, z: Int)
  final case class Moon(pos: Point, vel: Velocity = Velocity.empty)

  object Velocity {
    def empty = Velocity(0,0,0)
  }

  def main(args: Array[String]): Unit = {
    val moons = parseMoons()
    println(part1(moons))
  }

  def part1(moons: List[Moon]): Int = {
    Iterator.iterate(moons)(step).drop(1000).next.map(energy).sum
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
    moons.map { case moon =>
      val Point(x,y,z) = moon.pos
      val Velocity(vX,vY,vZ) = moon.vel

      val moonsToCompare = moons.filter(_ != moon)
      val xChange = moonsToCompare.map(_.pos.x).map(x => calc(moon.pos.x, x)).sum
      val yChange = moonsToCompare.map(_.pos.y).map(y => calc(moon.pos.y, y)).sum
      val zChange = moonsToCompare.map(_.pos.z).map(z => calc(moon.pos.z, z)).sum

      val nextVel = Velocity(
        x = moon.vel.x + xChange,
        y = moon.vel.y + yChange,
        z = moon.vel.z + zChange,
      )
      val nextPos = Point(
        x = moon.pos.x + nextVel.x,
        y = moon.pos.y + nextVel.y,
        z = moon.pos.z + nextVel.z,
      )

      Moon(nextPos, nextVel)
    }
  }

  def parseMoons(): List[Moon] = {
    // <x=-15, y=1, z=4>
    // <x=1, y=-10, z=-8>
    // <x=-5, y=4, z=9>
    // <x=4, y=6, z=-2>
    List(
      Moon(Point(-15,1,4)),
      Moon(Point(1,-10,-8)),
      Moon(Point(-5,4,9)),
      Moon(Point(4,6,-2)),
    )
  }
}
