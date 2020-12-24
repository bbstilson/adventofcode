package bbstilson.aoc2020

import bbstilson.implicits.ListImplicits._
import bbstilson.implicits.TupleImplicits._

import scala.annotation.tailrec

// This was very helpful!
// https://www.redblobgames.com/grids/hexagons/
object Day24 extends aocd.Problem(2020, 24) {
  type HexPoint = (Int, Int, Int)
  val e = (1, -1, 0)
  val w = (-1, 1, 0)
  val ne = (1, 0, -1)
  val sw = (-1, 0, 1)
  val nw = (0, 1, -1)
  val se = (0, -1, 1)
  val hexNeighbors = List(e, w, ne, sw, nw, se)

  val directions = Set("e", "se", "sw", "w", "nw", "ne")

  def run(input: List[String]): Unit = {
    part1 {
      input
        .map(parseMoves)
        .map(doMoves)
        .toFrequencyMap
        .count { case (_, flips) => flips % 2 != 0 }
    }

    part2 {
      val board = input
        .map(parseMoves)
        .map(doMoves)
        .toFrequencyMap

      LazyList
        .iterate(board)(cycle)
        .drop(100)
        .head
        .count { case (_, flips) => flips % 2 != 0 }
    }
    ()
  }

  def cycle(board: Map[HexPoint, Int]): Map[HexPoint, Int] = {
    val ps = board.keySet
    val ns = (ps.flatMap(getNeighbors) -- ps).map(_ -> 0).toMap
    (board ++ ns).map {
      case (tile, flips) if shouldFlip(board, tile, flips) => tile -> (flips + 1)
      case kv                                              => kv
    }
  }

  def shouldFlip(board: Map[HexPoint, Int], tile: HexPoint, flips: Int): Boolean = {
    val blackNeighbors = numBlackNeighbors(tile, board)
    (flips % 2 != 0 && (blackNeighbors == 0 || blackNeighbors > 2)) ||
    (flips % 2 == 0 && blackNeighbors == 2)
  }

  def getNeighbors(p: HexPoint): List[HexPoint] = hexNeighbors.map(_ + p)

  def numBlackNeighbors(p: HexPoint, board: Map[HexPoint, Int]): Int =
    getNeighbors(p).map(board.getOrElse(_, 0)).count(_ % 2 != 0)

  def doMoves(moves: List[HexPoint]): HexPoint = moves.foldLeft((0, 0, 0))(_ + _)

  def parseMoves(line: String): List[HexPoint] = {
    val iter = line.to(Iterator)

    @tailrec
    def _parseMoves(moves: List[HexPoint] = Nil): List[HexPoint] = {
      val x = iter.next().toString()
      val rawDir = if (directions(x)) x else x + iter.next()
      val dir = rawDir match {
        case "e"  => e
        case "w"  => w
        case "ne" => ne
        case "sw" => sw
        case "nw" => nw
        case "se" => se
      }
      if (iter.isEmpty) dir +: moves else _parseMoves(dir +: moves)
    }

    _parseMoves().reverse
  }
}
