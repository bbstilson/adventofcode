package org.bbstilson.aoc2018

object Day5Part1 {

  def main(args: Array[String]): Unit = {
    val source: List[Char] = io.Source
      .fromResource("2018/day5/input.txt")
      .toList
      .filter(_.isLetter)

    println(deletePairs(source))
  }

  private def deletePairs(source: Seq[Char]): Int = {
    val init = source.head
    val rest = source.tail

    rest
      .foldLeft(Seq(init)) {
        case (passed, char) => {
          passed match {
            // We've exhausted our passed list. Repopulate it and continue.
            case Nil => Seq(char)
            case Seq(as @ _*) => {
              if (isMatch(as.last, char)) {
                // Remove the last letter.
                as.init
              } else {
                // Not a match. Append this character and continue.
                as :+ char
              }
            }
          }
        }
      }
      .mkString
      .size
  }

  // a and A match OR A and a
  private def isMatch(a: Char, b: Char): Boolean = {
    (a.isLower && b.isUpper && a.toUpper == b) ||
    (a.isUpper && b.isLower && a.toLower == b)
  }
}
