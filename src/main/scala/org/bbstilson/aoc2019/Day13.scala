package org.bbstilson.aoc2019

import org.bbstilson.aoc2019.intcode.IntCode

object Day13 {
  final case class InputState(
    x: Option[Long],
    y: Option[Long],
    z: Option[Long],
    controllerPosition: Option[Long],
    outputs: LazyList[Long]
  )

  object InputState {
    def init(outputs: LazyList[Long]) = InputState(None, None, None, None, outputs)
  }

  def main(args: Array[String]): Unit = {
    val program = IntCode.getProgramFromResource("2019/day13/input.txt")
    println(part1(program))
    part2(program)
  }

  def part1(program: List[Long]): Int = {
    IntCode(program).toList
      .grouped(3)
      .filter(_.last == 2)
      .size
  }

  def part2(program: List[Long]): LazyList[Long] = {
    lazy val inputs: LazyList[Long] = player(outputs)
    lazy val outputs: LazyList[Long] = IntCode(program.updated(0, 2), inputs)

    outputs
  }

  def player(outputs: LazyList[Long]): LazyList[Long] = {
    LazyList.unfold(InputState.init(outputs))(playerStep).flatten
  }

  def playerStep(state: InputState): Option[(Option[Long], InputState)] = {
    zipped(state) match {
      case None => Some(updateState(state))
      case Some((x, _, z)) if z == 3 => Some(setControllerPosition(x, state))
      case Some((x, _, z)) if z == 4 => Some(moveController(x, state))
      case Some((x, _, z)) if x == -1 => {
        println(s"Current score: $z")
        Some((None, reset(state)))
      }
    }
  }

  def zipped(state: InputState): Option[(Long, Long, Long)] = {
    state.x.zip(state.y.zip(state.z)) match {
      case Some((x,(y,z))) => Some((x, y, z))
      case _ => None
    }
  }

  def updateState(state: InputState): (Option[Long], InputState) = {
    val (output #:: newOutputs) = state.outputs
    val resetState = reset(state)
    if (state.x.isEmpty) {
      (Some(0L), resetState.copy(x = Some(output), outputs = newOutputs))
    } else if (state.y.isEmpty) {
      (Some(0L), resetState.copy(y = Some(output), outputs = newOutputs))
    } else {
      (Some(0L), resetState.copy(z = Some(output), outputs = newOutputs))
    }
  }

  def setControllerPosition(x: Long, state: InputState): (Option[Long], InputState) = {
    (Some(0L), reset(state).copy(controllerPosition = Some(x)))
  }

  def moveController(x: Long, state: InputState): (Option[Long], InputState) = {
    val curPos = state.controllerPosition.get
    val adj = if (curPos < x) 1 else if (curPos == x) 0 else 1
    val newPos = Some(curPos + adj)
    (newPos, reset(state).copy(controllerPosition = newPos))
  }

  def reset(state: InputState): InputState = state.copy(
    x = None,
    y = None,
    z = None,
  )
}
