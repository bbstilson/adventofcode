
package org.bbstilson

object Day3 {
  type Coord = (Int, Int)
  type CoordMap = Map[Coord, List[Int]]
  type CoordMap2 = Map[Coord, List[(Int, Int)]]

  def main(args: Array[String]): Unit = {
    val input = parseInput()

    println(part1(input))
    println(part2(input))
  }

  def part1(input: List[(List[String], Int)]): Int = {
    val initMap = Map((0,0) -> List(-1))
    val coords = input.foldLeft(initMap) { case (cMap, (line, id)) =>
      line.foldLeft((cMap, (0,0))) { case ((map, pos), cmd) =>
        val (x, y) = pos
        val direction = cmd.head
        val steps = cmd.drop(1).toInt

        direction match {
          case 'L' => {
            val nextPos = (x - steps, y)
            val nextMap = updateGridMap(id, steps, map, s => (x - s, y))
            (nextMap, nextPos)
          }
          case 'R' => {
            val nextPos = (x + steps, y)
            val nextMap = updateGridMap(id, steps, map, s => (x + s, y))
            (nextMap, nextPos)
          }
          case 'U' => {
            val nextPos = (x, y + steps)
            val nextMap = updateGridMap(id, steps, map, s => (x, y + s))
            (nextMap, nextPos)
          }
          case 'D' => {
            val nextPos = (x, y - steps)
            val nextMap = updateGridMap(id, steps, map, s => (x, y - s))
            (nextMap, nextPos)
          }
        }
      }._1
    }

    coords
      .filter { case (coord, xs) => xs.size == 2 && coord != (0,0) }
      .keys
      .map { case (x, y) => Math.abs(x) + Math.abs(y) }
      .min
  }

  private def updateGridMap(id: Int, steps: Int, cMap: CoordMap, f: Int => Coord): CoordMap = {
    (0 until steps).foldLeft(cMap) { case (map, s) =>
      val coord = f(s)
      map.get(coord) match {
        case Some(xs) if xs.head == id => map
        case Some(xs)                  => map + (coord -> (id +: xs))
        case None                      => map + (coord -> List(id))
      }
    }
  }

  def part2(input: List[(List[String], Int)]): Int = {
    val initMap = Map((0,0) -> List((-1, 0)))
    val coords = input.foldLeft(initMap) { case (cMap, (line, id)) =>
      line.foldLeft((cMap, (0,0), 0)) { case ((map, pos, stepsTaken), cmd) =>
        val (x, y) = pos
        val direction = cmd.head
        val steps = cmd.drop(1).toInt
        val nextStepsTaken = stepsTaken + steps
        direction match {
          case 'L' => {
            val nextPos = (x - steps, y)
            val nextMap = updateGridMap2(id, stepsTaken, steps, map, s => (x - s, y))
            (nextMap, nextPos, nextStepsTaken)
          }
          case 'R' => {
            val nextPos = (x + steps, y)
            val nextMap = updateGridMap2(id, stepsTaken, steps, map, s => (x + s, y))
            (nextMap, nextPos, nextStepsTaken)
          }
          case 'U' => {
            val nextPos = (x, y + steps)
            val nextMap = updateGridMap2(id, stepsTaken, steps, map, s => (x, y + s))
            (nextMap, nextPos, nextStepsTaken)
          }
          case 'D' => {
            val nextPos = (x, y - steps)
            val nextMap = updateGridMap2(id, stepsTaken, steps, map, s => (x, y - s))
            (nextMap, nextPos, nextStepsTaken)
          }
        }
      }._1
    }

    coords
      .filter { case (coord, xs) => xs.size == 2 && coord != (0,0) }
      .values
      .minBy(_.map(_._2).sum)
      .map(_._2)
      .sum
  }

  private def updateGridMap2(
    id: Int,
    stepsTaken: Int,
    steps: Int,
    cMap: CoordMap2,
    f: Int => Coord
  ): CoordMap2 = {
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

  def parseInput(): List[(List[String], Int)] = {
    io.Source.fromResource("day3/input.txt").getLines.toList.map(_.split(",").toList).zipWithIndex
  }
}
