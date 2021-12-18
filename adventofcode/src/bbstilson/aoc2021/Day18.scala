package bbstilson.aoc2021

import java.util.concurrent.atomic.AtomicInteger

object Day18 extends aocd.Problem(2021, 18) {

  sealed trait Number {
    val id: Int
    def magnitude: Long
  }
  case class Pair(id: Int, l: Number, r: Number) extends Number {
    def magnitude: Long = l.magnitude * 3 + r.magnitude * 2
  }
  case class Literal(id: Int, value: Long) extends Number {
    def magnitude: Long = value
  }
  case object End extends Number {
    val id: Int = -1
    def magnitude: Long = -1L
  }

  def run(input: List[String]): Unit = {
    input.take(3).foreach(println)

    ()
  }

  def parseNumber(raw: Iterator[Char]): Number = {
    val id = new AtomicInteger()
    def parse(raw: Iterator[Char]): Number =
      if (raw.isEmpty) End
      else {
        raw.next() match {
          case tok if tok == '[' => Pair(id.getAndIncrement(), parse(raw), parse(raw))
          case tok if tok == ']' => parse(raw)
          case tok if tok == ',' => parse(raw)
          case value             => Literal(id.getAndIncrement(), value.toString().toLong)
        }
      }

    parse(raw)
  }

  // def reduce(number: Number): Number = explode.andThen(split)(number)

  // If any pair is nested inside four pairs, the leftmost such pair explodes.
  // The pair's left value is added to the first regular number to the left
  // of the exploding pair (if any).
  // The pair's right value is added to the first regular number to the
  // right of the exploding pair (if any).
  // Exploding pairs will always consist of two regular numbers.
  // Then, the entire exploding pair is replaced with the regular number 0.

  // helper(number, ExplodeState()).explodable.getOrElse(number)
  // if pair
  //   inspect left
  //     if literal, save value as left most
  //     if pair, continue left
  //   inspect right
  //     if literal, save value as right most
  //     if pair, continue right
  // }

  sealed trait Direction
  case object L extends Direction
  case object R extends Direction

  def findPathToExplosion(number: Number): Option[Pair] = {
    def search(number: Number, depth: Int): Option[Pair] = {
      number match {
        case pair @ Pair(_, l, r) =>
          (l, r) match {
            case (Literal(_, _), Literal(_, _)) if depth == 4 => Some(pair)
            case _ if depth >= 4                              => None
            case _ => {
              search(l, depth + 1) match {
                case None => search(r, depth + 1)
                case pair => pair
              }
            }
          }
        case _ => None
      }
    }

    search(number, 0)
  }

  // def getLeftLiterals(number: Number): List[Literal] = Nil

  // left most == pre-order
  // pair == in-order
  // right most == post-order

}
