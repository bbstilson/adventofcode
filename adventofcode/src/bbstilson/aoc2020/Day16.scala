package bbstilson.aoc2020

object Day16 extends aocd.Problem(2020, 16) {

  def run(input: List[String]): Unit = {
    val rules = InputParsing.parseTicketInfo(input)
    val myTicket = input
      .dropWhile(_ != "your ticket:")
      .drop(1)
      .take(1)
      .map(InputParsing.parseLine)
      .head

    val tickets = input
      .dropWhile(_ != "nearby tickets:")
      .drop(1)
      .map(InputParsing.parseLine)

    part1(tickets, rules)
    part2(tickets, rules, myTicket)
    ()
  }

  def part1(tickets: List[List[Int]], rules: Map[String, List[Range]]): Int = part1 {
    val ranges = rules.values.toList.flatten
    tickets
      .flatMap(_.filter(value => ranges.find(range => range.contains(value)).isEmpty))
      .sum
  }

  def part2(tickets: List[List[Int]], rules: Map[String, List[Range]], myTicket: List[Int]): Long =
    part2 {
      val ranges = rules.values.toList.flatten
      val goodTickets = tickets.filter(isValid(ranges))
      val columnarTickets = (myTicket +: goodTickets).transpose.zipWithIndex.to(LazyList)
      val fieldToIndex = filterRules(columnarTickets, rules)
      val departureIndexes = fieldToIndex.collect {
        case (key, value) if key.startsWith("departure") => value
      }.toSet

      myTicket.zipWithIndex.collect { case (x, idx) if departureIndexes(idx) => x.toLong }.product
    }

  private def filterRules(
    columnarTickets: LazyList[(List[Int], Int)],
    rules: Map[String, List[Range]]
  ): Map[String, Int] =
    columnarTickets
      .map { case (column, idx) => (idx, columnToMatchingRules(column, rules)) }
      .collectFirst {
        case (idx, matched) if matched.size == 1 => (matched.head, idx)
      } match {
      case Some((field, index)) =>
        Map(field -> index) ++ filterRules(
          columnarTickets.filterNot { case (_, idx) => idx == index },
          rules - field
        )
      case None => Map.empty
    }

  private def columnToMatchingRules(
    column: List[Int],
    rules: Map[String, List[Range]]
  ): Set[String] =
    column
      .foldLeft(rules) { case (rs, value) =>
        rs.filter { case (_, ranges) => ranges.find(_.contains(value)).nonEmpty }
      }
      .keySet

  private def isValid(rs: List[Range])(xs: List[Int]): Boolean =
    xs.find(x => rs.find(_.contains(x)).isEmpty).isEmpty

}

object InputParsing {
  val RuleRegex = raw"(.+): (\d+)-(\d+) or (\d+)-(\d+)".r

  def parseTicketInfo(input: List[String]): Map[String, List[Range]] = {
    input
      .takeWhile(_.nonEmpty)
      .map { case RuleRegex(rule, x1, x2, y1, y2) =>
        rule -> List(x1.toInt to x2.toInt, y1.toInt to y2.toInt)
      }
      .toMap
  }

  def parseLine(str: String): List[Int] = str
    .split(",")
    .map(_.toInt)
    .toList
}
