package org.bbstilson.aoc2019.intcode

object IntCodeComputer {

  final case class Args(a: Option[Long], b: Option[Long], c: Option[Long])
  final case class ProgramState(index: Long, memory: Map[Long, Long], inputs: LazyList[Long])

  def apply(program: List[Long], inputs: LazyList[Long]): LazyList[Long] = {
    outputs(ProgramState(0, mkMemory(program), inputs))
  }

  def mkMemory(xs: List[Long]): Map[Long, Long] = xs.zipWithIndex.map { case (x, i) => (i.toLong, x) }.toMap

  def outputs(init: ProgramState): LazyList[Long] = LazyList.unfold(init)(step).flatten

  def step(state: ProgramState): Option[(Option[Long], ProgramState)] = {
    val ProgramState(index, memory, _) = state
    val op = memory(index)
    val args = parseArgs(op, index, memory)
    op % 100 match {
      case 1  => Some(add(state, args))
      case 2  => Some(mult(state, args))
      case 3  => Some(handleInput(state))
      case 4  => Some(output(state, args))
      case 5  => Some(jumpIfTrue(state, args))
      case 6  => Some(jumpIfFalse(state, args))
      case 7  => Some(ifLessThan(state, args))
      case 8  => Some(ifEquals(state, args))
      case 9 => println("hi"); None
      case 99 => None
      case _ => throw new Error(s"hit a weird op code: $op")
    }
  }

  private def parseArgs(op: Long, index: Long, memory: Map[Long, Long]): Args = {
    val v1Idx = index + 1
    val v2Idx = index + 2
    val v3Idx = index + 3
    val opV1 = op / 100 % 10
    val opV2 = op / 1000 % 10
    val opV3 = op / 10000 % 10
      val v1 = getValue(opV1, memory, v1Idx)
      val v2 = getValue(opV2, memory, v2Idx)
      val v3 = getValue(opV3, memory, v3Idx)
    Args(v1, v2, v3)
  }

  def getValue(posCode: Long, memory: Map[Long, Long], index: Long): Option[Long] = {
    if (posCode == 0) {
      memory.get(index).flatMap(memory.get)
    } else {
      memory.get(index)
    }
  }

  private def add(state: ProgramState, args: Args): (Option[Long], ProgramState) = {
    val nextMem = state.memory + (state.memory(state.index + 3) -> (args.a.get + args.b.get))
    (None, state.copy(index = state.index + 4, memory = nextMem))
  }

  private def mult(state: ProgramState, args: Args): (Option[Long], ProgramState) = {
    val nextMem = state.memory + (state.memory(state.index + 3) -> (args.a.get * args.b.get))
    (None, state.copy(index = state.index + 4, memory = nextMem))
  }

  private def handleInput(state: ProgramState) = {
    val (input #:: nextInputs) = state.inputs
    val nextMem = state.memory + (state.memory(state.index + 1) -> input)
    (None, state.copy(index = state.index + 2, memory = nextMem, inputs = nextInputs))
  }

  private def output(state: ProgramState, args: Args): (Option[Long], ProgramState) = {
    val optOutput = args.a.collectFirst { case x if x != 0 => x }
    (optOutput, state.copy(index = state.index + 2))
  }

  private def jumpIfTrue(state: ProgramState, args: Args): (Option[Long], ProgramState) = {
    val nextIdx = if (args.a.get != 0) args.b.get else state.index + 3
    (None, state.copy(index = nextIdx))
  }

  private def jumpIfFalse(state: ProgramState, args: Args): (Option[Long], ProgramState) = {
    val nextIdx = if (args.a.get == 0) args.b.get else state.index + 3
    (None, state.copy(index = nextIdx))
  }

  private def ifLessThan(state: ProgramState, args: Args): (Option[Long], ProgramState) = {
    val value = if (args.a.get < args.b.get) 1L else 0L
    val newMem = state.memory + (state.memory(state.index + 3) -> value)
    (None, state.copy(index = state.index + 4, memory = newMem))
  }

  private def ifEquals(state: ProgramState, args: Args): (Option[Long], ProgramState) = {
    val value = if (args.a.get == args.b.get) 1L else 0L
    val newMem = state.memory + (state.memory(state.index + 3) -> value)
    (None, state.copy(index = state.index + 4, memory = newMem))
  }

  def getProgramFromResource(resource: String): List[Long] = {
    io.Source
      .fromResource(resource)
      .getLines.toList
      .head.split(",")
      .map(_.toLong)
      .toList
  }
}
