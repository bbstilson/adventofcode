package bbstilson.aoc2021

import utest._

object Day18Spec extends TestSuite {
  import Day18._

  val tests = Tests {
    test("single explode") {
      List(
        // the 9 has no regular number to its left, so it is not added to any
        // regular number
        "[[[[[9,8],1],2],3],4]" -> "[[[[0,9],2],3],4]",
        // the 2 has no regular number to its right, and so it is not added to any
        // regular number
        "[7,[6,[5,[4,[3,2]]]]]" -> "[7,[6,[5,[7,0]]]]",
        "[[6,[5,[4,[3,2]]]],1]" -> "[[6,[5,[7,0]]],3]",
        // the pair [3,2] is unaffected because the pair [7,3] is further to the
        // left; [3,2] would explode on the next action
        "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]" -> "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
        "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]" -> "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"
      ).foreach {
        case (rawPre, _) => {
          val actual = parseNumber(rawPre.iterator)
          // val expected = parseNumber(rawPost.iterator)
          println(findPathToExplosion(actual))
        }
      }
    }

    test("magnitude") {
      List(
        "[[1,2],[[3,4],5]]" -> 143L,
        "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]" -> 1384L,
        "[[[[1,1],[2,2]],[3,3]],[4,4]]" -> 445L,
        "[[[[3,0],[5,3]],[4,4]],[5,5]]" -> 791L,
        "[[[[5,0],[7,4]],[5,5]],[6,6]]" -> 1137L,
        "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]" -> 3488L
      ).foreach { case (raw, mag) =>
        parseNumber(raw.iterator).magnitude ==> mag
      }
    }
  }
}
