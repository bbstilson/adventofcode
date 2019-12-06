package org.bbstilson.aoc2019

import scala.util.Try

object Day5 {

  def main(args: Array[String]): Unit = {
    val memory = mkMemory(parseInput())
    println(Try(run(0, memory, 1))) // part 1
    println(Try(run(0, memory, 5))) // part 2
  }

  def mkMemory(xs: List[Int]): Map[Int, Int] = xs.zipWithIndex.map(_.swap).toMap

  def run(index: Int, memory: Map[Int, Int], input: Int): Int = {
    val (command, args) = parse(index, memory)
    command match {
      case Add         => add(index, memory, args, input)
      case Multiply    => mult(index, memory, args, input)
      case Input       => handleInput(index, memory, input)
      case Output      => output(index, memory, args, input)
      case JumpIfTrue  => jumpIfTrue(index, memory, args, input)
      case JumpIfFalse => jumpIfFalse(index, memory, args, input)
      case IfLessThan  => ifLessThan(index, memory, args, input)
      case IfEquals    => ifEquals(index, memory, args, input)
      case Exit        => exit()
    }
  }

  case class Args(a: Option[Int], b: Option[Int], c: Option[Int])

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

  private def parse(index: Int, memory: Map[Int, Int]): (Command, Args) = {
    val op = memory(index)
    val args = parseArgs(op, index, memory)
    val command = op % 100 match {
      case 1  => Add
      case 2  => Multiply
      case 3  => Input
      case 4  => Output
      case 5  => JumpIfTrue
      case 6  => JumpIfFalse
      case 7  => IfLessThan
      case 8  => IfEquals
      case 99 => Exit
      case _ => throw new Error(s"hit a weird op code: $op")
    }

    (command, args)
  }

  private def parseArgs(op: Int, index: Int, memory: Map[Int, Int]): Args = {
    val x = index + 1
    val y = index + 2
    val z = index + 3
    val v1 = if ((op / 100 % 10) == 0) memory.get(x).flatMap(memory.get) else memory.get(x)
    val v2 = if ((op / 1000 % 10) == 0) memory.get(y).flatMap(memory.get) else memory.get(y)
    val v3 = if ((op / 10000 % 10) == 0) memory.get(z).flatMap(memory.get) else memory.get(z)
    Args(v1, v2, v3)
  }

  private def add(index: Int, memory: Map[Int, Int], args: Args, input: Int): Int = {
    val value = args.a.get + args.b.get
    run(index + 4, memory + (memory(index + 3) -> value), input)
  }

  private def mult(index: Int, memory: Map[Int, Int], args: Args, input: Int): Int = {
    val value = args.a.get * args.b.get
    run(index + 4, memory + (memory(index + 3) -> value), input)
  }

  private def handleInput(index: Int, memory: Map[Int, Int], input: Int): Int = {
    run(index + 2, memory + (memory(index + 1) -> input), input)
  }

  private def output(index: Int, memory: Map[Int, Int], args: Args, input: Int): Int = {
    val output = args.a.get
    if (output != 0) println(s"Output: $output")
    run(index + 2, memory, input)
  }

  private def jumpIfTrue(index: Int, memory: Map[Int, Int], args: Args, input: Int): Int = {
    val nextIdx = if (args.a.get != 0) args.b.get else index + 3
    run(nextIdx, memory, input)
  }

  private def jumpIfFalse(index: Int, memory: Map[Int, Int], args: Args, input: Int): Int = {
    val nextIdx = if (args.a.get == 0) args.b.get else index + 3
    run(nextIdx, memory, input)
  }

  private def ifLessThan(index: Int, memory: Map[Int, Int], args: Args, input: Int): Int = {
    val value = if (args.a.get < args.b.get) 1 else 0
    run(index + 4, memory + (memory(index + 3) -> value), input)
  }

  private def ifEquals(index: Int, memory: Map[Int, Int], args: Args, input: Int): Int = {
    val value = if (args.a.get == args.b.get) 1 else 0
    run(index + 4, memory + (memory(index + 3) -> value), input)
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
