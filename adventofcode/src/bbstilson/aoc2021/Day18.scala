package bbstilson.aoc2021

import java.util.concurrent.atomic.AtomicInteger
import scala.annotation.tailrec

object Day18 extends aocd.Problem(2021, 18) {

  sealed trait Token {
    val id: Int
    def magnitude: Long
    def show: String
  }
  case class PairToken(id: Int, l: Token, r: Token) extends Token {
    def show: String = s"[${l.show},${r.show}]"
    def magnitude: Long = l.magnitude * 3 + r.magnitude * 2
  }
  case class LiteralToken(id: Int, value: Long) extends Token {
    def show: String = value.toString()
    def magnitude: Long = value
  }
  case object EndToken extends Token {
    def show: String = ""
    def magnitude: Long = ???
    val id: Int = -1
  }

  sealed trait Child { def +(o: Child): Child }
  case class Pair(left: Int, right: Int) extends Child {
    def +(o: Child): Child = ???
  }
  case class Value(value: Long) extends Child {
    def +(other: Child): Child = other match {
      case Pair(_, _)   => throw new Exception("Cannot add Pairs")
      case Value(other) => Value(value + other)
    }
  }

  def run(input: List[String]): Unit = {
    val tokens = input.map(parse)

    part1 {
      tokens.tail
        .foldLeft(tokens.head) { case (t1, t2) =>
          reduce(add(t1, t2))
        }
        .magnitude
    }

    part2 {
      val magnitudes = for {
        t1 <- tokens
        t2 <- tokens
      } yield reduce(add(t1, t2)).magnitude
      magnitudes.max
    }

    ()
  }

  def add(t1: Token, t2: Token): Token = fixIds(PairToken(9999, t1, t2))

  @tailrec
  def reduce(token: Token): Token = {
    val stepped = step(token)
    if (stepped == token) token else reduce(stepped)
  }

  def step(token: Token): Token = {
    val exploded = explode(token)
    val splitted = split(token)

    val nextToken = if (exploded != token) {
      exploded
    } else if (splitted != token) {
      splitted
    } else {
      token
    }

    fixIds(nextToken)
  }

  def fixIds(token: Token): Token = {
    val id = new AtomicInteger()
    def helper(t: Token): Token = t match {
      case PairToken(_, l, r)     => PairToken(id.getAndIncrement(), helper(l), helper(r))
      case LiteralToken(_, value) => LiteralToken(id.getAndIncrement(), value)
      case EndToken               => EndToken
    }

    helper(token)
  }

  def explode(token: Token): Token = {
    val map = tokenToMap(token)
    val optPairToExplode = findFirstPairIdToExplode(token)
    val pairIds = optPairToExplode
      .map(map)
      .collect { case Pair(l, r) => Some(List(l, r)) }
      .getOrElse(None)

    pairIds match {
      case None => token
      case Some(ps) => {
        val literals = getInOrderLiterals(token).filterNot(ps.contains)
        val leftMost = literals.takeWhile(_ < ps.head).lastOption
        val rightMost = literals.dropWhile(_ < ps.last).headOption
        val map1 = leftMost.foldLeft(map) { case (map, id) =>
          map + (id -> (map(id) + map(ps.head)))
        }
        val map2 = rightMost.foldLeft(map1) { case (map, id) =>
          map + (id -> (map(id) + map(ps.last)))
        }
        val map3 = optPairToExplode.foldLeft(map2) { case (map, id) =>
          map + (id -> Value(0))
        }
        mapToToken(map3)
      }
    }
  }

  def split(token: Token): Token = {
    val map = tokenToMap(token)
    val map1 = findFirstLiteralToSplit(token) match {
      case Some(id) => {
        val value = map(id) match {
          case Pair(_, _)   => throw new Exception("Cannot split a pair")
          case Value(value) => value.toDouble
        }
        map ++ Map(
          id -> Pair(9999, 99999),
          9999 -> Value(Math.floor(value / 2).toLong),
          99999 -> Value(Math.ceil(value / 2).toLong)
        )
      }
      case None => map
    }

    mapToToken(map1)
  }

  def parse(raw: String): Token = {
    val id = new AtomicInteger()
    def helper(raw: Iterator[Char]): Token =
      if (raw.isEmpty) EndToken
      else {
        raw.next() match {
          case tok if tok == '[' => PairToken(id.getAndIncrement(), helper(raw), helper(raw))
          case tok if tok == ']' => helper(raw)
          case tok if tok == ',' => helper(raw)
          case value             => LiteralToken(id.getAndIncrement(), value.toString().toLong)
        }
      }

    helper(raw.iterator)
  }

  def getInOrderLiterals(token: Token): List[Int] = {
    def traverse(node: Token, carry: List[Int]): List[Int] = {
      node match {
        case PairToken(_, l, r)  => traverse(l, carry) ++ traverse(r, carry)
        case LiteralToken(id, _) => id +: carry
        case EndToken            => Nil
      }
    }

    traverse(token, Nil)
  }

  def tokenToMap(token: Token): Map[Int, Child] = token match {
    case PairToken(id, l, r)     => Map(id -> Pair(l.id, r.id)) ++ tokenToMap(l) ++ tokenToMap(r)
    case LiteralToken(id, value) => Map(id -> Value(value))
    case EndToken                => Map.empty
  }

  def mapToToken(map: Map[Int, Child]): Token = {
    def builder(id: Int): Token = {
      map.get(id) match {
        case Some(child) =>
          child match {
            case Pair(leftId, rightId) => PairToken(id, builder(leftId), builder(rightId))
            case Value(value)          => LiteralToken(id, value)
          }
        case None => EndToken
      }
    }

    builder(0)
  }

  def findFirstPairIdToExplode(number: Token): Option[Int] = {
    def search(number: Token, depth: Int): Option[Int] = {
      number match {
        case pair @ PairToken(_, l, r) =>
          (l, r) match {
            case (LiteralToken(_, _), LiteralToken(_, _)) if depth == 4 => Some(pair.id)
            case _ if depth >= 4                                        => None
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

  def findFirstLiteralToSplit(number: Token): Option[Int] = {
    number match {
      case PairToken(_, l, r) =>
        findFirstLiteralToSplit(l) match {
          case None    => findFirstLiteralToSplit(r)
          case literal => literal
        }
      case LiteralToken(id, value) if value >= 10 => Some(id)
      case _                                      => None
    }
  }

}
