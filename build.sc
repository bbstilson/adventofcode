import $ivy.`io.github.davidgregory084::mill-tpolecat:0.1.3`

import mill._
import mill.scalalib._
import mill.scalalib.scalafmt._
import io.github.davidgregory084.TpolecatModule

object adventofcode extends ScalaModule with TpolecatModule with ScalafmtModule {
  def scalaVersion = "2.13.4"

  object test extends Tests {
    def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.7.2")
    def testFrameworks = Seq("utest.runner.Framework")
  }
}
