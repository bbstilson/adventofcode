package bbstilson.aoc2021

import bbstilson.implicits.ListImplicits._
import scala.annotation.tailrec

object Day4 extends aocd.Problem(2021, 4) {

  val winningStates = {
    val rowWinningState = (0 to 4).map(x => (0 to 4).map(y => (x, y)).toSet).toSet
    val colWinningState = (0 to 4).map(y => (0 to 4).map(x => (x, y)).toSet).toSet

    rowWinningState ++ colWinningState
  }

  def run(input: List[String]): Unit = {
    val numbers = input.head.split(",").map(_.toInt).toList
    implicit val boardsByIndex: Map[Int, Map[Int, (Int, Int)]] =
      input.tail
        .groupedBy(_.nonEmpty)
        .filter(_.nonEmpty)
        .map(_.map(_.split("\\s+").toList.collect {
          case s if s.nonEmpty => s.toInt
        }))
        .map { rawBoard =>
          rawBoard
            .map(_.zipWithIndex)
            .zipWithIndex
            .foldLeft(Map.empty[Int, (Int, Int)]) { case (map, (row, rowIdx)) =>
              row.foldLeft(map) { case (m, (value, colIdx)) =>
                m + (value -> (colIdx, rowIdx))
              }
            }
        }
        .zipWithIndex
        .map(_.swap)
        .toMap

    val stateByIndex: Map[Int, Set[(Int, Int)]] = List
      .fill(boardsByIndex.size)(Set.empty[(Int, Int)])
      .zipWithIndex
      .map(_.swap)
      .toMap

    part1 {
      @tailrec
      def helper(ns: List[Int], boardStates: Map[Int, Set[(Int, Int)]]): Int = {
        ns match {
          case n :: tail => {
            // mark it on each board
            val nextBoardStates = nextState(n, boardStates)

            // check for winners
            nextBoardStates.find { case (_, state) => isWinner(state) } match {
              case Some((idx, state)) => handleWinner(boardsByIndex(idx), state, n)
              case None               => helper(tail, nextBoardStates)
            }
          }
          case Nil => -1
        }
      }

      helper(numbers, stateByIndex)
    }

    part2 {
      @tailrec
      def helper(
        ns: List[Int],
        boardStates: Map[Int, Set[(Int, Int)]],
        prevWinner: Option[(Int, Set[(Int, Int)], Int)]
      ): Int = {
        ns match {
          case n :: tail => {
            // mark it on each board
            val nextBoardStates = nextState(n, boardStates)
            // check for winners
            val winners = nextBoardStates
              .filter { case (_, state) => isWinner(state) }
              .keySet
              .toList

            winners match {
              case Nil => helper(tail, nextBoardStates, prevWinner)
              case ws =>
                helper(
                  tail,
                  nextBoardStates -- ws,
                  Some((ws.last, nextBoardStates(ws.last), n))
                )
            }
          }
          case Nil => {
            val (idx, state, n) = prevWinner.get
            handleWinner(boardsByIndex(idx), state, n)
          }
        }
      }

      helper(numbers, stateByIndex, None)
    }

    ()
  }

  private def nextState(
    n: Int,
    boardStates: Map[Int, Set[(Int, Int)]]
  )(implicit boardsByIndex: Map[Int, Map[Int, (Int, Int)]]): Map[Int, Set[(Int, Int)]] =
    boardStates.map {
      case (idx, state) => {
        val nextState = boardsByIndex(idx)
          .get(n)
          .map(point => state + point)
          .getOrElse(state)

        (idx, nextState)
      }
    }
  private def isWinner(state: Set[(Int, Int)]): Boolean = winningStates.exists { winningState =>
    winningState.intersect(state).size == 5
  }

  private def handleWinner(
    board: Map[Int, (Int, Int)],
    state: Set[(Int, Int)],
    winningNumber: Int
  ): Int = {
    // find all unmarked numbers
    val unmarkedSum = board.collect {
      case (value, pos) if !state.contains(pos) => value
    }.sum
    unmarkedSum * winningNumber
  }
}
