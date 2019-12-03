package org.bbstilson.aoc2017

sealed trait State {
  def next: State
  def position: Int
  def stepCount: Int
  def movesMap: Map[Int, Int]
}

final case class Part1(stepCount: Int, position: Int, movesMap: Map[Int, Int]) extends State {
  def next: Part1 = {
    val nextStep = stepCount + 1
    // Max Int signifies that we've exited the sequence.
    val nextPosition = movesMap.get(position).map(_ + position).getOrElse(Int.MaxValue)
    // If we exited the sequence, just return the existing map.
    // Otherwise, update position we just came from.
    val nextMovesMap = if (nextPosition == Int.MaxValue) {
      movesMap
    } else {
      movesMap + (position -> (movesMap(position) + 1))
    }

    Part1(nextStep, nextPosition, nextMovesMap)
  }
}

final case class Part2(stepCount: Int, position: Int, movesMap: Map[Int, Int]) extends State {
  def next: Part2 = {
    val nextStep = stepCount + 1
    // Max Int signifies that we've exited the sequence.
    val nextPosition = movesMap.get(position).map(_ + position).getOrElse(Int.MaxValue)
    // If we exited the sequence, just return the existing map.
    // Otherwise, update position we just came from.
    val nextMovesMap = if (nextPosition == Int.MaxValue) {
      movesMap
    } else {
      val prevPos = movesMap(position)
      val modifier = if (prevPos >= 3) -1 else 1
      movesMap + (position -> (prevPos + modifier))
    }

    Part2(nextStep, nextPosition, nextMovesMap)
  }
}

object Day5 {
  def main(args: Array[String]): Unit = {
    val sequence = parseInput()
    val exit = sequence.size
    val movesMap = sequence.zipWithIndex.map(_.swap).toMap
    println(findStepsToExit(exit, Part1(0, 0, movesMap)))
    println(findStepsToExit(exit, Part2(0, 0, movesMap)))
  }

  def findStepsToExit(exit: Int, initState: State): Int = {
    Iterator
      .iterate(initState)(_.next)
      .dropWhile(_.position < exit)
      .next
      .stepCount
  }

  def parseInput(): List[Int] = {
    io.Source
      .fromResource("2017/day5/input.txt")
      .getLines
      .map(_.toInt)
      .toList
  }
}
