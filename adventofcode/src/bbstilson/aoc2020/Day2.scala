package bbstilson.aoc2020

case class Password(min: Int, max: Int, letter: Char, pw: String)

object Input {

  val lineRegex = """(\d+)\-(\d+) ([a-z]): ([a-z]+)""".r

  def parseLine(str: String): Password = str match {
    case lineRegex(min, max, char, pw) =>
      Password(min.toInt, max.toInt, char.toCharArray().head, pw)
    case _ => Password(0, 0, 'a', "")
  }
}

object Day2 extends bbstilson.Problem("day2", Input.parseLine) {

  def run(input: List[Password]): Unit = {
    println(input.filter(isValidPart1).size)
    println(input.filter(isValidPart2).size)
  }

  private def isValidPart1(pw: Password): Boolean = {
    val numChar = pw.pw.filter(_ == pw.letter).size
    numChar >= pw.min && numChar <= pw.max
  }

  private def isValidPart2(pw: Password): Boolean = {
    (pw.pw(pw.min - 1) == pw.letter || pw.pw(pw.max - 1) == pw.letter) && !(pw.pw(
      pw.min - 1
    ) == pw.letter && pw.pw(pw.max - 1) == pw.letter)
  }
}
