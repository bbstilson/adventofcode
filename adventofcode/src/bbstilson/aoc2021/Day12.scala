package bbstilson.aoc2021

object Day12 extends aocd.Problem(2021, 12) {
  def run(input: List[String]): Unit = {
    val edges = input
      .map(_.split("-", 2).toList)
      .map(xs => xs.head -> xs.last)

    val graph = buildGraph(edges)
    val START = "start"
    val END = "end"
    val START_AND_END = Set(START, END)

    part1 {
      def traverse(
        current: String,
        seen: Set[String],
        paths: Int
      ): Int = {
        if (current == END) paths + 1
        else {
          graph(current)
            .filterNot(seen)
            .map { cave =>
              val nextSeen = if (cave.toLowerCase() == cave) seen + cave else seen
              traverse(cave, nextSeen, paths)
            }
            .sum
        }
      }

      traverse(START, Set(START), 0)
    }

    part2 {
      def traverse(
        current: String,
        seen: Set[String],
        carry: List[String],
        paths: List[Int],
        smallCave: (String, Boolean)
      ): List[Int] = {
        if (current == END) {
          carry.hashCode() +: paths
        } else {
          graph(current)
            .filterNot(seen)
            .flatMap { cave =>
              val (nextSeen, nextSmallCave) = if (cave == cave.toLowerCase()) {
                // if is special small cave and we have seen it, add it to the
                // official seen list
                //
                // if is special small cave and we have not seen it, flag it as
                // seen but not to the official seen list
                //
                // otherwise, it's a normal small cave
                smallCave match {
                  case (sc, true) if sc == cave  => (seen + cave, smallCave)
                  case (sc, false) if sc == cave => (seen, (sc, true))
                  case _                         => (seen + cave, smallCave)
                }
              } else {
                (seen, smallCave)
              }

              traverse(cave, nextSeen, cave +: carry, paths, nextSmallCave)
            }
        }
      }

      graph.keySet
        .filterNot(START_AND_END)
        .filter(k => k.toLowerCase() == k)
        .flatMap { cave =>
          traverse(START, Set(START), List(START), List.empty, (cave, false))
        }
        .size
    }

    ()
  }

  def buildGraph(edges: List[(String, String)]): Map[String, List[String]] = {
    edges.foldLeft(Map.empty[String, List[String]]) { case (graph, (from, to)) =>
      graph ++ Map(
        from -> (to +: graph.getOrElse(from, List.empty)),
        to -> (from +: graph.getOrElse(to, List.empty))
      )
    }
  }
}
