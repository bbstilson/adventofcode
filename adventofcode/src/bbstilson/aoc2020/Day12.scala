package bbstilson.aoc2020

object Day12 extends aocd.Problem(2020, 12) {
  val ActionRegex = raw"(.)(\d+)".r
  type Actions = List[(Action, Int)]
  type Point = (Int, Int)

  def run(input: List[String]): Unit = {
    val actions = mkActions(input)
    part1(actions)
    part2(actions)
    ()
  }

  def part1(actions: Actions): Int = part1 {
    val ship = actions.foldLeft(Ship()) { case (ship, (action, amount)) =>
      action match {
        case Forward     => ship.move(amount = amount)
        case North       => ship.move(North, amount)
        case South       => ship.move(South, amount)
        case East        => ship.move(East, amount)
        case West        => ship.move(West, amount)
        case RotateLeft  => ship.rotate(RotateLeft, amount)
        case RotateRight => ship.rotate(RotateRight, amount)
      }
    }
    val (x, y) = ship.position
    Math.abs(x) + Math.abs(y)
  }

  def part2(actions: Actions): Int = part2 {
    val (ship, _) = actions.foldLeft((Ship(), (10, 1))) {
      case ((ship, waypoint), (action, amount)) =>
        action match {
          case Forward     => (ship.moveTowards(waypoint, amount), waypoint)
          case North       => (ship, (waypoint.x, waypoint.y + amount))
          case South       => (ship, (waypoint.x, waypoint.y - amount))
          case East        => (ship, (waypoint.x + amount, waypoint.y))
          case West        => (ship, (waypoint.x - amount, waypoint.y))
          case RotateLeft  => (ship, waypoint.rotate(RotateLeft, amount))
          case RotateRight => (ship, waypoint.rotate(RotateRight, amount))
        }
    }
    val (x, y) = ship.position
    Math.abs(x) + Math.abs(y)
  }

  implicit class PointImplicits(point: (Int, Int)) {
    def x = point._1
    def y = point._2

    def rotate(rotation: Rotation, amount: Int): Point = {
      (0 until (amount / 90)).foldLeft(point) { case (p, _) =>
        rotation match {
          case RotateLeft  => (-p.y, p.x)
          case RotateRight => (p.y, -p.x)
        }
      }
    }
  }

  private def mkActions(input: List[String]): Actions = input.map {
    case ActionRegex(action, amount) =>
      (action.toCharArray().head, amount.toInt) match {
        case ('N', amt) => (North, amt)
        case ('S', amt) => (South, amt)
        case ('E', amt) => (East, amt)
        case ('W', amt) => (West, amt)
        case ('L', amt) => (RotateLeft, amt)
        case ('R', amt) => (RotateRight, amt)
        case ('F', amt) => (Forward, amt)
      }
  }
}

sealed trait Action
case object Forward extends Action

sealed trait Rotation
case object RotateLeft extends Rotation with Action
case object RotateRight extends Rotation with Action

sealed trait Direction {
  def left: Direction
  def right: Direction
}

case object North extends Direction with Action {
  def left: Direction = West
  def right: Direction = East
}

case object South extends Direction with Action {
  def left: Direction = East
  def right: Direction = West
}

case object East extends Direction with Action {
  def left: Direction = North
  def right: Direction = South
}

case object West extends Direction with Action {
  def left: Direction = South
  def right: Direction = North
}

import Day12._

case class Ship(
  direction: Direction = East,
  position: Point = (0, 0)
) {

  def move(d: Direction = direction, amount: Int): Ship = d match {
    case North => copy(position = (position.x, position.y + amount))
    case South => copy(position = (position.x, position.y - amount))
    case East  => copy(position = (position.x + amount, position.y))
    case West  => copy(position = (position.x - amount, position.y))
  }

  def moveTowards(waypoint: Point, amount: Int): Ship = (0 until amount).foldLeft(this) {
    case (ship, _) =>
      ship.copy(position = (ship.position.x + waypoint.x, ship.position.y + waypoint.y))
  }

  def rotate(rotation: Rotation, amount: Int): Ship =
    copy(direction = (0 until (amount / 90)).foldLeft(direction) { case (dir, _) =>
      rotation match {
        case RotateLeft  => dir.left
        case RotateRight => dir.right
      }
    })
}
