package bbstilson.aoc2019

object Day11 {

  type Point = (Int, Int)

  def main(args: Array[String]): Unit = {
    val program = IntCode.getProgramFromResource("2019/day11/input.txt")

    println(part1(program))
    part2(program)
  }

  def part1(program: List[Long]): Int = {
    val init = Map.empty[(Int, Int), Int].withDefaultValue(0)
    IntCode(program, init).last.values.size
  }

  def part2(program: List[Long]): Unit = {
    val init = Map.empty[(Int, Int), Int].withDefaultValue(0)
    val initWithStart = init + ((0, 0) -> 1)
    val map = IntCode(program, initWithStart).last
    val (bottomLeft, topRight) = getBoundingBox(map.keys.toList)

    val (x1, y1) = bottomLeft
    val (x2, y2) = topRight

    (y2 to y1 by -1).foreach { y =>
      (x1 to x2).foreach { x =>
        map.get((x, y)) match {
          case Some(x) if x == 1 => print("XX")
          case _                 => print("  ")
        }
      }
      println()
    }
  }

  private def getBoundingBox(ps: List[Point]): (Point, Point) = {
    val xs = ps.map(_._1)
    val ys = ps.map(_._2)

    ((xs.min, ys.min), (xs.max, ys.max))
  }
}

// Mostly copied from Day9 with changes.
object IntCode {

  type Color = Int
  type Memory = Map[Int, Long]
  type PanelMap = Map[(Int, Int), Color]

  sealed trait Action
  case object Paint extends Action
  case object Turn extends Action

  sealed trait Direction
  case object Up extends Direction
  case object Down extends Direction
  case object Left extends Direction
  case object Right extends Direction

  final case class Point(x: Int, y: Int, direction: Direction) {
    def coord: (Int, Int) = (x, y)
  }

  final case class ProgramState(
    index: Int,
    memory: Memory,
    inputs: PanelMap,
    relativeBase: Long = 0L,
    pos: Point = Point(0, 0, Up),
    lastAction: Action = Turn
  )

  def apply(program: List[Long], inputs: PanelMap = Map.empty): LazyList[PanelMap] = {
    outputs(ProgramState(0, mkMemory(program), inputs))
  }

  def outputs(init: ProgramState): LazyList[PanelMap] = LazyList.unfold(init)(step).flatten

  def mkMemory(xs: List[Long]): Memory =
    xs.zipWithIndex.map { case (x, i) => (i, x) }.toMap.withDefaultValue(0)

  def getCommand(state: ProgramState): Int = state.memory(state.index).toInt
  def getOpcode(cmd: Int): Int = (cmd % 100).toInt
  def paramMode(cmd: Long)(param: Int): Int = (cmd / math.pow(10, (2 + param).toDouble) % 10).toInt
  def getParamModes(cmd: Long): Vector[Int] = Vector(0, 1, 2).map(paramMode(cmd))

  def readParamWithState(state: ProgramState, paramModes: Vector[Int])(i: Int): Long = {
    val ProgramState(index, memory, _, relativeBase, _, _) = state
    val pos = i + 1
    paramModes(i) match {
      case 0 => memory((memory(index + pos)).toInt)
      case 1 => memory(index + pos)
      case 2 => memory((memory(index + pos) + relativeBase).toInt)
      case _ => throw new Error("WOAH WRONG PARAM MODE")
    }
  }

  def writeParamWithState(
    state: ProgramState,
    paramModes: Vector[Int]
  )(i: Int, value: Long): Memory = {
    val ProgramState(index, memory, _, relativeBase, _, _) = state
    val pos = i + 1
    paramModes(i) match {
      case 0 => memory + (memory(index + pos).toInt -> value)
      case 2 => memory + ((relativeBase + memory(index + pos)).toInt -> value)
      case _ => throw new Error("WOAH WRONG PARAM MODE")
    }
  }

  def step(state: ProgramState): Option[(Option[PanelMap], ProgramState)] = {
    val ProgramState(index, _, inputs, relativeBase, pos, lastAction) = state
    val cmd = getCommand(state)
    val opcode = getOpcode(cmd)
    val paramModes = getParamModes(cmd.toLong)
    val readParam = readParamWithState(state, paramModes) _
    val writeParam = writeParamWithState(state, paramModes) _

    opcode match {
      case 1 => {
        val nextMemory = writeParam(2, readParam(0) + readParam(1))
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case 2 => {
        val nextMemory = writeParam(2, readParam(0) * readParam(1))
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case 3 => {
        val input = inputs(pos.coord)
        val nextMemory = writeParam(0, input.toLong)
        Some((None, state.copy(index = index + 2, memory = nextMemory)))
      }
      case 4 => {
        val output = readParam(0).toInt
        val (nextInputs, nextPos, nextAction) = if (lastAction == Turn) {
          (inputs + (pos.coord -> output), pos, Paint)
        } else {
          (inputs, getNextPosition(pos, output), Turn)
        }

        val nextState = state.copy(
          index = index + 2,
          inputs = nextInputs,
          pos = nextPos,
          lastAction = nextAction
        )

        Some((Some(nextInputs), nextState))
      }
      case 5 => {
        val nextIndex = if (readParam(0) != 0) readParam(1).toInt else index + 3
        Some((None, state.copy(index = nextIndex)))
      }
      case 6 => {
        val nextIndex = if (readParam(0) == 0) readParam(1).toInt else index + 3
        Some((None, state.copy(index = nextIndex)))
      }
      case 7 => {
        val value = if (readParam(0) < readParam(1)) 1 else 0
        val nextMemory = writeParam(2, value.toLong)
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case 8 => {
        val value = if (readParam(0) == readParam(1)) 1 else 0
        val nextMemory = writeParam(2, value.toLong)
        Some((None, state.copy(index = index + 4, memory = nextMemory)))
      }
      case 9 => {
        Some((None, state.copy(index = index + 2, relativeBase = relativeBase + readParam(0))))
      }
      case 99 => None
      case _  => throw new Error(s"hit a weird op code: ${opcode}")
    }
  }

  private def getNextPosition(pos: Point, change: Int): Point = {
    val (x, y) = pos.coord

    def up: Point = pos.copy(y = y + 1, direction = Up)
    def down: Point = pos.copy(y = y - 1, direction = Down)
    def left: Point = pos.copy(x = x - 1, direction = Left)
    def right: Point = pos.copy(x = x + 1, direction = Right)

    (pos.direction, change) match {
      // Turning left.
      case (Up, 0)    => left
      case (Down, 0)  => right
      case (Left, 0)  => down
      case (Right, 0) => up
      // Turning right.
      case (Up, 1)    => right
      case (Down, 1)  => left
      case (Left, 1)  => up
      case (Right, 1) => down
      case _          => throw new Error("INVALID DIRECTION CHANGE")
    }
  }

  def getProgramFromResource(resource: String): List[Long] = {
    io.Source
      .fromResource(resource)
      .getLines()
      .toList
      .head
      .split(",")
      .map(_.toLong)
      .toList
  }
}
