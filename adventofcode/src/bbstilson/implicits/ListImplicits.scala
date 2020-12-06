package bbstilson.implicits

object ListImplicits {

  implicit class FrequencyMapOps[A](xs: List[A]) {
    def toFrequencyMap: Map[A, Int] = xs.groupMapReduce(identity)(_ => 1)(_ + _)

    /** Groups the input into groups by new lines.
      *  Ex:
      *  List(
      *   "aa",
      *   "aaaa",
      *   "",
      *   "bb",
      *   "bb",
      *   "",
      *   "c"
      * )
      * Yields:
      *  List(
      *    List("aa", "aaaa"),
      *    List("bb", "bb"),
      *    List("c")
      *  )
      * @param input
      * @return
      */
    def groupedByNewlines: List[List[A]] = {
      val (out, carry) = xs
        .foldLeft((List.empty[List[A]], List.empty[A])) { case ((out, carry), line) =>
          line match {
            case "" => (carry +: out, List.empty)
            case _  => (out, line +: carry)
          }
        }

      carry +: out
    }
  }
}
