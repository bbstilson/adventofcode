package bbstilson.aoc2020

import bbstilson.implicits.ListImplicits._

import scala.collection.immutable.Queue

object Day22 extends aocd.Problem(2020, 22) {

  val PlayerRegex = raw"Player (\d+):".r
  case class Game(player1: Queue[Int], player2: Queue[Int])

  def run(input: List[String]): Unit = {
    val players: Map[Int, Queue[Int]] = input
      .groupedBy(_.nonEmpty)
      .map { xs =>
        xs.head match {
          case PlayerRegex(player) => player.toInt -> Queue.from(xs.tail.map(_.toInt))
        }
      }
      .toMap

    part1(Game(players(1), players(2)))
    ()
  }

  def part1(init: Game): Long = part1 {
    val game = LazyList
      .iterate(init)(playRound)
      .dropWhile(g => !(g.player1.isEmpty || g.player2.isEmpty))
      .head

    val winningDeck = if (game.player1.isEmpty) game.player2 else game.player1

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
      player1 = if (p1Card > p2Card) p1Q.enqueueAll(List(p1Card, p2Card)) else p1Q,
      player2 = if (p1Card < p2Card) p2Q.enqueueAll(List(p2Card, p1Card)) else p2Q
    )
  }
}
