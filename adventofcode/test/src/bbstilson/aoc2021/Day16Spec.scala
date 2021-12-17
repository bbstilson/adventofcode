package bbstilson.aoc2021

import utest._

object Day16Spec extends TestSuite {
  import Day16._

  val tests = Tests {
    test("part1") {
      new PacketParser("D2FE28").parse() ==> Some(Literal(6, 2021))
      new PacketParser("38006F45291200")
        .parse() ==> Some(Operator(1, LessThan, List(Literal(6, 10), Literal(2, 20))))
      new PacketParser("EE00D40C823060")
        .parse() ==> Some(Operator(7, Max, List(Literal(2, 1), Literal(4, 2), Literal(1, 3))))
      processPart1("8A004A801A8002F478") ==> Some(16)
      hexStrToBits("620080001611562C8802118E34").toList.size ==> 104
      processPart1("620080001611562C8802118E34") ==> Some(12)
      processPart1("C0015000016115A2E0802F182340") ==> Some(23)
      processPart1("A0016C880162017C3686B18A3D4780") ==> Some(31)
    }

    test("part2") {
      processPart2("C200B40A82") ==> Some(3)
      processPart2("04005AC33890") ==> Some(54)
      processPart2("880086C3E88112") ==> Some(7)
      processPart2("CE00C43D881120") ==> Some(9)
      processPart2("D8005AC2A8F0") ==> Some(1)
      processPart2("F600BC2D8F") ==> Some(0)
      processPart2("9C005AC2F8F0") ==> Some(0)
      processPart2("9C0141080250320F1802104A08") ==> Some(1)
      processPart2("000294200841022044088110220440881102204408811020") ==> Some(2)
      processPart2("3232D42BF9400") ==> Some(5000000000L)
    }

    test("eval") {
      eval(
        Operator(
          4,
          Min,
          List(
            Literal(7, 10),
            Literal(0, 40190236496L),
            Literal(4, 151)
          )
        )
      ) ==> 10
    }

  }
}
