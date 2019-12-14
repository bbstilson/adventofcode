package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode._
import scala.util.{ Failure, Success, Try }

object Day13 {
  def main(args: Array[String]): Unit = {
    val program = IntCodeHelpers.getProgramFromResource("2019/day13/input.txt")
    // println(part1(program))
    part2(program)
  }

  def part1(program: List[Long]): Int = IntCode(program).toList.grouped(3).filter(_.last == 2).size

  def part2(program: List[Long]): Long = {
    lazy val inputs: LazyList[Long] = player(outputs)
    lazy val outputs: LazyList[Long] = IntCode(program.updated(0, 2), 0 #:: inputs)

    outputs.last
  }

  ////////////////////////////
  ////////// PART 2 //////////
  ////////////////////////////
  final case class InputState(
    x: Option[Int] = None,
    y: Option[Int] = None,
    z: Option[Int] = None,
    paddle: Int = 0,
    ball: Int = 0,
    outputs: LazyList[Long]
  )

  object InputState {
    def init(outputs: LazyList[Long]) = InputState(outputs = outputs)
  }

  sealed trait Pixel
  final case class Score(score: Int) extends Pixel
  final case class Empty(x: Int, y: Int) extends Pixel
  final case class Wall(x: Int, y: Int) extends Pixel
  final case class Block(x: Int, y: Int) extends Pixel
  final case class Paddle(x: Int, y: Int) extends Pixel
  final case class Ball(x: Int, y: Int) extends Pixel

  type FoldState = (Option[Long], InputState)

  def player(outputs: LazyList[Long]): LazyList[Long] = {
    LazyList.unfold(InputState.init(outputs))(playerStep).flatten
  }

  def playerStep(state: InputState): Option[FoldState] = {
    getPixel(state).map {
      case Empty(x, y)  => noop(state)
      case Wall(x, y)   => noop(state)
      case Block(x, y)  => noop(state)
      case Paddle(x, y) => setPaddlePosition(x, y, state)
      case Ball(x, y)   => setBallPosition(x, y, state)
      case Score(score) => {
        println(s"!! CURRENT SCORE: $score !!")
        (None, reset(state))
      }
    }.orElse(Some(updateState(state)))
  }

  def getPixel(state: InputState): Option[Pixel] = {
    val zipped = state.x.zip(state.y.zip(state.z))
    zipped.map { case ((x, (y, z))) => z match {
      case 0 => Empty(x, y)
      case 1 => Wall(x, y)
      case 2 => Block(x, y)
      case 3 => Paddle(x, y)
      case 4 => Ball(x, y)
      case _ if x == -1 => Score(z)
      case _ => throw new Error("INVALID PIXEL")
    }}
  }

  def noop(state: InputState): FoldState = (None, reset(state))

  def updateState(state: InputState): FoldState = {
    if (state.outputs.isEmpty) {
      (Some(0L), state)
    } else {
      val output = state.outputs.head.toInt
      val outputs = state.outputs.tail
      if (state.x.isEmpty) {
        (None, state.copy(x = Some(output), outputs = outputs))
      } else if (state.y.isEmpty) {
        (None, state.copy(y = Some(output), outputs = outputs))
      } else {
        (None, state.copy(z = Some(output), outputs = outputs))
      }
    }
  }

  def setBallPosition(x: Int, y: Int, state: InputState): FoldState = {
    println("ball")
    (None, reset(state).copy(ball = x))
  }

  // When the ball moves, we need to move our controller underneath it.
  // Unless it's one away, then don't move.
  def getControllerAdjustment(x: Int, y: Int, state: InputState): Long = {
    val paddle = state.paddle
    val move = if (paddle < x) 1 else if (paddle > x) -1 else 0
    println(s"move = $move | paddle = $paddle | ball = (x=$x, y=$y)")
    move
  }

  def setPaddlePosition(x: Int, y: Int, state: InputState): FoldState = {
    println("paddle")
    // (None, reset(state).copy(paddle = x))
    val move = if (x < state.ball) 1 else if (x > state.ball) -1 else 0
    println(s"move = $move | paddle = $x | ball = ${state.ball}")
    (Some(move), reset(state).copy(paddle = x))
  }

  def reset(state: InputState): InputState = state.copy(x = None, y = None, z = None)
}
