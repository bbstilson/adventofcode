package bbstilson.aoc2018

object Day3 {
  case class Point(x: Int, y: Int)
  case class Claim(p1: Point, p2: Point)

  case class Order(id: Int, claim: Claim) {

    // Neato site: https://silentmatt.com/rectangle-intersection/
    def collidesWith(o: Order): Boolean = {
      claim.p1.x < o.claim.p2.x &&
      claim.p2.x > o.claim.p1.x &&
      claim.p1.y < o.claim.p2.y &&
      claim.p2.y > o.claim.p1.y
    }
  }

  def main(args: Array[String]): Unit = {
    val orders: List[Order] = io.Source
      .fromResource("2018/day3/input.txt")
      .getLines()
      .toList
      .map(parseOrder)

    val unique = orders.filterNot(order => orders.exists(o => order != o && order.collidesWith(o)))

    // Should only be one, but just in case print everything.
    unique.foreach(println)
  }

  val orderRegex = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".r

  private def parseOrder(line: String): Order = {
    val orderRegex(orderId, posX, posY, width, height) = line

    val x = posX.toInt
    val y = posY.toInt
    val w = width.toInt
    val h = height.toInt

    Order(
      id = orderId.toInt,
      claim = Claim(
        p1 = Point(x, y),
        p2 = Point(x + w, y + h)
      )
    )
  }
}
