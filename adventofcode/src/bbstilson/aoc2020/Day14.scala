package bbstilson.aoc2020

object Day14 extends aocd.Problem(2020, 14) {

  val MaskRegex = raw"mask = (.*)".r
  val MemRegex = raw"mem\[(\d+)\] = (\d+)".r
  sealed trait Cmd
  case class Mask(mask: String) extends Cmd
  case class AddressSet(address: Int, value: Int) extends Cmd

  def run(input: List[String]): Unit = {
    val cmds = parseInput(input)
    part1(cmds)
    part2(cmds)
    ()
  }

  def part1(cmds: List[Cmd]): BigInt = part1 {
    val (map, _) = cmds.foldLeft((Map.empty[Int, BigInt], "")) { case ((map, mask), cmd) =>
      cmd match {
        case Mask(nextMask)             => (map, nextMask)
        case AddressSet(address, value) => (map + (address -> merge(mask, value)), mask)
      }
    }
    map.values.sum
  }

  def part2(cmds: List[Cmd]): BigInt = part2 {
    val (map, _) = cmds.foldLeft((Map.empty[BigInt, BigInt], "")) { case ((map, mask), cmd) =>
      cmd match {
        case Mask(nextMask) => (map, nextMask)
        case AddressSet(address, value) =>
          (map ++ decodeMask(mask, address).map(_ -> BigInt(value)).toMap, mask)

      }
    }
    map.values.sum
  }

  def merge(mask: String, value: Int): BigInt = {
    val fixed = mask.reverse
      .zipAll(value.toBinaryString.reverse, '0', '0')
      .map {
        case ('X', bit) => bit
        case (r, _)     => r
      }
      .reverse
      .mkString

    BigInt(fixed, 2)
  }

  def decodeMask(mask: String, value: Int): List[BigInt] = {
    val fixed = mask.reverse
      .zipAll(value.toBinaryString.reverse, '0', '0')
      .map {
        case ('1', _)   => List('1')
        case ('0', bit) => List(bit)
        case ('X', _)   => List('0', '1')
      }

    permute(fixed.toList).map { bits => BigInt(bits.reverse.mkString, 2) }
  }

  def permute(xs: List[List[Char]]): List[List[Char]] = xs match {
    case head :: tail =>
      permute(tail) match {
        case Nil => head.map(List(_))
        case next =>
          for {
            bit <- head
            bits <- next
          } yield bit +: bits
      }
    case Nil => Nil
  }

  def parseInput(input: List[String]): List[Cmd] = {
    input.map {
      case MaskRegex(mask)          => Mask(mask)
      case MemRegex(address, value) => AddressSet(address.toInt, value.toInt)
    }
  }
}
