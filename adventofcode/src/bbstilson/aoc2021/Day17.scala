package bbstilson.aoc2021

// import scala.annotation.tailrec

object Day17 extends aocd.Problem(2021, 17) {
  type Point = (Int, Int)

  implicit class PointOps(p: Point) {
    def x: Int = p._1
    def y: Int = p._2
  }

  case class Projectile(velocity: Point, position: Point = (0, 0))
  case class Area(bl: Point, tr: Point) {
    def contains(p: Point): Boolean = p.x >= bl.x && p.x <= tr.x && p.y >= bl.y && p.y <= tr.y
  }

  def run(input: List[String]): Unit = {
    implicit val target = Area((257, -101), (286, -57))

    part1 {
      val initVelocities = for {
        x <- 0 to target.tr.x
        y <- 0 to Math.abs(target.tr.y - target.bl.y).toInt * 4
      } yield x -> y

      val highest = initVelocities
        .filter { velocity => canHit(Projectile(velocity)) }
        .maxBy(_._2)

      val init = Projectile(highest)
      val ps = LazyList.unfold(init) { projectile =>
        if (stillValid(projectile.position)) Some((projectile -> step(projectile)))
        else None
      }
      ps.maxBy(_.position.y).position._2
    }

    part2 {
      val absY = Math.abs(target.tr.y - target.bl.y).toInt * 4
      val initVelocities = for {
        x <- -target.tr.x to target.tr.x
        y <- -absY to absY
      } yield x -> y

      initVelocities.filter { velocity => canHit(Projectile(velocity)) }.size
    }

    ()

  }

  def stillValid(pos: Point)(implicit target: Area): Boolean =
    pos.x <= target.tr.x && pos.y >= target.bl.y

  // iterate until it cannot hit the target
  // further right than the target, or below the target
  // @tailrec
  def canHit(projectile: Projectile)(implicit target: Area): Boolean = {
    if (target.contains(projectile.position)) true
    else if (!stillValid(projectile.position)) false
    else canHit(step(projectile))
  }

  def step(projectile: Projectile): Projectile = {
    // The probe's x position increases by its x velocity.
    // The probe's y position increases by its y velocity.
    val currPos = projectile.position
    val currVel = projectile.velocity
    val nextPos = (currPos.x + currVel.x, currPos.y + currVel.y)
    // Due to drag, the probe's x velocity changes by 1 toward the value 0;
    // that is, it decreases by 1 if it is greater than 0, increases by 1
    // if it is less than 0, or does not change if it is already 0.
    val nextVelX = if (currVel.x == 0) 0 else if (currVel.x > 0) currVel.x - 1 else currVel.x + 1
    // Due to gravity, the probe's y velocity decreases by 1.
    val nextVel = (nextVelX, currVel.y - 1)

    Projectile(velocity = nextVel, position = nextPos)
  }
}
