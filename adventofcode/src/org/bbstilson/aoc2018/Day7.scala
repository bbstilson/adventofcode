package org.bbstilson.aoc2018

import scala.collection.mutable.PriorityQueue

/*
Big thanks to UW for posting slides from their CS courses. I didn't get a CS
degree, so these were a huge help.
https://courses.cs.washington.edu/courses/cse326/03wi/lectures/RaoLect20.pdf
 */

object Part1 {
  type Digraph = Map[Char, Set[Char]]

  object Digraph {
    val empty: Digraph = Map.empty[Char, Set[Char]]
  }

  type DegreeMap = Map[Char, Int]

  object DegreeMap {
    val empty = Map.empty[Char, Int]
    def initFromDigraph(g: Digraph): DegreeMap = (g.keySet ++ g.values.flatten).map((_, 0)).toMap
  }

  def newPQueue: PriorityQueue[Char] = {
    PriorityQueue.empty[Char](implicitly[Ordering[Char]].reverse)
  }

  def main(args: Array[String]): Unit = {
    val source: Seq[(Char, Char)] = io.Source
      .fromResource("2018/day7/input.txt")
      .getLines()
      .toList
      .map(parse)

    val graph = mkAdjGraph(source)
    val degreeMap = calcInDegrees(graph)
    val topoSorted = topologicalSort(graph, degreeMap)

    println(topoSorted)
  }

  lazy val stepRegex = """(?i)step (\w)""".r

  private def parse(line: String): (Char, Char) = {
    val matches = stepRegex.findAllMatchIn(line).toList.map(_.toString)
    (matches.head.last, matches.last.last)
  }

  private def mkAdjGraph(pairs: Seq[(Char, Char)]): Digraph = {
    pairs.foldLeft(Digraph.empty) {
      case (graph, (parent, dependant)) => {
        graph.get(parent) match {
          case Some(dSet) => graph.updated(parent, dSet + dependant)
          case None       => graph + (parent -> Set(dependant))
        }
      }
    }
  }

  private def calcInDegrees(g: Digraph): DegreeMap = {
    g.foldLeft(DegreeMap.initFromDigraph(g)) {
      case (degreeMap, (_, values)) => {
        values.foldLeft(degreeMap)(incrementDegree(_, _))
      }
    }
  }

  private def incrementDegree(dMap: DegreeMap, value: Char): DegreeMap = {
    dMap.updated(value, dMap(value) + 1)
  }

  private def topologicalSort(graph: Digraph, degreeMap: DegreeMap): String = {
    val pQueue = newPQueue

    // Find vertices with in-degree 0, and populate queue
    getZeroInDegree(degreeMap).foreach(pQueue.enqueue(_))

    topologicalSort(graph, degreeMap, pQueue)
  }

  private def topologicalSort(
    graph: Digraph,
    degreeMap: DegreeMap,
    pQueue: PriorityQueue[Char]
  ): String = {
    if (pQueue.isEmpty) {
      ""
    } else {
      // Dequeue, save letter for result
      val letter: Char = pQueue.dequeue()

      // Reduce In-Degree of all vertices adjacent to it by 1
      val nextDegreeMap = graph.get(letter) match {
        case Some(charSet) =>
          charSet.foldLeft(degreeMap) { case (dMap, char) =>
            dMap(char) match {
              case 0 => dMap
              case _ => dMap.updated(char, dMap(char) - 1)
            }
          }
        case None => degreeMap
      }

      // Enqueue any of these vertices whose In-Degree became zero
      (getZeroInDegree(nextDegreeMap) -- getZeroInDegree(degreeMap))
        .foreach(pQueue.enqueue(_))

      letter.toString + topologicalSort(graph, nextDegreeMap, pQueue)
    }
  }

  private def getZeroInDegree(degreeMap: DegreeMap): Set[Char] = {
    degreeMap.filter { case (_, v) => v == 0 }.keySet
  }
}
