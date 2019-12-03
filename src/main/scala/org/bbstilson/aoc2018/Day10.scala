package org.bbstilson.aoc2018

object Day10 {

  case class Point(x: Long, y: Long)
  case class Light(position: Point, velocity: Point)
  case class BoundingBox(lowerLeft: Point, topRight: Point)

  def main(args: Array[String]): Unit = {
    val lights: List[Light] = io.Source
      .fromResource("2018/day10/input.txt")
      .getLines
      .toList
      .map(parseLight _)

    val (stepsTilLetters, finalLights) = fly(lights)

    println(stepsTilLetters)
    printLights(finalLights)
  }

  private def fly(lights: List[Light]): (Long, List[Light]) = {
    val initBoundingBoxArea = getBoundingBoxArea(lights.map(_.position))
    fly(lights, 0L, initBoundingBoxArea)
  }
  private def fly(
    lights: List[Light],
    stepNum: Long,
    boundingBoxArea: Long
  ): (Long, List[Light]) = {
    // move lights
    val nextLights = lights.map(moveLight)
    // get new BoundingBox area
    val nextBoundingBoxArea = getBoundingBoxArea(nextLights.map(_.position))
    // if smaller continue. else, return stepNum
    if (nextBoundingBoxArea < boundingBoxArea) {
      fly(nextLights, stepNum + 1, nextBoundingBoxArea)
    } else {
      (stepNum, lights)
    }
  }

  private def moveLight(light: Light): Light = {
    val lastPos = light.position
    val velocity = light.velocity
    val nextPos = Point(lastPos.x + velocity.x, lastPos.y + velocity.y)
    Light(nextPos, light.velocity)
  }

  private def printLights(ls: List[Light]): Unit = {
    val positions = ls.map(_.position)
    val bb = getBoundingBox(positions)
    val lightSet = positions.toSet

    val width = bb.topRight.x - bb.lowerLeft.x
    val height = bb.topRight.y - bb.lowerLeft.y
    val beginX = bb.lowerLeft.x
    val endX = bb.lowerLeft.x + width
    val beginY = bb.lowerLeft.y
    val endY = bb.lowerLeft.y + height

    (beginY to endY).foreach({ y =>
      (beginX to endX).foreach({ x =>
        val printChar = if (lightSet.contains(Point(x, y))) "#" else "."
        print(printChar)
      })
      println()
    })
  }

  private lazy val lightRegex =
    // position=< 31351, -51811> velocity=<-3,  5>
    """position=<\s*(-?\d+),\s*(-?\d+)> velocity=<\s*(-?\d+),\s*(-?\d+)>""".r
  private def parseLight(line: String): Light = {
    val lightRegex(posX, posY, velX, velY) = line

    val pX = posX.toLong
    val pY = posY.toLong
    val vX = velX.toLong
    val vY = velY.toLong

    Light(Point(pX, pY), Point(vX, vY))
  }

  def getBoundingBox(ps: List[Point]): BoundingBox = {
    val xs = ps.map(_.x)
    val ys = ps.map(_.y)

    BoundingBox(Point(xs.min, ys.min), Point(xs.max, ys.max))
  }

  def getBoundingBoxArea(bb: BoundingBox): Long = {
    val width = bb.topRight.x - bb.lowerLeft.x
    val height = bb.topRight.y - bb.lowerLeft.y
    width * height
  }

  def getBoundingBoxArea(ps: List[Point]): Long = {
    getBoundingBoxArea(getBoundingBox(ps))
  }
}
