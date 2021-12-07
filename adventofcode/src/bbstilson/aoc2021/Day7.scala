package bbstilson.aoc2021

import scala.annotation.tailrec

object Day7 extends aocd.Problem(2021, 7) {
  def run(input: List[String]): Unit = {
    val xs = input.head.split(",").map(_.toInt).toVector.sorted
    val median = xs(xs.size / 2)

    part1 {
      xs.map(x => Math.abs(x - median)).sum
    }

    part2 {
      @tailrec
      def facAdd(n: Int, acc: Int = 0): Int = n match {
        case 0 => acc
        case _ => facAdd(n - 1, acc + n)
      }

      def getCost(pivot: Int): Int = xs.map(x => facAdd(Math.abs(x - pivot))).sum

      @tailrec
      def search(pivot: Int, prevTotal: Int): Int = {
        val nextCost = getCost(pivot)
        if (prevTotal < nextCost) prevTotal else search(pivot + 1, nextCost)
      }

      search(median, getCost(median))
    }

    ()
  }
}
