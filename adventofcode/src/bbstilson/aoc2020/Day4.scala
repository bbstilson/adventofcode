package bbstilson.aoc2020

import aocd._

object Day4 extends Problem(2020, 4) {

  def run(input: List[String]): Unit = {
    val passports = Parse.parseInput(input)
    val part1 = passports.filter(hasAllFields)
    val part2 = part1.filter(hasValidData)

    println(part1.size)
    println(part2.size)
  }

  private def hasAllFields(p: Map[String, String]): Boolean = {
    val hasAllFields = p.keySet.size == Rules.fieldRules.size
    val missingOnlyCid = p.keySet.size == (Rules.fieldRules.size - 1) && p.get("cid").isEmpty
    hasAllFields || missingOnlyCid
  }

  private def hasValidData(p: Map[String, String]): Boolean = {
    Rules.fieldRules.forall { case (field, rule) =>
      p.get(field).map(rule).getOrElse(true)
    }
  }
}

object Parse {
  val PairRegex = """(.+):(.+)""".r

  def parseInput(input: List[String]): List[Map[String, String]] = {
    val (passports, carry) = input
      .map(_.trim())
      .foldLeft((List.empty[Map[String, String]], Map.empty[String, String])) {
        case ((data, carry), line) =>
          line match {
            case "" => (carry +: data, Map.empty)
            case _  => (data, carry ++ parseLine(line))
          }
      }

    carry +: passports
  }

  private def parseLine(line: String): Map[String, String] = {
    line
      .split(" ")
      .map { case PairRegex(k, v) => k -> v }
      .toMap
  }
}

object Rules {
  val HeightPattern = """(\d+)(.+)""".r
  //  # followed by exactly six characters 0-9 or a-f.
  val HairColorPattern = """#[a-f0-9]{6}""".r
  // a nine-digit number, including leading zeroes.
  val PidPattern = """\d{9}""".r
  val ValidEyeColors = Set("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

  private def inRange(s: Int, e: Int)(n: String): Boolean = (s to e).contains(n.toInt)

  private def hgt(s: String): Boolean = s match {
    case HeightPattern(h, t) => if (t == "cm") inRange(150, 193)(h) else inRange(59, 76)(h)
  }

  val fieldRules: List[(String, String => Boolean)] = List(
    "byr" -> inRange(1920, 2002),
    "iyr" -> inRange(2010, 2020),
    "eyr" -> inRange(2020, 2030),
    "hgt" -> hgt,
    "hcl" -> HairColorPattern.matches,
    "ecl" -> ValidEyeColors,
    "pid" -> PidPattern.matches,
    "cid" -> (_ => true)
  )
}
