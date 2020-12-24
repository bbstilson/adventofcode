package bbstilson.aoc2020

// import java.util.concurrent.atomic.AtomicInteger

object Day23 extends aocd.Problem(2020, 23) {

  case class State(xs: List[Int], seen: Set[Int] = Set.empty, turn: Int = 1)

  def run(input: List[String]): Unit = {
    // val start = input.head.split("").map(_.toInt).toList
    val start = List(3, 8, 9, 1, 2, 5, 4, 6, 7)
    val max = start.max

    part1 {
      val result = LazyList.iterate(State(start))(round(max)).drop(100).head.xs
      val newTail = result.takeWhile(_ != 1)
      val newHead = result.dropWhile(_ != 1).tail
      (newHead ++ newTail).mkString
    }

    part2 {
      // val labeler = new AtomicInteger()
      // start.foreach(_ => labeler.incrementAndGet())
      // val p2Start = start ++ List.fill(1_000_000 - start.size)(labeler.incrementAndGet())
      // val result = LazyList.iterate(State(p2Start))(round(max)).drop(100_000).head.xs
      // result.dropWhile(_ != 1).tail.take(2)
    }
    ()
  }

  def round(max: Int)(state: State): State = {
    val State(xs, seen, step) = state
    val current = xs.head
    val selected = xs.tail.take(3)
    val nextInit = xs.tail.drop(3)
    val destination = {
      val start = if (current == 1) max else current - 1
      val remaining = if (start != max) (max to (start + 2) by -1) else IndexedSeq.empty
      ((start to 1 by -1) ++ remaining).filterNot(selected.contains).head
    }
    val mid = nextInit.dropWhile(_ != destination)
    val nextXs = List(
      nextInit.takeWhile(_ != destination),
      List(mid.head),
      selected,
      mid.tail,
      List(current)
    ).flatten

    if (state.seen.contains(nextXs.hashCode) || step > 10000) {
      println(step)
      println(seen.size)
      throw new Exception("wah")
    }

    State(nextXs, seen + nextXs.hashCode(), step + 1)
  }
}
