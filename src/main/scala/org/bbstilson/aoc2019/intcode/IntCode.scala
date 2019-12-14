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
    val paramModes = getParamModes(cmd)
    val readParam = readParamWithState(state, paramModes) _
    val writeParam = writeParamWithState(state, paramModes) _

    getOpcode(cmd) match {
      case Add => {
        val nextMemory = writeParam(2, readParam(0) + readParam(1))
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case Mult => {
        val nextMemory = writeParam(2, readParam(0) * readParam(1))
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case Input => {
        val (input #:: newInputs) = inputs
        val nextMemory = writeParam(0, input)
        Some((None, state.copy(index = index + 2, memory = nextMemory, inputs = newInputs)))
      }
      case Output => {
        Some((Some(readParam(0)), state.copy(index = index + 2)))
      }
      case JumpIfTrue => {
        val nextIndex = if (readParam(0) != 0) readParam(1).toInt else index + 3
        Some((None, state.copy(index = nextIndex)))
      }
      case JumpIfFalse => {
        val nextIndex = if (readParam(0) == 0) readParam(1).toInt else index + 3
        Some((None, state.copy(index = nextIndex)))
      }
      case LessThan => {
        val value = if (readParam(0) < readParam(1)) 1 else 0
        val nextMemory = writeParam(2, value)
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case EqualTo => {
        val value = if (readParam(0) == readParam(1)) 1 else 0
        val nextMemory = writeParam(2, value)
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case AdjRel => {
        Some((None, state.copy(index = index + 2, relativeBase = relativeBase + readParam(0).toInt)))
      }
      case Halt => None
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
  private[intcode] def getOpcode(cmd: Int): OpCode = {
    (cmd % 100).toInt match {
      case 1 => Add
      case 2 => Mult
      case 3 => Input
      case 4 => Output
      case 5 => JumpIfTrue
      case 6 => JumpIfFalse
      case 7 => LessThan
      case 8 => EqualTo
      case 9 => AdjRel
      case 99 => Halt
      case x => throw new Error(s"hit a weird op code: ${x}")
    }
  }
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

  sealed trait OpCode
  case object Add extends OpCode
  case object Mult extends OpCode
  case object Input extends OpCode
  case object Output extends OpCode
  case object JumpIfTrue extends OpCode
  case object JumpIfFalse extends OpCode
  case object LessThan extends OpCode
  case object EqualTo extends OpCode
  case object AdjRel extends OpCode
  case object Halt extends OpCode
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
