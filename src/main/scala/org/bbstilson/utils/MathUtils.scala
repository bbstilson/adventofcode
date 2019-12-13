package org.bbstilson.aoc2019.utils

import scala.annotation.tailrec

object MathUtils {
  @tailrec
  def gcd(a: Int, b: Int): Int = if (b == 0) a.abs else gcd(b, a % b)

  def lcm(a: Int, b: Int): Int = (math.abs(a.toLong * b) / gcd(a, b)).toInt
}
