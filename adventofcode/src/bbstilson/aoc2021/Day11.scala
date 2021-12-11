package bbstilson.aoc2021

object Day11 extends aocd.Problem(2021, 11) {
  type GridWithIndex = Map[(Int, Int), Int]

  def run(input: List[String]): Unit = {
    val energies: GridWithIndex = input.zipWithIndex.foldLeft(Map.empty[(Int, Int), Int]) {
      case (grid, (line, row)) =>
        line.split("").zipWithIndex.foldLeft(grid) { case (g, (value, col)) =>
          g + ((row, col) -> value.toInt)
        }
    }

    part1 {
      def step(energies: GridWithIndex, generation: Int, until: Int, flashes: Int): Int =
        if (generation == until) flashes
        else {
          val stepped = energies.view.mapValues(_ + 1).toMap
          val (next, flashedCount) = flash(stepped)

          step(next, generation + 1, until, flashedCount + flashes)
        }

      step(energies, 0, 100, 0)
    }

    part2 {
      def isSynced(energies: GridWithIndex): Boolean =
        energies.forall { case (_, value) => value == 0 }

      def step(energies: GridWithIndex, generation: Int): Int =
        if (isSynced(energies)) generation
        else {
          val stepped = energies.view.mapValues(_ + 1).toMap
          val (next, _) = flash(stepped)
          step(next, generation + 1)
        }

      step(energies, 0)
    }

    ()
  }

  def getNeighbors(row: Int, col: Int): List[(Int, Int)] = List(
    (row - 1, col), // up
    (row - 1, col + 1), // up right
    (row - 1, col - 1), // up left
    (row + 1, col), // down
    (row + 1, col + 1), // down right
    (row + 1, col - 1), // down left
    (row, col + 1), // right
    (row, col - 1) // left
  ).filter { case ((r, c)) => r < 10 && r >= 0 && c < 10 && c >= 0 }

  def flash(energies: GridWithIndex): (GridWithIndex, Int) = {

    def helper(
      energies: GridWithIndex,
      toFlash: List[(Int, Int)],
      flashed: Set[(Int, Int)]
    ): (GridWithIndex, Int) = {
      toFlash match {
        case (r, c) :: rest => {
          val neighbors = getNeighbors(r, c).filterNot(flashed)
          val (nextEnergies, nextToFlash) = getToFlash(neighbors.foldLeft(energies) {
            case (grid, np) =>
              grid + (np -> (grid(np) + 1))
          })

          helper(nextEnergies, nextToFlash ++ rest, flashed ++ nextToFlash)
        }
        case Nil => energies -> flashed.size
      }
    }

    val (reset, toFlash) = getToFlash(energies)

    helper(reset, toFlash, toFlash.toSet)
  }

  def getToFlash(energies: GridWithIndex): (GridWithIndex, List[(Int, Int)]) = energies
    .foldLeft((energies, List.empty[(Int, Int)])) { case ((grid, toFlash), (p, energy)) =>
      if (energy == 10) {
        (grid + (p -> 0), p +: toFlash)
      } else {
        (grid, toFlash)
      }
    }
}
