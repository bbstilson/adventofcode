package bbstilson.aoc2020

import utest._

object Day11Spec extends TestSuite {
  import Day11.SegmentedViewable
  import Day11.SegmentedViewable._

  val tests = Tests {
    test("places points in the right segment") {
      val me = (1, 1)
      fromPointAndTarget(me, (2, 1)) ==> SegmentedViewable(right = List((2, 1)))
      fromPointAndTarget(me, (0, 1)) ==> SegmentedViewable(left = List((0, 1)))
      fromPointAndTarget(me, (1, 2)) ==> SegmentedViewable(up = List((1, 2)))
      fromPointAndTarget(me, (1, 0)) ==> SegmentedViewable(down = List((1, 0)))
      fromPointAndTarget(me, (2, 2)) ==> SegmentedViewable(upRight = List((2, 2)))
      fromPointAndTarget(me, (2, 0)) ==> SegmentedViewable(downRight = List((2, 0)))
      fromPointAndTarget(me, (0, 0)) ==> SegmentedViewable(downLeft = List((0, 0)))
      fromPointAndTarget(me, (0, 2)) ==> SegmentedViewable(upLeft = List((0, 2)))
    }

    test("orders points correctly") {
      val u1 = SegmentedViewable(up = List((1, 1)))
      val u2 = SegmentedViewable(up = List((1, 2)))
      u1.merge(u2).up ==> List((1, 1), (1, 2))
      u2.merge(u1).up ==> List((1, 1), (1, 2))

      val d1 = SegmentedViewable(down = List((1, 1)))
      val d2 = SegmentedViewable(down = List((1, 2)))
      d1.merge(d2).down ==> List((1, 2), (1, 1))
      d2.merge(d1).down ==> List((1, 2), (1, 1))

      val l1 = SegmentedViewable(left = List((1, 1)))
      val l2 = SegmentedViewable(left = List((0, 1)))
      l1.merge(l2).left ==> List((1, 1), (0, 1))
      l2.merge(l1).left ==> List((1, 1), (0, 1))

      val r1 = SegmentedViewable(right = List((1, 1)))
      val r2 = SegmentedViewable(right = List((0, 1)))
      r1.merge(r2).right ==> List((0, 1), (1, 1))
      r2.merge(r1).right ==> List((0, 1), (1, 1))

      val ur1 = SegmentedViewable(upRight = List((1, 1)))
      val ur2 = SegmentedViewable(upRight = List((2, 2)))
      ur1.merge(ur2).upRight ==> List((1, 1), (2, 2))
      ur2.merge(ur1).upRight ==> List((1, 1), (2, 2))

      val dr1 = SegmentedViewable(downRight = List((1, 1)))
      val dr2 = SegmentedViewable(downRight = List((2, 0)))
      dr1.merge(dr2).downRight ==> List((1, 1), (2, 0))
      dr2.merge(dr1).downRight ==> List((1, 1), (2, 0))

      val ul1 = SegmentedViewable(upLeft = List((1, 1)))
      val ul2 = SegmentedViewable(upLeft = List((0, 2)))
      ul1.merge(ul2).upLeft ==> List((1, 1), (0, 2))
      ul2.merge(ul1).upLeft ==> List((1, 1), (0, 2))

      val dl1 = SegmentedViewable(downLeft = List((1, 1)))
      val dl2 = SegmentedViewable(downLeft = List((0, 0)))
      dl1.merge(dl2).downLeft ==> List((1, 1), (0, 0))
      dl2.merge(dl1).downLeft ==> List((1, 1), (0, 0))

    }
  }
}
