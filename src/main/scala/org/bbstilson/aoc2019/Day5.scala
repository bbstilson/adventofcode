package org.bbstilson.aoc2019

import scala.util.Try

object Day5 {

  def main(args: Array[String]): Unit = {
    val memory = mkMemory(parseInput())
    println(Try(run(0, memory, 1))) // part 1
    println(Try(run(0, memory, 5))) // part 2
  }

  private def mkMemory(xs: List[Int]): Map[Int, Int] = {
    xs.zipWithIndex.map(_.swap).toMap
  }

  def run(index: Int, memory: Map[Int, Int], input: Int): Int = {
    val (command, modes) = parse(memory(index))
    command match {
      case Add         => add(index, memory, modes, input)
      case Multiply    => mult(index, memory, modes, input)
      case Input       => handleInput(index, memory, input)
      case Output      => output(index, memory, modes, input)
      case JumpIfTrue  => jumpIfTrue(index, memory, modes, input)
      case JumpIfFalse => jumpIfFalse(index, memory, modes, input)
      case IfLessThan  => ifLessThan(index, memory, modes, input)
      case IfEquals    => ifEquals(index, memory, modes, input)
      case Exit        => exit()
    }
  }

  case class Modes(m1: Int, m2: Int, m3: Int)

  sealed trait Command
  case object Add extends Command
  case object Multiply extends Command
  case object Input extends Command
  case object Output extends Command
  case object JumpIfTrue extends Command
  case object JumpIfFalse extends Command
  case object IfLessThan extends Command
  case object IfEquals extends Command
  case object Exit extends Command

  private def parse(x: Int): (Command, Modes) = {
    val op = x % 100
    val command = op match {
      case 1  => Add
      case 2  => Multiply
      case 3  => Input
      case 4  => Output
      case 5  => JumpIfTrue
      case 6  => JumpIfFalse
      case 7  => IfLessThan
      case 8  => IfEquals
      case 99 => Exit
      case _ => throw new Error(s"hit a weird op code: $op | ${x}")
    }

    val m1 = x / 100 % 10
    val m2 = x / 1000 % 10
    val m3 = x / 10000 % 10
    val modes = Modes(m1, m2, m3)

    (command, modes)
  }

  private def add(index: Int, memory: Map[Int, Int], modes: Modes, input: Int): Int = {
    run(index + 4, buildMemoryMath(index, modes, memory, _ + _), input)
  }

  private def mult(index: Int, memory: Map[Int, Int], modes: Modes, input: Int): Int = {
    run(index + 4, buildMemoryMath(index, modes, memory, _ * _), input)
  }

  private def handleInput(index: Int, memory: Map[Int, Int], input: Int): Int = {
    run(index + 2, memory + (memory(index + 1) -> input), input)
  }

  private def output(index: Int, memory: Map[Int, Int], modes: Modes, input: Int): Int = {
    val loc = index + 1
    val value = if (modes.m1 == 0) memory(memory(loc)) else memory(loc)
    if (value != 0) println(s"output: $value")
    run(index + 2, memory, input)
  }

  private def jumpIfTrue(index: Int, memory: Map[Int, Int], modes: Modes, input: Int): Int = {
    val nextIdx = doJump(index, modes, memory, _ != 0)
    run(nextIdx, memory, input)
  }

  private def jumpIfFalse(index: Int, memory: Map[Int, Int], modes: Modes, input: Int): Int = {
    val nextIdx = doJump(index, modes, memory, _ == 0)
    run(nextIdx, memory, input)
  }

  private def ifLessThan(index: Int, memory: Map[Int, Int], modes: Modes, input: Int): Int = {
    run(index + 4, buildMemoryComp(index, modes, memory, _ < _), input)
  }

  private def ifEquals(index: Int, memory: Map[Int, Int], modes: Modes, input: Int): Int = {
    run(index + 4, buildMemoryComp(index, modes, memory, _ == _), input)
  }

  // Memory helpers.
  private def buildMemoryMath(index: Int, modes: Modes, memory: Map[Int, Int], f: (Int, Int) => Int): Map[Int, Int] = {
    val x = index + 1
    val y = index + 2
    val z = index + 3

    val p1 = if (modes.m1 == 0) memory(memory(x)) else memory(x)
    val p2 = if (modes.m2 == 0) memory(memory(y)) else memory(y)

    memory + (memory(z) -> f(p1, p2))
  }

  private def doJump(index: Int, modes: Modes, memory: Map[Int, Int], f: (Int) => Boolean): Int = {
    val x = index + 1
    val y = index + 2
    val p1Value = if (modes.m1 == 0) memory(memory(x)) else memory(x)
    if (f(p1Value)) {
      if (modes.m2 == 0) memory(memory(y)) else memory(y)
    } else index + 3
  }

  private def buildMemoryComp(
    index: Int,
    modes: Modes,
    memory: Map[Int, Int],
    f: (Int, Int) => Boolean
  ): Map[Int, Int] = {
    val x = index + 1
    val y = index + 2
    val z = index + 3
    val value1 = if (modes.m1 == 0) memory(memory(x)) else memory(x)
    val value2 = if (modes.m2 == 0) memory(memory(y)) else memory(y)
    val value = if (f(value1, value2)) 1 else 0
    memory + (memory(z) -> value)
  }

  private def exit(): Int = 99

  def parseInput(): List[Int] = {
    io.Source
      .fromResource("2019/day5/input.txt")
      .getLines.toList
      .head
      .split(",")
      .map(_.toInt)
      .toList
  }
}
