package bbstilson.aoc2021

object Day10 extends aocd.Problem(2021, 10) {
  sealed trait Program
  case object Valid extends Program
  case class Invalid(expected: Char, actual: Char) extends Program
  case class Incomplete(missing: List[Char]) extends Program

  val TOKENS = Map(
    '[' -> ']',
    '(' -> ')',
    '{' -> '}',
    '<' -> '>'
  )

  val INVALID_TOKEN_POINTS = Map(
    ')' -> 3,
    ']' -> 57,
    '}' -> 1197,
    '>' -> 25137
  )

  val MISSING_TOKEN_POINTS = Map(
    ')' -> 1,
    ']' -> 2,
    '}' -> 3,
    '>' -> 4
  )

  def run(programs: List[String]): Unit = {

    val gradedPrograms = programs.map { program => validate(program.toCharArray().toList) }

    part1 {
      gradedPrograms.collect { case Invalid(_, actual) =>
        INVALID_TOKEN_POINTS(actual)
      }.sum
    }

    part2 {
      def scoreMissingTokens(missing: List[Char]): Long = missing
        .foldLeft(0L) { case (score, token) =>
          (score * 5) + MISSING_TOKEN_POINTS(token)
        }

      val incompleteScores = gradedPrograms
        .collect { case Incomplete(missing) => missing }
        .map(scoreMissingTokens)
        .sorted
        .toVector

      incompleteScores(incompleteScores.size / 2)
    }

    ()
  }

  def validate(program: List[Char], stack: List[Char] = List.empty): Program = {
    program match {
      case curTok :: rest =>
        // is left token
        if (TOKENS.contains(curTok)) {
          validate(rest, curTok +: stack)
        } else {
          // is right token
          stack match {
            case tok :: nextStack =>
              // is left token and the right token is the current token
              if (TOKENS.contains(tok) && TOKENS(tok) == curTok) {
                validate(rest, nextStack)
              } else {
                Invalid(TOKENS(tok), curTok)
              }
            case Nil => Invalid('_', curTok)
          }
        }
      case Nil if stack.nonEmpty => Incomplete(stack.map(TOKENS))
      case Nil                   => Valid
    }
  }
}
