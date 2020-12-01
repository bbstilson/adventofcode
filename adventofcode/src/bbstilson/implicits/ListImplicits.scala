package bbstilsonimplicits

object ListImplicits {

  implicit class FrequencyMapOps[A](xs: List[A]) {
    def toFrequencyMap: Map[A, Int] = xs.groupMapReduce(identity)(_ => 1)(_ + _)
  }
}
