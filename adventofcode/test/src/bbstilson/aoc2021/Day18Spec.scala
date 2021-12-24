package bbstilson.aoc2021

import utest._

object Day18Spec extends TestSuite {
  import Day18._

  val tests = Tests {
    test("single explode") {
      List(
        "[[[[[9,8],1],2],3],4]" -> "[[[[0,9],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]" -> "[7,[6,[5,[7,0]]]]",
        "[[6,[5,[4,[3,2]]]],1]" -> "[[6,[5,[7,0]]],3]",
        "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]" -> "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
        "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]" -> "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
        "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]" -> "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
      ).foreach {
        case (input, expected) => {
          val parsed = parse(input)
          explode(parsed).show ==> expected
        }
      }
    }

    test("split") {
      List(
        PairToken(
          0,
          PairToken(1, PairToken(2, LiteralToken(3, 0), LiteralToken(4, 7)), LiteralToken(5, 4)),
          PairToken(6, LiteralToken(7, 15), PairToken(8, LiteralToken(9, 0), LiteralToken(10, 13)))
        ) -> "[[[0,7],4],[[7,8],[0,13]]]"
      ).foreach {
        case (parsed, expected) => {
          split(parsed).show ==> expected
        }
      }
    }

    test("reduce") {
      List(
        "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]" -> "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
        // "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]" -> "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
      ).foreach { case (input, output) =>
        reduce(parse(input)).show ==> output
      }
    }

    test("example 1") {
      // after addition
      val start = parse("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]")
      // after explode
      val s1 = step(start)
      s1.show ==> "[[[[0,7],4],[7,[[8,4],9]]],[1,1]]"
      // after explode
      val s2 = step(s1)
      s2.show ==> "[[[[0,7],4],[15,[0,13]]],[1,1]]"
      // after split
      val s3 = step(s2)
      s3.show ==> "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"
      // after split
      val s4 = step(s3)
      s4.show ==> "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"
      // after explode
      val s5 = step(s4)
      s5.show ==> "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
    }

    test("magnitude") {
      List(
        "[[1,2],[[3,4],5]]" -> 143L,
        "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]" -> 1384L,
        "[[[[1,1],[2,2]],[3,3]],[4,4]]" -> 445L,
        "[[[[3,0],[5,3]],[4,4]],[5,5]]" -> 791L,
        "[[[[5,0],[7,4]],[5,5]],[6,6]]" -> 1137L,
        "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]" -> 3488L
      ).foreach { case (input, mag) =>
        parse(input).magnitude ==> mag
      }
    }
  }
}
