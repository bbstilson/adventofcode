package org.bbstilson.aoc2019

object Day8 {
  def main(args: Array[String]): Unit = {
    val input = parseInput()

    println(part1(input))
    part2(input, 25, 6)
  }

  def part1(data: List[Int]): Int = {
    val (layer, _) = data
      .sliding(150, 150).toList
      .map { layer => (layer.mkString(""), layer.filter(_ > 0)) }
      .toMap
      .map { case (str, xs) => (str, xs.size) }
      .maxBy { case (_, fewestZeros) => fewestZeros }

    layer.count(_ == '1') * layer.count(_ == '2')
  }

  def part2(data: List[Int], width: Int, height: Int): Unit = {
    val line = List.fill(width)("****").mkString("") + "**"
    val layerLen = width * height

    val message = data
      .sliding(layerLen, layerLen)
      .toList
      .transpose
      .map(_.find(_ < 2).get)
      .sliding(width, width)

    println(line)
    message.foreach(xs => {
      print("*")
      xs.foreach(x => if (x == 0) print("****") else print("    "))
      print("*")
      println("")
    })
    println(line)
  }

  private def parseInput(): List[Int] = {
    io.Source.fromResource("2019/day8/input.txt")
      .getLines.toList
      .head
      .split("").toList
      .map(_.toInt)
  }
}
