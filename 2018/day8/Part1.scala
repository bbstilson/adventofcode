import scala.io.Source

object Part1 {
  def main(args: Array[String]): Unit = {
    val source: Iterator[Int] = Source
      .fromFile("input.txt")
      .mkString
      .trim
      .split(' ')
      .map(_.toInt)
      .toIterator

    println(getEntrySum(source))
  }

  private def getEntrySum(source: Iterator[Int]): Int = {
    val numChildNodes = source.next
    val metadataEntries = source.next
    val childSum = (1 to numChildNodes).map(_ => getEntrySum(source)).sum
    val entrySum = (1 to metadataEntries).map(_ => source.next).sum

    childSum + entrySum
  }
}
