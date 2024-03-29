package bbstilson.aoc2021

object Day2 extends aocd.Problem(2021, 2) {
  val ParseInputLine = """(forward|down|up) (\d+)""".r
  def run(input: List[String]): Unit = {
    val parsed: List[(String, Long)] = input.map { case ParseInputLine(direction, amt) =>
      (direction, amt.toLong)
    }

    part1(parsed)
    part2(parsed)

    ()
  }

  def part1(xs: List[(String, Long)]): Long = part1 {
    val (horizontal, depth) = xs.foldLeft((0L, 0L)) {
      case ((horizontal, depth), (direction, amt)) =>
        direction match {
          case "up"      => (horizontal, depth - amt)
          case "down"    => (horizontal, depth + amt)
          case "forward" => (horizontal + amt, depth)
        }
    }
    horizontal * depth
  }

  def part2(xs: List[(String, Long)]): Long = part2 {
    val (horizontal, depth, _) = xs.foldLeft((0L, 0L, 0L)) {
      case ((horizontal, depth, aim), (direction, amt)) =>
        direction match {
          case "up"      => (horizontal, depth, aim - amt)
          case "down"    => (horizontal, depth, aim + amt)
          case "forward" => (horizontal + amt, depth + (aim * amt), aim)
        }

    }
    horizontal * depth
  }
}
