package org.bbstilson.aoc2019

object Day5 {

  def main(args: Array[String]): Unit = {
    val memory = mkMemory(parseInput("2019/day5/input.txt"))
    println(run(0, memory, IOState(List(1)))) // part 1
    println(run(0, memory, IOState(List(5)))) // part 2
  }

  def mkMemory(xs: List[Int]): Map[Int, Int] = xs.zipWithIndex.map(_.swap).toMap


  def run(index: Int, memory: Map[Int, Int], state: IOState): IOState = {
    val (command, args) = parse(index, memory)
    command match {
      case Add         => add(index, memory, args, state)
      case Multiply    => mult(index, memory, args, state)
      case Input       => handleInput(index, memory, state)
      case Output      => output(index, memory, args, state)
      case JumpIfTrue  => jumpIfTrue(index, memory, args, state)
      case JumpIfFalse => jumpIfFalse(index, memory, args, state)
      case IfLessThan  => ifLessThan(index, memory, args, state)
      case IfEquals    => ifEquals(index, memory, args, state)
      case Exit        => state
    }
  }

  final case class IOState(input: List[Int] = Nil, output: List[Int] = Nil)
  final case class Args(a: Option[Int], b: Option[Int], c: Option[Int])

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

  private def add(index: Int, memory: Map[Int, Int], args: Args, state: IOState): IOState = {
    val value = args.a.get + args.b.get
    run(index + 4, memory + (memory(index + 3) -> value), state)
  }

  private def mult(index: Int, memory: Map[Int, Int], args: Args, state: IOState): IOState = {
    val value = args.a.get * args.b.get
    run(index + 4, memory + (memory(index + 3) -> value), state)
  }

  private def handleInput(index: Int, memory: Map[Int, Int], state: IOState): IOState = {
    val nextState = state.copy(input = state.input.tail)
    run(index + 2, memory + (memory(index + 1) -> state.input.head), nextState)
  }

  private def output(index: Int, memory: Map[Int, Int], args: Args, state: IOState): IOState = {
    val nextState = args.a.collect {
      case x if x != 0 => state.copy(output = x +: state.output)
    }.getOrElse(state)

    run(index + 2, memory, nextState)
  }

  private def jumpIfTrue(index: Int, memory: Map[Int, Int], args: Args, state: IOState): IOState = {
    val nextIdx = if (args.a.get != 0) args.b.get else index + 3
    run(nextIdx, memory, state)
  }

  private def jumpIfFalse(index: Int, memory: Map[Int, Int], args: Args, state: IOState): IOState = {
    val nextIdx = if (args.a.get == 0) args.b.get else index + 3
    run(nextIdx, memory, state)
  }

  private def ifLessThan(index: Int, memory: Map[Int, Int], args: Args, state: IOState): IOState = {
    val value = if (args.a.get < args.b.get) 1 else 0
    run(index + 4, memory + (memory(index + 3) -> value), state)
  }

  private def ifEquals(index: Int, memory: Map[Int, Int], args: Args, state: IOState): IOState = {
    val value = if (args.a.get == args.b.get) 1 else 0
    run(index + 4, memory + (memory(index + 3) -> value), state)
  }

  def parseInput(resource: String): List[Int] = {
    io.Source
      .fromResource(resource)
      .getLines.toList
      .head
      .split(",")
      .map(_.toInt)
      .toList
  }
}
