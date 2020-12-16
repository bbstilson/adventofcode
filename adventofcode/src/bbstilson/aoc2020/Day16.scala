package bbstilson.aoc2020

object Day16 extends aocd.Problem(2020, 16) {
  val RuleRegex = raw"(.+): (\d+)-(\d+) or (\d+)-(\d+)".r

  def parseTicketInfo(input: List[String]): Map[String, List[Range]] = {
    input
      .takeWhile(_.nonEmpty)
      .map { case RuleRegex(rule, x1, x2, y1, y2) =>
        rule -> List(x1.toInt to x2.toInt, y1.toInt to y2.toInt)
      }
      .toMap
  }

  private def parseLine(str: String): List[Int] = str
    .split(",")
    .map(_.toInt)
    .toList

  def run(input: List[String]): Unit = {
    val rules = parseTicketInfo(input)
    val myTicket = parseLine(
      input
        .dropWhile(_ != "your ticket:")
        .drop(1)
        .head
    )

    val tickets = input
      .dropWhile(_ != "nearby tickets:")
      .drop(1)
      .map(parseLine)

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
      val columnarTickets = (myTicket +: goodTickets).transpose.zipWithIndex
      val fieldToIndex = filterRules(columnarTickets, rules)
      val departureIndexes = fieldToIndex.collect {
        case (key, value) if key.startsWith("departure") => value
      }.toSet

      myTicket.zipWithIndex.collect {
        case (x, idx) if departureIndexes(idx) => {
          x.toLong
        }
      }.product
    }

  private def filterRules(
    columnarTickets: List[(List[Int], Int)],
    rules: Map[String, List[Range]]
  ): Map[String, Int] = {
    val (found, _) = columnarTickets
      .foldLeft((Map.empty[String, Int], Set.empty[String])) {
        case ((mappings, chosen), (colVals, colIndex)) => {
          colVals
            .foldLeft(rules -- chosen) { case (rs, value) =>
              rs.filter { case (_, ranges) => ranges.find(_.contains(value)).nonEmpty }
            }
            .keys
            .toList match {
            case field :: Nil => (mappings + (field -> colIndex), chosen + field)
            case _            => (mappings, chosen)
          }

        }
      }

    if (found.isEmpty) found
    else
      found ++ filterRules(
        columnarTickets.filterNot { case (_, idx) => found.values.toSet(idx) },
        rules -- found.keySet
      )
  }

  private def isValid(rs: List[Range])(xs: List[Int]): Boolean =
    xs.find(x => rs.find(_.contains(x)).isEmpty).isEmpty

}
