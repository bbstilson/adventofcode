package bbstilson.aoc2019

object Day7 {

  def main(args: Array[String]): Unit = {
    val program = getProgramFromResource("2019/day7/input.txt")
    println(part1(program))
    // println(part2(program)) This doesn't work :(
  }

  def part1(program: List[Int]): Int = {
    List(0, 1, 2, 3, 4).permutations.map { phases =>
      phases.foldLeft(0) { case (prev, phase) =>
        apply(program, LazyList(phase, prev)).head
      }
    }.max
  }

  def part2(program: List[Int]): Long = {
    List(5, 6, 7, 8, 9).permutations.map { ps => lazyCompute(program, ps) }.max
  }

  def lazyCompute(program: List[Int], phases: List[Int]): Long = {
    val p = program.map(_.toLong) // ugh
    lazy val a: LazyList[Long] = Day9(p, phases(0).toLong #:: 0L #:: e)
    lazy val b: LazyList[Long] = Day9(p, phases(1).toLong #:: a)
    lazy val c: LazyList[Long] = Day9(p, phases(2).toLong #:: b)
    lazy val d: LazyList[Long] = Day9(p, phases(3).toLong #:: c)
    lazy val e: LazyList[Long] = Day9(p, phases(4).toLong #:: d)
    e.last
  }

  final case class Args(a: Option[Int], b: Option[Int], c: Option[Int])
  final case class ProgramState(index: Int, memory: Map[Int, Int], inputs: LazyList[Int])

  def apply(program: List[Int], inputs: LazyList[Int]): LazyList[Int] = {
    outputs(ProgramState(0, mkMemory(program), inputs))
  }

  def mkMemory(xs: List[Int]): Map[Int, Int] = xs.zipWithIndex.map(_.swap).toMap

  def outputs(init: ProgramState): LazyList[Int] = LazyList.unfold(init)(step).flatten

  def step(state: ProgramState): Option[(Option[Int], ProgramState)] = {
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
      case 99 => None
      case _  => throw new Error(s"hit a weird op code: $op")
    }
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

  private def add(state: ProgramState, args: Args): (Option[Int], ProgramState) = {
    val nextMem = state.memory + (state.memory(state.index + 3) -> (args.a.get + args.b.get))
    (None, state.copy(index = state.index + 4, memory = nextMem))
  }

  private def mult(state: ProgramState, args: Args): (Option[Int], ProgramState) = {
    val nextMem = state.memory + (state.memory(state.index + 3) -> (args.a.get * args.b.get))
    (None, state.copy(index = state.index + 4, memory = nextMem))
  }

  private def handleInput(state: ProgramState) = {
    val (input #:: nextInputs) = state.inputs
    // println(s"got input: $input")
    val nextMem = state.memory + (state.memory(state.index + 1) -> input)
    (None, state.copy(index = state.index + 2, memory = nextMem, inputs = nextInputs))
  }

  private def output(state: ProgramState, args: Args): (Option[Int], ProgramState) = {
    val optOutput = args.a.collectFirst { case x if x != 0 => x }
    (optOutput, state.copy(index = state.index + 2))
  }

  private def jumpIfTrue(state: ProgramState, args: Args): (Option[Int], ProgramState) = {
    val nextIdx = if (args.a.get != 0) args.b.get else state.index + 3
    (None, state.copy(index = nextIdx))
  }

  private def jumpIfFalse(state: ProgramState, args: Args): (Option[Int], ProgramState) = {
    val nextIdx = if (args.a.get == 0) args.b.get else state.index + 3
    (None, state.copy(index = nextIdx))
  }

  private def ifLessThan(state: ProgramState, args: Args): (Option[Int], ProgramState) = {
    val value = if (args.a.get < args.b.get) 1 else 0
    val newMem = state.memory + (state.memory(state.index + 3) -> value)
    (None, state.copy(index = state.index + 4, memory = newMem))
  }

  private def ifEquals(state: ProgramState, args: Args): (Option[Int], ProgramState) = {
    val value = if (args.a.get == args.b.get) 1 else 0
    val newMem = state.memory + (state.memory(state.index + 3) -> value)
    (None, state.copy(index = state.index + 4, memory = newMem))
  }

  def getProgramFromResource(resource: String): List[Int] = {
    io.Source
      .fromResource(resource)
      .getLines()
      .toList
      .head
      .split(",")
      .map(_.toInt)
      .toList
  }
}
