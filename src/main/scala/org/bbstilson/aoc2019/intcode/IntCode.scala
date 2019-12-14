package org.bbstilson.aoc2019.intcode

object IntCode {
  import IntCodeMethods._
  import IntCodeDataTypes._

  def apply(program: List[Long], inputs: LazyList[Long] = LazyList.empty): LazyList[Long] = {
    outputs(ProgramState(0, mkMemory(program), inputs))
  }

  def outputs(init: ProgramState): LazyList[Long] = LazyList.unfold(init)(step).flatten

  private def step(state: ProgramState): Option[(Option[Long], ProgramState)] = {
    val ProgramState(index, memory, inputs, relativeBase) = state
    val cmd = getCommand(state)
    val opcode = getOpcode(cmd)
    val paramModes = getParamModes(cmd)
    val readParam = readParamWithState(state, paramModes) _
    val writeParam = writeParamWithState(state, paramModes) _

    opcode match {
      case 1  => {
        val nextMemory = writeParam(2, readParam(0) + readParam(1))
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case 2  => {
        val nextMemory = writeParam(2, readParam(0) * readParam(1))
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case 3  => {
        val (input #:: newInputs) = inputs
        val nextMemory = writeParam(0, input)
        Some((None, state.copy(index = index + 2, memory = nextMemory, inputs = newInputs)))
      }
      case 4  => {
        Some((Some(readParam(0)), state.copy(index = index + 2)))
      }
      case 5  => {
        val nextIndex = if (readParam(0) != 0) readParam(1).toInt else index + 3
        Some((None, state.copy(index = nextIndex)))
      }
      case 6  => {
        val nextIndex = if (readParam(0) == 0) readParam(1).toInt else index + 3
        Some((None, state.copy(index = nextIndex)))
      }
      case 7  => {
        val value = if (readParam(0) < readParam(1)) 1 else 0
        val nextMemory = writeParam(2, value)
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case 8  => {
        val value = if (readParam(0) == readParam(1)) 1 else 0
        val nextMemory = writeParam(2, value)
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case 9  => {
        Some((None, state.copy(index = index + 2, relativeBase = relativeBase + readParam(0).toInt)))
      }
      case 99 => None
      case _ => throw new Error(s"hit a weird op code: ${opcode}")
    }
  }
}

object IntCodeMethods {
  import IntCodeDataTypes._

  private[intcode] def mkMemory(xs: List[Long]): Memory = {
    xs
      .zipWithIndex
      .map { case (x, i) => (i, x) }
      .toMap
      .withDefaultValue(0)
  }

  private[intcode] def getCommand(state: ProgramState): Int = state.memory(state.index).toInt
  private[intcode] def getOpcode(cmd: Int): Int = (cmd % 100).toInt
  private[intcode] def paramMode(cmd: Long)(param: Int): Int = (cmd / math.pow(10, 2 + param) % 10).toInt
  private[intcode] def getParamModes(cmd: Long): Vector[Int] = Vector(0,1,2).map(paramMode(cmd))
  private[intcode] def readParamWithState(state: ProgramState, paramModes: Vector[Int])(i: Int): Long = {
    val ProgramState(index, memory, _, relativeBase) = state
    val pos = i + 1
    paramModes(i) match {
      case 0 => memory((memory(index + pos)).toInt)
      case 1 => memory(index + pos)
      case 2 => memory((memory(index + pos) + relativeBase).toInt)
      case _ => throw new Error("WOAH WRONG PARAM MODE")
    }
  }

  private[intcode] def writeParamWithState(state: ProgramState, paramModes: Vector[Int])(i: Int, value: Long): Memory = {
    val ProgramState(index, memory, _, relativeBase) = state
    val pos = i + 1
    paramModes(i) match {
      case 0 => memory + (memory(index + pos).toInt -> value)
      case 2 => memory + ((relativeBase + memory(index + pos)).toInt -> value)
      case _ => throw new Error("WOAH WRONG PARAM MODE")
    }
  }
}

object IntCodeDataTypes {
  type Memory = Map[Int, Long]

  final case class ProgramState(
    index: Int,
    memory: Memory,
    inputs: LazyList[Long],
    relativeBase: Int = 0
  )
}

object IntCodeHelpers {
  def getProgramFromResource(resource: String): List[Long] = {
    io.Source
      .fromResource(resource)
      .getLines.toList
      .head.split(",")
      .map(_.toLong)
      .toList
  }
}
