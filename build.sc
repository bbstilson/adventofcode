import $ivy.`io.github.davidgregory084::mill-tpolecat:0.2.0`

import mill._
import mill.scalalib._
import mill.scalalib.scalafmt._
import io.github.davidgregory084.TpolecatModule
import java.time.{ LocalDate, ZoneId }

object adventofcode extends ScalaModule with TpolecatModule with ScalafmtModule {
  def scalaVersion = "2.13.7"

  def ivyDeps = Agg(
    ivy"io.github.bbstilson::aocd:0.1.3"
  )

  def today: LocalDate = LocalDate.now(ZoneId.of("US/Eastern"))
  def thisYear: Int = today.getYear()
  def thisDay: Int = today.getDayOfMonth()

  def runProblem(y: Int = thisYear, d: Int = thisDay) = T.command {
    runMain(s"bbstilson.aoc$y.Day$d")
  }

  object test extends Tests {
    def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.7.2")
    def testFrameworks = Seq("utest.runner.Framework")
  }
}
