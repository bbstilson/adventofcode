package bbstilson.aoc2020

import bbstilson.implicits.ListImplicits._

import scala.collection.immutable.Queue

object Day22 extends aocd.Problem(2020, 22) {

  val PlayerRegex = raw"Player (\d+):".r

  case class Game(
    player1: Queue[Int],
    player2: Queue[Int],
    player1Hands: Set[Int] = Set.empty,
    player2Hands: Set[Int] = Set.empty
  )

  object Game {

    def fromInput(input: List[String]): Game = {
      val players: Map[Int, Queue[Int]] = input
        .groupedBy(_.nonEmpty)
        .map { xs =>
          xs.head match {
            case PlayerRegex(player) => player.toInt -> Queue.from(xs.tail.map(_.toInt))
          }
        }
        .toMap

      Game(players(1), players(2))

    }
  }

  def run(input: List[String]): Unit = {
    val start = Game.fromInput(input)
    part1(start)
    part2(start)
    ()
  }

  def part1(start: Game): Long = part1 {
    val game = playGame(start, playRound)
    makeScore(if (game.player1.isEmpty) game.player2 else game.player1)
  }

  def part2(start: Game): Long = part2 {
    val game = playGame(start, playRoundRecursive)
    makeScore(if (game.player1.isEmpty) game.player2 else game.player1)
  }

  def playGame(init: Game, rules: Game => Game): Game = LazyList
    .iterate(init)(rules)
    .dropWhile(g => !(g.player1.isEmpty || g.player2.isEmpty))
    .head

  def makeScore(winningDeck: Queue[Int]): Long = {
    winningDeck
      .foldLeft(List.empty[Int]) { case (stack, card) =>
        card +: stack
      }
      .zipWithIndex
      .map { case (card, point) => (card * (point + 1)).toLong }
      .sum
  }

  def playRound(game: Game): Game = {
    val (p1Card, p1Q) = game.player1.dequeue
    val (p2Card, p2Q) = game.player2.dequeue
    game.copy(
      player1 = nextQueue(p1Card > p2Card, p1Q, List(p1Card, p2Card)),
      player2 = nextQueue(p1Card < p2Card, p2Q, List(p2Card, p1Card))
    )
  }

  def playRoundRecursive(game: Game): Game = {
    val (p1Card, p1Q) = game.player1.dequeue
    val (p2Card, p2Q) = game.player2.dequeue
    val player1Hash = game.player1.hashCode()
    val player2Hash = game.player2.hashCode()

    if (game.player1Hands.contains(player1Hash) && game.player2Hands.contains(player2Hash)) {
      game.copy(
        player2 = Queue.empty
      )
    } else if (p1Q.size >= p1Card && p2Q.size >= p2Card) {
      val subGame = playGame(
        Game(player1 = p1Q.take(p1Card), player2 = p2Q.take(p2Card)),
        playRoundRecursive
      )
      val p1Wins = subGame.player1.nonEmpty && subGame.player2.isEmpty
      game.copy(
        player1 = nextQueue(p1Wins, p1Q, List(p1Card, p2Card)),
        player2 = nextQueue(!p1Wins, p2Q, List(p2Card, p1Card))
      )
    } else {
      game.copy(
        player1 = nextQueue(p1Card > p2Card, p1Q, List(p1Card, p2Card)),
        player2 = nextQueue(p1Card < p2Card, p2Q, List(p2Card, p1Card)),
        player1Hands = game.player1Hands + player1Hash,
        player2Hands = game.player2Hands + player2Hash
      )
    }

  }

  def nextQueue(winner: Boolean, nextQ: Queue[Int], toEnqueue: List[Int]): Queue[Int] =
    if (winner) nextQ.enqueueAll(toEnqueue) else nextQ
}
