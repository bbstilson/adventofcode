package bbstilson.aoc2017

object Day2 {

  def main(args: Array[String]): Unit = {
    val spreadsheet = parseInput()
    println(part1(spreadsheet))
    println(part2(spreadsheet))
  }

  def part1(spreadsheet: List[List[Int]]): Int = spreadsheet.map(getDiff).sum

  def part2(spreadsheet: List[List[Int]]): Int = {
    spreadsheet.map { xs =>
      val pairs = for {
        x <- xs
        y <- xs
        if (x != y)
      } yield (x, y)

      pairs.collectFirst {
        case (x, y) if x % y == 0 => x / y
      }.get
    }.sum
  }

  def getDiff(xs: List[Int]): Int = {
    val (min, max) = xs.foldLeft((Int.MaxValue, Int.MinValue)) { case ((min, max), x) =>
      (Math.min(min, x), Math.max(max, x))
    }
    max - min
  }

  private def parseInput(): List[List[Int]] = io.Source
    .fromResource("2017/day2/input.txt")
    .getLines()
    .toList
    .map(_.split("\\s+").toList)
    .map(_.map(_.toInt))
}
