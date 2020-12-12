package bbstilson.aoc2020

import utest._

object Day12Spec extends TestSuite {

  val tests = Tests {
    test("waypoint") {
      test("rotates around") {
        import Day12._
        val waypoint = (1, 1)
        waypoint.rotate(RotateLeft, 90) ==> ((-1, 1))
        waypoint.rotate(RotateLeft, 180) ==> ((-1, -1))
        waypoint.rotate(RotateLeft, 270) ==> ((1, -1))
        waypoint.rotate(RotateLeft, 360) ==> waypoint

        waypoint.rotate(RotateRight, 90) ==> ((1, -1))
        waypoint.rotate(RotateRight, 180) ==> ((-1, -1))
        waypoint.rotate(RotateRight, 270) ==> ((-1, 1))
        waypoint.rotate(RotateRight, 360) ==> waypoint
      }
    }
  }
}
