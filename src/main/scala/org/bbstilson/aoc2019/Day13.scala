package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode.IntCode

object Day13 {
  final case class InputState(
    x: Option[Int] = None,
    y: Option[Int] = None,
    z: Option[Int] = None,
    paddlePosition: Int = 0,
    ballPosition: Int = 0,
    outputs: LazyList[Long]
  )

  object InputState {
    def init(outputs: LazyList[Long]) = InputState(outputs = outputs)
  }

  def main(args: Array[String]): Unit = {
    val program = IntCode.getProgramFromResource("2019/day13/input.txt")
    // println(part1(program))
    part2(program)
  }

  def part1(program: List[Long]): Int = {
    IntCode(program).toList.grouped(3).filter(_.last == 2).size
  }

  def part2(program: List[Long]): Long = {
    lazy val inputs: LazyList[Long] = player(outputs)
    lazy val outputs: LazyList[Long] = IntCode(program.updated(0, 2), 0 #:: inputs)

    outputs.last
  }

  def player(outputs: LazyList[Long]): LazyList[Long] = {
    LazyList.unfold(InputState.init(outputs))(playerStep).flatten
  }

  def playerStep(state: InputState): Option[(Option[Long], InputState)] = {
    getPixel(state).map {
      case Empty(x, y) => noop(state)
      case Wall(x, y) => noop(state)
      case Block(x, y) => noop(state)
      case Paddle(x, y) => println("paddle"); setPaddlePosition(x, y, state)
      case Ball(x, y) => println("ball"); setBallPosition(x, y, state)
      case Score(score) => {
        println(s"!! CURRENT SCORE: $score !!")
        (None, reset(state))
      }
      case _ => throw new Error("WOAH BAD STATE")
    }.orElse(Some(updateState(state)))
  }

  sealed trait Pixel
  final case class Score(score: Int) extends Pixel
  final case class Empty(x: Int, y: Int) extends Pixel
  final case class Wall(x: Int, y: Int) extends Pixel
  final case class Block(x: Int, y: Int) extends Pixel
  final case class Paddle(x: Int, y: Int) extends Pixel
  final case class Ball(x: Int, y: Int) extends Pixel

  def getPixel(state: InputState): Option[Pixel] = {
    val zipped = state.x.zip(state.y.zip(state.z))
    zipped.map { case ((x,(y,z))) => z match {
      case 0 => Empty(x, y)
      case 1 => Wall(x, y)
      case 2 => Block(x, y)
      case 3 => Paddle(x, y)
      case 4 => Ball(x, y)
      case _ if x == -1 => Score(z)
      case _ => throw new Error("INVALID PIXEL")
    }}
  }

  def noop(state: InputState): (Option[Long], InputState) = (None, reset(state))

  def updateState(state: InputState): (Option[Long], InputState) = {
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

  def setBallPosition(x: Int, y: Int, state: InputState): (Option[Long], InputState) = {
    // (Some(getControllerAdjustment(x, y, state)), reset(state).copy(ballPosition = x))
    (None, reset(state).copy(ballPosition = x))
  }

  // When the ball moves, we need to move our controller underneath it.
  // Unless it's one away, then don't move.
  def getControllerAdjustment(x: Int, y: Int, state: InputState): Long = {
    val paddle = state.paddlePosition
    val move = if (paddle < x) 1 else if (paddle > x) -1 else 0
    println(s"move = $move | paddle = $paddle | ball = (x=$x, y=$y)")
    // scala.io.StdIn.readLong()
    move
  }

  def setPaddlePosition(x: Int, y: Int, state: InputState): (Option[Long], InputState) = {
    // (None, reset(state).copy(paddlePosition = x))
    (Some(getControllerAdjustment(x, y, state)), reset(state).copy(paddlePosition = x))
  }

  def reset(state: InputState): InputState = state.copy(x = None, y = None, z = None)
}
