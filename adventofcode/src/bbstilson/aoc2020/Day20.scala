package bbstilson.aoc2020

import bbstilson.implicits.ListImplicits._

object Day20 extends aocd.Problem(2020, 20) {
  case class Side(change: Int)

  val TileRegex = raw"Tile (\d+):".r

  def run(input: List[String]): Unit = {
    part1(input)
    ()
  }

  def part1(input: List[String]): Long = part1 {
    matchTiles(input)
      .filter { case (_, matches) =>
        matches.size == 2
      }
      .keySet
      .product
  }

  def matchTiles(input: List[String]): Map[Long, Map[Long, (Side, Side)]] = time(
    "make matches", {
      val idToUncorrectedImg: Map[Long, List[String]] = input
        .groupedBy(_.nonEmpty)
        .collect {
          case xs if xs.nonEmpty =>
            TileRegex.findFirstMatchIn(xs.head).get.group(1).toLong -> xs.tail
        }
        .toMap

      val idToSides: Map[Long, List[(Side, String)]] = idToUncorrectedImg.map { case (id, img) =>
        id -> getSidesFromImg(img)
      }
      val idToSidesFlatten: List[(Long, Side, String)] = idToSides.toList.flatMap {
        case (id, sides) =>
          sides.map { case (side, str) => (id, side, str) }
      }

      idToUncorrectedImg.keys.toList
        .flatMap { id =>
          for {
            (side, str) <- idToSides(id)
            (oid, oSide, oStr) <- idToSidesFlatten
            if oid != id && str == oStr
          } yield id -> Map(
            oid -> (side -> oSide)
          )
        }
        .groupMapReduce(_._1)(_._2)(_ ++ _)
    }
  )

  private def getSidesFromImg(img: List[String]): List[(Side, String)] = {
    val img1: List[String] = img.transpose.map(_.mkString)
    List(
      (Side(1), img.head),
      (Side(-1), img.head.reverse),
      (Side(2), img.last),
      (Side(-2), img.last.reverse),
      (Side(3), img1.head),
      (Side(-3), img1.head.reverse),
      (Side(4), img1.last),
      (Side(-4), img1.last.reverse)
    )
  }
}
