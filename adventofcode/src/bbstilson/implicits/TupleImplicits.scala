package bbstilson.implicits

object TupleImplicits {

  implicit class PointTuple2Ops[A: Numeric](t: (A, A)) {
    private val add = Numeric[A].plus _

    def x = t._1
    def y = t._2

    def +(o: (A, A)): (A, A) = (add(x, o.x), add(y, o.y))
  }

  implicit class PointTuple3Ops[A: Numeric](t: (A, A, A)) {
    private val add = Numeric[A].plus _

    def x = t._1
    def y = t._2
    def z = t._3

    def +(o: (A, A, A)): (A, A, A) = (add(x, o.x), add(y, o.y), add(z, o.z))
  }

  implicit class PointTuple4Ops[A: Numeric](t: (A, A, A, A)) {
    private val add = Numeric[A].plus _

    def x = t._1
    def y = t._2
    def z = t._3
    def w = t._4

    def +(o: (A, A, A, A)): (A, A, A, A) = (add(x, o.x), add(y, o.y), add(z, o.z), add(w, o.w))
  }
}
