package bbstilson.aoc2021

object Day14 extends aocd.Problem(2021, 14) {
  val ReactionRegex = """(.{2}) -> (.)""".r

  def run(input: List[String]): Unit = {
    val init = input.head
      .sliding(2)
      .map(_.toList)
      .map { p => p.head -> p.last }
      .toList
      .groupMapReduce(identity)(_ => 1L)(_ + _)

    val reactions = input
      .drop(2)
      .map { case ReactionRegex(input, output) =>
        val p = input.toCharArray().toList
        (p.head -> p.last) -> output.charAt(0)
      }
      .toMap

    def step(
      molecule: Map[(Char, Char), Long],
      counts: Map[Char, Long]
    ): (Map[(Char, Char), Long], Map[Char, Long]) =
      molecule.foldLeft((Map.empty[(Char, Char), Long], counts)) {
        case ((map, cs), (pair @ (x, y), count)) => {
          val reaction = reactions(pair)
          val p1 = (x, reaction)
          val p2 = (reaction, y)

          val nextMap = map ++ Map(
            p1 -> (count + map.getOrElse(p1, 0L)),
            p2 -> (count + map.getOrElse(p2, 0L))
          )

          val nextCounts = (cs + (reaction -> (cs.getOrElse(reaction, 0L) + count)))

          nextMap -> nextCounts
        }
      }

    def doNSteps(n: Int): Long = {
      val charCounts = input.head
        .groupMapReduce(identity)(_ => 1L)(_ + _)

      val (_, counts) = (0 until n)
        .foldLeft((init, charCounts)) { case ((molecule, counts), _) =>
          step(molecule, counts)
        }

      val sorted = counts.toList.map(_._2).sorted
      sorted.last - sorted.head
    }

    part1 {
      doNSteps(10)
    }

    part2 {
      doNSteps(40)
    }

    ()
  }
}
