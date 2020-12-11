package bbstilson.aoc2020

import scala.annotation.tailrec

object Day11 extends aocd.Problem(2020, 11) {
  type Point = (Int, Int)
  type IndexedBoard = Vector[(Vector[(Char, Int)], Int)]
  type ViewableAngles = Map[(Int, Int), SegmentedViewable]

  def run(input: List[String]): Unit = {
    val indexed = input.map(_.zipWithIndex.toVector).zipWithIndex.toVector
    val viewableAngles = mkViewAngles(indexed)
    println("built viewable angles")
    val p1 = runSimulation(indexed, adjacentSeatsP1, 4)
    val p2 = runSimulation(indexed, adjacentSeatsP2(viewableAngles), 5)
    println(p1)
    println(p2)
  }

  case class SegmentedViewable(
    up: List[Point] = List.empty,
    down: List[Point] = List.empty,
    left: List[Point] = List.empty,
    right: List[Point] = List.empty,
    upRight: List[Point] = List.empty,
    downRight: List[Point] = List.empty,
    downLeft: List[Point] = List.empty,
    upLeft: List[Point] = List.empty
  ) {

    def merge(o: SegmentedViewable): SegmentedViewable = copy(
      up = (up ++ o.up).sortBy(_._2),
      down = (down ++ o.down).sortBy(-_._2),
      left = (left ++ o.left).sortBy(-_._1),
      right = (right ++ o.right).sortBy(_._1),
      upRight = (upRight ++ o.upRight).sortBy(_._1),
      downRight = (downRight ++ o.downRight).sortBy(_._1),
      downLeft = (downLeft ++ o.downLeft).sortBy(-_._1),
      upLeft = (upLeft ++ o.upLeft).sortBy(-_._1)
    )

    def all = up ++ down ++ left ++ right ++ upLeft ++ upRight ++ downLeft ++ downRight
  }

  object SegmentedViewable {

    def fromPointAndTarget(origin: Point, t: Point): SegmentedViewable =
      (origin, t) match {
        case ((x1, y1), (x2, y2)) if y1 == y2 && x1 < x2 => SegmentedViewable(right = List(t))
        case ((x1, y1), (x2, y2)) if y1 == y2 && x1 > x2 => SegmentedViewable(left = List(t))
        case ((x1, y1), (x2, y2)) if x1 == x2 && y1 < y2 => SegmentedViewable(up = List(t))
        case ((x1, y1), (x2, y2)) if x1 == x2 && y1 > y2 => SegmentedViewable(down = List(t))
        case ((x1, y1), (x2, y2)) if x1 < x2 && y1 < y2  => SegmentedViewable(upRight = List(t))
        case ((x1, y1), (x2, y2)) if x1 < x2 && y1 > y2  => SegmentedViewable(downRight = List(t))
        case ((x1, y1), (x2, y2)) if x1 > x2 && y1 > y2  => SegmentedViewable(downLeft = List(t))
        case ((x1, y1), (x2, y2)) if x1 > x2 && y1 < y2  => SegmentedViewable(upLeft = List(t))
        case _                                           => SegmentedViewable()
      }
  }

  def mkViewAngles(board: IndexedBoard): ViewableAngles = {
    val coords = for {
      (line, row) <- board
      (char, col) <- line
    } yield (row, col)

    val angles = for {
      viewer <- coords
      viewed <- coords
      if Math.abs(slope(viewer, viewed)) == 1
    } yield viewer -> viewed

    angles
      .groupMap(_._1)(_._2)
      .map { case (point, viewable) =>
        val (x, y) = point
        val row = (0 until board.size).collect {
          case r if (r, y) != point => (r, y)
        }.toSet
        val col = (0 until board.head._1.size).collect { case c if (x, c) != point => (x, c) }.toSet
        point -> (row ++ col ++ viewable)
      }
      .map { case (point, viewable) => point -> segment(point, viewable) }
  }

  def segment(point: Point, viewable: Set[Point]): SegmentedViewable =
    viewable.map(p => SegmentedViewable.fromPointAndTarget(point, p)).reduce(_ merge _)

  def slope(p1: Point, p2: Point): Double = {
    val (x1, y1) = p1
    val (x2, y2) = p2
    (y2.toDouble - y1.toDouble) / (x2.toDouble - x1.toDouble)
  }

  @tailrec
  def runSimulation(
    board: IndexedBoard,
    visibilityFn: (IndexedBoard, (Int, Int)) => Int,
    maxFriends: Int
  ): Int = {
    val next = board.map { case (line, row) =>
      line.map { case (char, col) =>
        val numAdj = visibilityFn(board, (row, col))
        val next = char match {
          case 'L' if numAdj == 0          => '#'
          case '#' if numAdj >= maxFriends => 'L'
          case _                           => char
        }
        next -> col
      } -> row
    }
    if (next == board) countOccupiedSeats(board) else runSimulation(next, visibilityFn, maxFriends)
  }

  def adjacentSeatsP1(board: IndexedBoard, pos: (Int, Int)): Int = {
    val (x, y) = pos
    val adjacents = for {
      row <- (x - 1) to (x + 1)
      col <- (y - 1) to (y + 1)
      if row >= 0 && col >= 0 && row < board.size && col < board.head._1.size && (row, col) != pos
    } yield board.at((row, col))
    adjacents.count(_ == '#')
  }

  def adjacentSeatsP2(viewableAngles: ViewableAngles)(board: IndexedBoard, pos: (Int, Int)): Int = {
    val viewable = viewableAngles(pos)
    List(
      viewable.up.find(p => board.at(p) != '.'),
      viewable.down.find(p => board.at(p) != '.'),
      viewable.left.find(p => board.at(p) != '.'),
      viewable.right.find(p => board.at(p) != '.'),
      viewable.upRight.find(p => board.at(p) != '.'),
      viewable.downRight.find(p => board.at(p) != '.'),
      viewable.downLeft.find(p => board.at(p) != '.'),
      viewable.upLeft.find(p => board.at(p) != '.')
    ).flatten.count(p => board.at(p) == '#')
  }

  def countOccupiedSeats(board: IndexedBoard): Int =
    board.map { case (row, _) => row.count { case (char, _) => char == '#' } }.sum

  implicit class BoardPoint(board: IndexedBoard) {
    def at(p: Point): Char = board(p._1)._1(p._2)._1
  }
}
