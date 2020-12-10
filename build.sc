import $ivy.`io.github.davidgregory084::mill-tpolecat:0.2.0`

import mill._
import mill.scalalib._
import mill.scalalib.scalafmt._
import io.github.davidgregory084.TpolecatModule
import java.time.{ LocalDate, ZoneId }

object adventofcode extends ScalaModule with TpolecatModule with ScalafmtModule {
  def scalaVersion = "2.13.4"

  def ivyDeps = Agg(
    ivy"io.github.bbstilson::aocd:0.1.2"
  )

  def today: Int = LocalDate.now(ZoneId.of("US/Eastern")).getDayOfMonth()

  def runProblem(y: Int = 2020, d: Int = today) = T.command {
    runMain(s"bbstilson.aoc$y.Day$d")
  }

  object test extends Tests {
    def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.7.2")
    def testFrameworks = Seq("utest.runner.Framework")
  }
}
