package org.bbstilson

object Day2 {
  def main(args: Array[String]): Unit = {
    println(part1(mkProgram(12, 2)))
    println(part2())
  }

  def part1(program: List[Int]): Int = {
    val initMemoryMap = program.zipWithIndex.map(_.swap).toMap
    val (_, memoryMap) = program
      .sliding(4, 4)
      .foldLeft((false, initMemoryMap)) { case ((done, map), p) =>
        (done, parseCommand(p)) match {
          case (false, Some(cmd)) => cmd.action match {
            case 1 => (false, mkNextMap(map, cmd.args, add))
            case 2 => (false, mkNextMap(map, cmd.args, mult))
            case 99 => (true, map) // Oke, all done.
          }
          case _ => (true, map)
        }
      }

    memoryMap(0)
  }

  private def add(x: Int, y: Int): Int = x + y
  private def mult(x: Int, y: Int): Int = x * y

  private def mkNextMap(map: Map[Int, Int], args: Args, f: (Int, Int) => Int): Map[Int, Int] = {
    map + (args.res -> f(map(args.p1), map(args.p2)))
  }

  final case class Args(p1: Int, p2: Int, res: Int)
  final case class Command(action: Int, args: Args)
  private def parseCommand(p: List[Int]): Option[Command] = {
    val pos1 = p.drop(1).headOption
    val pos2 = p.drop(2).headOption
    val res  = p.drop(3).headOption

    val optArgs = (pos1, pos2, res) match {
      case (Some(p1), Some(p2), Some(r)) => Some(Args(p1, p2, r))
      case _ => None
    }

    optArgs.map { args => Command(p.head, args) }
  }

  private def mkProgram(noun: Int, verb: Int): List[Int] = {
    List(1,noun,verb,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,10,19,1,19,5,23,2,23,9,27,1,5,27,31,1,9,31,35,1,35,10,39,2,13,39,43,1,43,9,47,1,47,9,51,1,6,51,55,1,13,55,59,1,59,13,63,1,13,63,67,1,6,67,71,1,71,13,75,2,10,75,79,1,13,79,83,1,83,10,87,2,9,87,91,1,6,91,95,1,9,95,99,2,99,10,103,1,103,5,107,2,6,107,111,1,111,6,115,1,9,115,119,1,9,119,123,2,10,123,127,1,127,5,131,2,6,131,135,1,135,5,139,1,9,139,143,2,143,13,147,1,9,147,151,1,151,2,155,1,9,155,0,99,2,0,14,0)
  }

  def part2(): Int = {
    val answer = for {
      x <- 0 to 100
      y <- 0 to 100
      if part1(mkProgram(x, y)) == 19690720
    } yield (x, y)

    val (x, y) = answer.head
    100 * x + y
  }
}
