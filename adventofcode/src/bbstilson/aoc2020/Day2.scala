package bbstilson.aoc2020

import aocd.Problem

case class Rule(min: Int, max: Int, letter: Char, password: String)

object Input {

  val LineRegex = """(\d+)\-(\d+) ([a-z]): ([a-z]+)""".r

  def parseLine(str: String): Rule = str match {
    case LineRegex(min, max, char, pw) => Rule(min.toInt, max.toInt, char.toCharArray().head, pw)
    case _                             => Rule(0, 0, ' ', "")
  }
}

object Day2 extends Problem(2020, 2) {

  def run(in: List[String]): Unit = {
    val input = in.map(Input.parseLine)
    println(input.filter(isValidPart1).size)
    println(input.filter(isValidPart2).size)
  }

  private def isValidPart1(rule: Rule): Boolean = {
    val numChar = rule.password.filter(_ == rule.letter).size
    numChar >= rule.min && numChar <= rule.max
  }

  private def isValidPart2(rule: Rule): Boolean = {
    val atMin = rule.password(rule.min - 1) == rule.letter
    val atMax = rule.password(rule.max - 1) == rule.letter
    (atMin || atMax) && !(atMin && atMax)
  }
}
