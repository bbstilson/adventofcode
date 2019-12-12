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
    println(part2(moons))
  }

  def part1(moons: List[Moon]): Int = {
    Iterator.iterate(moons)(step).drop(1000).next.map(energy).sum
  }

  def part2(moons: List[Moon]): Unit = {
    val m0 = findSame(0, moons(0), moons)
    println(m0)
    val m1 = findSame(1, moons(1), moons)
    println(m1)
    val m2 = findSame(2, moons(2), moons)
    println(m2)
    val m3 = findSame(3, moons(3), moons)
    println(m3)
    // moons
    //   .zipWithIndex
    //   .map { case (moon, idx) => findSame(idx, moon, moons) }
    //   .foreach(println)
  }

  def findSame(idx: Int, moon: Moon, moons: List[Moon]): (List[Moon], Int) = {
    var found = false
    var count = 1
    var stepped = step(moons)
    while(!found) {
      if (stepped(idx) == moon) {
        found = true
      } else {
        stepped = step(stepped)
        count += 1
      }
    }

    (stepped, count)
    // Iterator
    //   .iterate(init){ case (moons, c) => (step(moons), c + 1) }
    //   .dropWhile { case (moons, count) => moons(idx) != moon }
    //   .next
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

  // def stepMoon(moon: Moon, otherMoons: List[Moon]): Moon = {
  //   val Point(x,y,z) = moon.pos
  //   val Velocity(vX,vY,vZ) = moon.vel

  //   val xChange = otherMoons.map(_.pos.x).map(x => calc(moon.pos.x, x)).sum
  //   val yChange = otherMoons.map(_.pos.y).map(y => calc(moon.pos.y, y)).sum
  //   val zChange = otherMoons.map(_.pos.z).map(z => calc(moon.pos.z, z)).sum

  //   val nextVel = Velocity(
  //     x = moon.vel.x + xChange,
  //     y = moon.vel.y + yChange,
  //     z = moon.vel.z + zChange,
  //   )
  //   val nextPos = Point(
  //     x = moon.pos.x + nextVel.x,
  //     y = moon.pos.y + nextVel.y,
  //     z = moon.pos.z + nextVel.z,
  //   )

  //   Moon(nextPos, nextVel)
  // }

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
