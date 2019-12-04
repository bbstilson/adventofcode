lazy val root = project
  .in(file("."))
  .settings(
    name := "advent of code",
    version := "1.0.0",
    scalaVersion := "2.13.1",
    resolvers ++= Seq(
      "Artima Maven Repository" at "https://repo.artima.com/releases"
    ),
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.1.0",
      "org.scalatest" %% "scalatest" % "3.1.0" % Test
    )
  )
