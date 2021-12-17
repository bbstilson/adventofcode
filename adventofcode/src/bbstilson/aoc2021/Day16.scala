package bbstilson.aoc2021

import scala.annotation.tailrec
import java.math.BigInteger
import scala.util.Try

object Day16 extends aocd.Problem(2021, 16) {

  sealed trait Operation
  case object Sum extends Operation
  case object Product extends Operation
  case object Min extends Operation
  case object Max extends Operation
  case object GreaterThan extends Operation
  case object LessThan extends Operation
  case object EqualTo extends Operation

  sealed trait Packet { val version: Int }
  case class Literal(version: Int, value: Long) extends Packet
  case class Operator(version: Int, op: Operation, packets: List[Packet] = Nil) extends Packet

  def run(input: List[String]): Unit = {

    part1 {
      processPart1(input.head)
    }

    part2 {
      processPart2(input.head)
    }

    ()
  }

  def processPart1(raw: String): Option[Long] = new PacketParser(raw).parse().map(sumVersions)

  def processPart2(raw: String): Option[Long] = new PacketParser(raw).parse().map(eval)

  class PacketParser(hexStr: String) {
    type Bits = Iterator[Char]

    private val _bits: Bits = hexStrToBits(hexStr)

    def parse(): Option[Packet] = parse(_bits)

    def parse(bits: Bits): Option[Packet] = header(bits).map { case (version, id) =>
      id match {
        case 4  => parseLiteral(bits, version)
        case op => parseOperationPacket(bits, version, op)
      }

    }

    def header(bits: Bits): Option[(Int, Int)] =
      Try(parseInt(bits.take(3))).toOption
        .zip(Try(parseInt(bits.take(3))).toOption)

    def parseLiteral(bits: Bits, version: Int): Literal =
      Literal(version, parseLong(parseBits(bits).mkString))

    def parseOperationPacket(bits: Iterator[Char], version: Int, op: Int): Packet = {
      val operation = op match {
        case 0 => Sum
        case 1 => Product
        case 2 => Min
        case 3 => Max
        case 5 => GreaterThan
        case 6 => LessThan
        case 7 => EqualTo
      }

      if (bits.next() == '0') {
        // The next 15 bits are a number that represents the total length
        // in bits of the sub-packets contained by this packet.
        val bitsInSubPacket = parseInt(bits.take(15))
        val subPacketBits = bits.take(bitsInSubPacket)

        val subPackets = List
          .unfold(subPacketBits) { bs =>
            if (bs.nonEmpty) {
              val tmp = bs.toList
              val bss = tmp.iterator
              parse(bss).map(p => p -> bss)
            } else None
          }

        Operator(
          version,
          operation,
          subPackets
        )
      } else {
        // The next 11 bits are a number that represents the number of
        // sub-packets immediately contained by this packet.
        val numSubPackets = parseInt(bits.take(11))
        val subPackets = (0 until numSubPackets).map(_ => parse(bits)).toList.flatten

        Operator(
          version,
          operation,
          subPackets
        )
      }
    }

    @tailrec
    private def parseBits(bits: Bits, ns: List[String] = Nil): List[String] = {
      val curr = bits.take(5)
      val shouldStop = curr.next() == '0'
      val nextNs = curr.take(4).mkString +: ns
      if (shouldStop) nextNs.reverse else parseBits(bits, nextNs)
    }
  }

  def parseInt(bs: Iterator[Char]): Int = Integer.parseInt(bs.mkString, 2)
  def parseLong(str: String): Long = new BigInteger(str, 2).longValue()

  def hexStrToBits(hexStr: String): Iterator[Char] = {
    val sb = new StringBuilder()
    hexStr.foreach { char =>
      val bStr = Integer.toBinaryString(Integer.parseInt(char.toString(), 16))
      sb.append(String.format("%1$4s", bStr).replaceAll(" ", "0"))
    }
    sb.toString().iterator
  }

  def sumVersions(root: Packet): Long = {
    root match {
      case Operator(version, _, packets) => version.toLong + packets.map(sumVersions).sum
      case Literal(version, _)           => version.toLong
    }
  }

  def eval(packet: Packet): Long = packet match {
    case Literal(_, value) => value
    case Operator(_, op, packets) =>
      op match {
        case Sum         => packets.map(eval).sum
        case Product     => packets.map(eval).product
        case Min         => packets.map(eval).min
        case Max         => packets.map(eval).max
        case GreaterThan => if (eval(packets.head) > eval(packets.last)) 1L else 0L
        case LessThan    => if (eval(packets.head) < eval(packets.last)) 1L else 0L
        case EqualTo     => if (eval(packets.head) == eval(packets.last)) 1L else 0L
      }
  }
}
