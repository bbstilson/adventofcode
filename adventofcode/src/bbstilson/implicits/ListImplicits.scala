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
    def groupedBy(pred: A => Boolean): List[List[A]] = {
      val (out, carry) = xs
        .foldLeft((List.empty[List[A]], List.empty[A])) { case ((out, carry), line) =>
          pred(line) match {
            case false => (carry +: out, List.empty)
            case true  => (out, line +: carry)
          }
        }

      carry +: out
    }
  }
}
