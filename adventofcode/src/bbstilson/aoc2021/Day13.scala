package bbstilson.aoc2021

import bbstilson.implicits.ListImplicits._

object Day13 extends aocd.Problem(2021, 13) {
  val FoldRegex = """fold along (y|x)=(\d+)""".r

  sealed trait Axis
  case object X extends Axis
  case object Y extends Axis
  case class Fold(axis: Axis, value: Int)

  def run(input: List[String]): Unit = {
    val grouped = input.groupedBy(_.nonEmpty)
    val points: Set[(Int, Int)] = grouped.head
      .map(_.split(","))
      .map(a => a.head.toInt -> a.last.toInt)
      .toSet

    val folds = grouped.last
      .map { case FoldRegex(rawAxis, value) =>
        val axis = if (rawAxis.charAt(0) == 'x') X else Y
        Fold(axis, value.toInt)
      }

    part1 {
      doFold(folds.head, points).size
    }

    part2 {
      printPoints(folds.foldLeft(points) { case (ps, fold) => doFold(fold, ps) })
    }

    ()
  }

  def doFold(fold: Fold, points: Set[(Int, Int)]): Set[(Int, Int)] = {
    points.map { case (x, y) =>
      fold.axis match {
        case X =>
          if (x < fold.value) (x, y)
          else (x - (x - fold.value) * 2, y)
        case Y =>
          if (y < fold.value) (x, y)
          else (x, y - (y - fold.value) * 2)
      }
    }
  }

  def printPoints(ps: Set[(Int, Int)]): String = {
    val sb = new StringBuilder()
    (ps.minBy(_._2)._2 to ps.maxBy(_._2)._2).foreach { y =>
      sb.append("\n")
      (ps.minBy(_._1)._1 to ps.maxBy(_._1)._1).foreach { x =>
        val cell = if (ps.contains((x, y))) '#' else ' '
        sb.append(cell)
      }
    }
    sb.mkString
  }
}
