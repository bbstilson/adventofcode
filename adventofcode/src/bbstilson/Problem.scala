package bbstilson

abstract class Problem[A](dir: String, lineFn: String => A) {
  def run(input: List[A]): Unit

  def main(args: Array[String]): Unit = {
    val input = scala.io.Source
      .fromResource(s"2020/$dir/input.txt")
      .getLines()
      .toList
      .map(lineFn)

    run(input)
  }
}
