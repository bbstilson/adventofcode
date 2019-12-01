import scala.io.Source

object Day4 {
  def main(args: Array[String]): Unit = {
    val input = parseInput()
    println(part1(input))
    println(part2(input))
  }

  def part1(phrases: List[List[String]]): Int = {
    phrases
      .map(_.toSet)
      .zip(phrases)
      .collect { case (xs, ys) if xs.size == ys.size => 1 }
      .sum
  }

  def part2(phrases: List[List[String]]): Int = phrases.filter(isValid).size

  private def isValid(words: List[String]): Boolean = {
    words.size == words.map(_.toSeq.sorted.unwrap).toSet.size
  }

  def parseInput(): List[List[String]] = {
    Source
      .fromFile("input.txt")
      .getLines.toList
      .map(_.split(" ").toList)
  }
}
