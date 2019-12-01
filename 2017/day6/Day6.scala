import scala.annotation.tailrec

object Day6 {
  def main(args: Array[String]): Unit = {
    val memoryBank = List(14,0,15,12,11,11,3,5,1,6,8,4,9,1,8,4)
    println(part1(memoryBank, Set(mkAlloc(memoryBank)), 1))
    println(part2(memoryBank, Map(mkAlloc(memoryBank) -> 0), 1))
  }

  @tailrec
  def part1(memoryBank: List[Int], memo: Set[String], depth: Int): Int = {
    val nextBank = rebalance(memoryBank)
    val alloc = mkAlloc(nextBank)
    if (memo.contains(alloc)) depth else part1(nextBank, memo + alloc, depth + 1)
  }

  @tailrec
  def part2(memoryBank: List[Int], memo: Map[String, Int], depth: Int): Int = {
    val nextBank = rebalance(memoryBank)
    val alloc = mkAlloc(nextBank)

    memo.get(alloc) match {
      case Some(depthSeen) => depth - depthSeen
      case None => part2(nextBank, memo + (alloc -> depth), depth + 1)
    }
  }

  def mkAlloc(bank: List[Int]): String = bank.mkString("|")

  def rebalance(bank: List[Int]): List[Int] = {
    val withIndex = bank.zipWithIndex
    val (maxValue, maxIndex) = withIndex.maxBy(_._1)
    val amtBulkAdd = maxValue / bank.size

    val (_, oneMoreSet) = (0 until maxValue % bank.size)
      .foldLeft((maxIndex + 1, Set.empty[Int])) { case ((idx, set), _) =>
        if (idx >= bank.size) (1, set + 0) else (idx + 1, set + idx)
      }

    def needsOneMore(idx: Int): Boolean = oneMoreSet.contains(idx)

    withIndex
      // First, 0 out block we took from.
      .map { case (block, idx) => if (idx == maxIndex) (0, idx) else (block, idx) }
      // Next, add bulk amounts.
      .map { case (block, idx) => (block + amtBulkAdd, idx) }
      // Finally, increment remainders.
      .map { case (block, idx) => if (needsOneMore(idx)) block + 1 else block }
  }
}
