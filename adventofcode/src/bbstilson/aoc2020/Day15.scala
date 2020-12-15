package bbstilson.aoc2020

object Day15 extends aocd.Problem(2020, 15) {

  def run(input: List[String]): Unit = {
    val parsed = input.head.split(",").toList.map(_.toInt)
    playGame(parsed, 2020)
    playGame(parsed, 30000000)
    ()
  }

  case class Game(memory: Map[Int, List[Int]], lastSaid: Int, turn: Int)

  def playGame(starting: List[Int], target: Int): Int = part1 {
    val init = Game(
      memory = starting.init.zipWithIndex.map { case (n, idx) => n -> List((idx + 1)) }.toMap,
      lastSaid = starting.last,
      turn = starting.size + 1
    )

    LazyList
      .iterate(init)(playRound)
      .drop(target - starting.size)
      .head
      .lastSaid
  }

  private def playRound(game: Game): Game = {
    val (nextMem, justSaid) = game.memory.get(game.lastSaid) match {
      case Some(lastSaidStack) =>
        (
          game.lastSaid -> ((game.turn - 1) +: lastSaidStack.take(1)),
          (game.turn - 1) - lastSaidStack.head
        )
      case None => (game.lastSaid -> List(game.turn - 1), 0)
    }

    game.copy(
      memory = game.memory + nextMem,
      lastSaid = justSaid,
      turn = game.turn + 1
    )
  }
}
