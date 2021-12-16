# Advent of Code

This repo is for my solutions to the annual [Advent of Code](https://adventofcode.com) event.

## To run

```bash
mill adventofcode.runProblem -d 1
mill adventofcode.runProblem -y 2018 -d 1
```

## Things I Learned

- Voronoi Diagram - ([2018 - Day 6](./adventofcode/src/bbstilson/aoc2018/Day6Part1.scala))
- Topological Sort - ([2018 - Day 7](./adventofcode/src/bbstilson/aoc2018/Day7.scala))
- Summed Area Table - ([2018 - Day 11](./adventofcode/src/bbstilson/aoc2018/Day11Part1.scala))
- Dijkstra's Algorithm - ([2021 - Day 15](./adventofcode/src/bbstilson/aoc2021/Day15.scala))

### Scala

- If you're creating really big lists, append to the head.
  - Since `List`s are `LinkedList`s, it has to traverse the whole list to append to the end.
