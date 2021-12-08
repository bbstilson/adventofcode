package bbstilson.aoc2021

object Day8 extends aocd.Problem(2021, 8) {
  def run(input: List[String]): Unit = {

    val parsed = input.map(_.split(""" \| """).toList.map(_.split(" ").toList)).map { case xs =>
      (xs.head, xs.last)
    }

    val segmentsToNumbers = Map(
      "abcefg" -> 0,
      "cf" -> 1,
      "acdeg" -> 2,
      "acdfg" -> 3,
      "bcdf" -> 4,
      "abdfg" -> 5,
      "abdefg" -> 6,
      "acf" -> 7,
      "abcdefg" -> 8,
      "abcdfg" -> 9
    )

    part1 {
      val numSize = Set(2, 3, 4, 7) // 1, 7, 4, 8
      parsed.map { case (_, ys) => ys.count { y => numSize.contains(y.size) } }.sum
    }

    part2 {
      def parseMessage(cipher: List[String], msg: List[String]): Int = {
        // group by segment count
        val cipherBySegmentCount = cipher.groupMap(_.size)(_.toSet)

        val one = cipherBySegmentCount(2).head
        val four = cipherBySegmentCount(4).head
        val seven = cipherBySegmentCount(3).head
        val eight = cipherBySegmentCount(7).head

        // A = 3 diff 2
        val a = seven.diff(one).head

        // G = 6s.diff(4 + A) == 1
        val fourAndA = four + a
        val g = cipherBySegmentCount(6).collectFirst {
          case c if c.diff(fourAndA).size == 1 => c.diff(fourAndA).head
        }.get

        // D = 5s.diff(one + A + G) == 1
        val agone = one + a + g
        val d = cipherBySegmentCount(5).collectFirst {
          case c if c.diff(agone).size == 1 => c.diff(agone).head
        }.get

        // B = (one + D).diff(4)
        val b = cipherBySegmentCount(4).head.diff(one + d).head

        // E = ((where (one + A + B + G).diff(6s) == 1) AND NOT EQUAL D)
        val abgone = one + a + b + g
        val e = cipherBySegmentCount(6)
          .map(c => c -> c.diff(abgone))
          .collectFirst { case (_, diff) if diff.size == 1 && diff.head != d => diff.head }
          .head

        // F = (where (A + B + D + G).diff(5s) == 1)
        val abdg = Set(a, b, d, g)
        val f = cipherBySegmentCount(5).collectFirst {
          case c if c.diff(abdg).size == 1 => c.diff(abdg).head
        }.get

        // Get C
        val c = eight.diff(Set(a, b, d, e, f, g)).head

        val fixedMapping = Map(
          a -> 'a',
          b -> 'b',
          c -> 'c',
          d -> 'd',
          e -> 'e',
          f -> 'f',
          g -> 'g'
        )

        msg
          .map(_.toCharArray().map(fixedMapping).mkString.sorted)
          .map(segmentsToNumbers)
          .mkString
          .toInt
      }

      parsed.map { case (cipher, msg) => parseMessage(cipher, msg) }.sum
    }

    ()
  }
}
