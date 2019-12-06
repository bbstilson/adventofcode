package org.bbstilson.aoc2019

import scala.annotation.tailrec

object Day6 {

  def main(args: Array[String]): Unit = {
    val orbits = parseOrbits()
    println(part1(orbits))
    println(part2(orbits))
  }

  def part1(orbits: Map[String, String]): Int = {
    orbits.keys.toList.map(moon => sumOrbits(moon, orbits, 0)).sum
  }

  def part2(orbits: Map[String, String]): Int = {
    val myPath = buildPathToCenterPlanet("YOU", orbits)
    val santaPath = buildPathToCenterPlanet("SAN", orbits)

    // Get only the planets that both touch on the way to the root planet.
    val sharedPlanets = myPath.toMap.keySet.intersect(santaPath.toMap.keySet)

    // Filter down the original paths so that we can get the distances it
    // took to get to those planets.
    val mySharedPath = myPath.filter { case (planet, _) => sharedPlanets.contains(planet) }
    val santaSharedPath = santaPath.filter { case (planet, _) => sharedPlanets.contains(planet) }

    // Build a map from planet -> total distance from both planets.
    // Then, find the smallest distance.
    (mySharedPath ++ santaSharedPath)
      .groupMapReduce(_._1)(_._2)(_ + _)
      .values
      .min
  }

  @tailrec
  private def sumOrbits(moon: String, orbits: Map[String, String], orbitCount: Int): Int = {
    orbits.get(moon) match {
      case Some(planet) => sumOrbits(planet, orbits, orbitCount + 1)
      case None => orbitCount
    }
  }

  @tailrec
  private def buildPathToCenterPlanet(
    moon: String,
    orbits: Map[String, String],
    dist: Int = -1,
    path: List[(String, Int)] = Nil
  ): List[(String, Int)] = {
    orbits.get(moon) match {
      case Some(planet) => buildPathToCenterPlanet(planet, orbits, dist + 1, (moon, dist) +: path)
      case None => (moon, dist) +: path
    }
  }

  private def parseOrbits(): Map[String, String] = {
    io.Source
      .fromResource("2019/day6/input.txt")
      .getLines.toList
      .map(_.split("\\)", 2).toList)
      .map {
        case planet :: moon :: Nil => (moon, planet)
        case _ => throw new Error("WOAH")
      }.toMap
  }
}
