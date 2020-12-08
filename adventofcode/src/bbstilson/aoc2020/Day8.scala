package bbstilson.aoc2020

object Day8 extends aocd.Problem(2020, 8) {
  val ProgramRegex = raw"(.{3}) ([-+]\d+)".r

  def run(input: List[String]): Unit = {
    implicit val instructions = input.toVector
      .map { case ProgramRegex(program, mod) => (program, mod.toInt) }

    println(part1)
    println(part2)
  }

  def part1(implicit instructions: Vector[(String, Int)]): Int = runProgram(0, 0, -1, Set.empty)._1

  def part2(implicit instructions: Vector[(String, Int)]): Int = instructions
    .to(Iterator)
    .zipWithIndex
    .collect { case ((cmd, _), idx) if cmd == "jmp" || cmd == "nop" => idx }
    .map(swap => runProgram(0, 0, swap, Set.empty))
    .dropWhile { case (_, found) => !found }
    .next()
    ._1

  private def runProgram(index: Int, accumulator: Int, swap: Int, seen: Set[Int])(implicit
    instructions: Vector[(String, Int)]
  ): (Int, Boolean) = {
    if (!seen.contains(index) && index < instructions.size) {
      val nextSeen = seen + index
      instructions(index) match {
        case (program, mod) =>
          program match {
            case "acc"                  => runProgram(index + 1, accumulator + mod, swap, nextSeen)
            case "jmp" if index == swap => runProgram(index + 1, accumulator, swap, nextSeen)
            case "nop" if index == swap => runProgram(index + mod, accumulator, swap, nextSeen)
            case "jmp"                  => runProgram(index + mod, accumulator, swap, nextSeen)
            case "nop"                  => runProgram(index + 1, accumulator, swap, nextSeen)
          }
      }
    } else if (index >= instructions.size) {
      (accumulator, true)
    } else {
      (accumulator, false)
    }
  }
}
