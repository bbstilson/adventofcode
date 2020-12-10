package bbstilson.aoc2020

import scala.annotation.tailrec

object Day10 extends aocd.Problem(2020, 10) {

  def run(input: List[String]): Unit = {
    val adapters = input.map(_.toInt).sorted
    val fixedAdapters = 0 +: adapters :+ (adapters.last + 3)
    println(part1(fixedAdapters))
    println(part2(fixedAdapters))
  }

  case class Joltages(ones: Int, threes: Int) {
    def result: Int = ones * threes
  }

  def part1(adapters: List[Int]): Int =
    adapters
      .sliding(2)
      .collect { case List(a, b) => b - a }
      .foldLeft(Joltages(0, 0)) { case (js, i) =>
        i match {
          case 1 => js.copy(ones = js.ones + 1)
          case 3 => js.copy(threes = js.threes + 1)
        }
      }
      .result

  def part2(adapters: List[Int]): Long = {
    implicit val graph = buildGraph(adapters, Map.empty)
    val intersections = buildIntersections(adapters)
    val merged = mergeIntersections(intersections)
    val permutations = merged.map(intersectionPermutations)
    permutations.product
  }

  def mergeIntersections(intersections: List[List[Int]]): List[List[Int]] = {
    val (out, carry) = intersections.tail.foldLeft((List.empty[List[Int]], intersections.head)) {
      case ((out, carry), intersection) =>
        if (intersection.head > carry.head && intersection.head < carry.last) {
          (out, carry ++ intersection)
        } else {
          (out :+ carry, intersection)
        }

    }
    (out :+ carry).map(_.distinct)
  }

  def buildIntersections(adapters: List[Int])(implicit
    graph: Map[Int, List[Int]]
  ): List[List[Int]] = adapters.map(buildIntersection).filter(_.size > 2)

  @tailrec
  def buildGraph(adapters: List[Int], graph: Map[Int, List[Int]]): Map[Int, List[Int]] =
    adapters match {
      case hd :: tl if tl.nonEmpty => buildGraph(tl, (graph + (hd -> tl.takeWhile(_ - hd <= 3))))
      case _                       => graph
    }

  def buildIntersection(start: Int)(implicit graph: Map[Int, List[Int]]): List[Int] =
    graph.get(start) match {
      case Some(edges) if edges.size > 1 =>
        List(start) ++ edges.init ++ buildIntersection(edges.last)
      case Some(List(edge)) => List(start, edge)
      case _                => Nil
    }

  def intersectionPermutations(xs: List[Int]): Long = {
    val (start, end) = (xs.head, xs.last)
    xs.permutations
      .map(
        _.sliding(2)
          .takeWhile {
            case List(a, b) => a < b && b - a <= 3
            case _          => false // silence compiler
          }
          .toList
          .flatten
      )
      .toList
      .distinct
      .count(xs => xs.nonEmpty && xs.head == start && xs.last == end)
      .toLong
  }
}
