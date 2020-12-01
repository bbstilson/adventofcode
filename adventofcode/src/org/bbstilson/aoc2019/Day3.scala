package org.bbstilson.aoc2019

object Day3 {
  type Coord = (Int, Int)
  type CoordMap = Map[Coord, List[(Int, Int)]]

  def main(args: Array[String]): Unit = {
    val input = parseInput()
    println(part1(input))
    println(part2(input.zipWithIndex))
  }

  def part1(input: List[List[String]]): Int = {
    val init: (Coord, List[Coord]) = ((0, 0), List.empty[Coord])
    val line1 = input.head.foldLeft(init)(mkCoords)._2.toSet
    val line2 = input.last.foldLeft(init)(mkCoords)._2.toSet

    val (x, y) = line1
      .intersect(line2)
      .minBy { case (x, y) => Math.abs(x) + Math.abs(y) }

    x + y
  }

  def part2(input: List[(List[String], Int)]): Int = {
    val initMap: Map[Coord, List[Coord]] = Map((0, 0) -> List((-1, 0)))
    val coords = input.foldLeft(initMap) { case (cMap, (line, id)) =>
      line
        .foldLeft((cMap, (0, 0), 0)) { case ((map, pos, stepsTaken), cmd) =>
          val steps = cmd.drop(1).toInt
          val f = getDirectionFunction(pos, cmd.head)
          val nextMap = updateGridMap(id, stepsTaken, steps, map, f)
          val nextPos = f(steps)
          val nextStepsTaken = stepsTaken + steps

          (nextMap, nextPos, nextStepsTaken)
        }
        ._1
    }

    coords
      .filter { case (coord, xs) => xs.size == 2 && coord != (0 -> 0) }
      .values
      .minBy(_.map(_._2).sum)
      .map(_._2)
      .sum
  }

  private def mkCoords(carry: (Coord, List[Coord]), cmd: String): (Coord, List[Coord]) = {
    val (pos, xs) = carry
    val f = getDirectionFunction(pos, cmd.head)
    val steps = cmd.drop(1).toInt
    (f(steps), (1 to steps).map(f).toList ++ xs)
  }

  private def getDirectionFunction(pos: Coord, direction: Char): Int => Coord = {
    val (x, y) = pos
    direction match {
      case 'L' => (s: Int) => (x - s, y)
      case 'R' => (s: Int) => (x + s, y)
      case 'U' => (s: Int) => (x, y + s)
      case 'D' => (s: Int) => (x, y - s)
    }
  }

  private def updateGridMap(
    id: Int,
    stepsTaken: Int,
    steps: Int,
    cMap: CoordMap,
    f: Int => Coord
  ): CoordMap = {
    (1 to steps).foldLeft(cMap) { case (map, s) =>
      val coord = f(s)
      val dist = s + stepsTaken
      map.get(coord) match {
        case Some(xs) if xs.head._1 == id => map
        case Some(xs)                     => map + (coord -> ((id, dist) +: xs))
        case None                         => map + (coord -> List((id, dist)))
      }
    }
  }

  def parseInput(): List[List[String]] = {
    io.Source.fromResource("2019/day3/input.txt").getLines().toList.map(_.split(",").toList)
  }
}
