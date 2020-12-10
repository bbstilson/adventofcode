package bbstilson.aoc2020

import scala.annotation.tailrec

object Day10 extends aocd.Problem(2020, 10) {

  def run(input: List[String]): Unit = {
    val adapters = input.map(_.toInt).sorted
    val fixedAdapters = 0 +: adapters :+ (adapters.last + 3)
    println(part1(fixedAdapters))
    println(part2(fixedAdapters))
  }

  def part1(adapters: List[Int]): Int = {
    val (ones, threes) = adapters
      .sliding(2)
      .collect { case List(a, b) => b - a }
      .foldLeft((0, 0)) { case ((ones, threes), i) =>
        i match {
          case 1 => (ones + 1, threes)
          case 3 => (ones, threes + 1)
        }
      }
    ones * threes
  }

  def part2(adapters: List[Int]): Long = {
    val graph = buildGraph(adapters, Map.empty)
    val cache = collection.mutable.Map.empty[Int, Long]
    def traverseGraph(node: Int): Long = cache.getOrElse(
      node, {
        graph.get(node) match {
          case Some(edges) => {
            cache.update(node, edges.map(traverseGraph).sum)
            cache(node)
          }
          case None => 1L
        }
      }
    )

    traverseGraph(0)
  }

  @tailrec
  def buildGraph(adapters: List[Int], graph: Map[Int, List[Int]]): Map[Int, List[Int]] =
    adapters match {
      case hd :: tl if tl.nonEmpty => buildGraph(tl, (graph + (hd -> tl.takeWhile(_ - hd <= 3))))
      case _                       => graph
    }
}
