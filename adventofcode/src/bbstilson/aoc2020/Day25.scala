package bbstilson.aoc2020

object Day25 extends aocd.Problem(2020, 25) {

  def run(input: List[String]): Unit = {
    part1 {
      transformer(input.head.toLong)
        .take(findLoopSize(input.last.toLong))
        .toList
        .last
    }
    ()
  }

  def findLoopSize(target: Long): Int = transformer(7L).takeWhile(k => k != target).toList.size + 1

  def transformer(sn: Long): LazyList[Long] = LazyList.iterate(sn)(_ * sn % 20201227L)
}
