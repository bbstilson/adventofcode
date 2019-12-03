# Advent of Code

This repo is for my solutions to the annual [Advent of Code](https://adventofcode.com) event.

## To run

```
sbt
sbt:root> ~runMain org.bbstilson.aoc2019.DayN
```

## Things I Learned

### Advent of Code 2018

- Voronoi Diagram - ([Day 6](./src/main/scala/org/bbstilson/aoc2018/Day6Part1.scala))
- Topological Sort - ([Day 7](./src/main/scala/org/bbstilson/aoc2018/Day7Part1.scala))
- Circular Lists (Zipper) - ([Day 9](./src/main/scala/org/bbstilson/aoc2018/Day9Part1.scala))
  - It looks like Scalaz has a Zipper class, but I didn't want to bring in a library, so I wrote my own to learn more.
- Summed Area Table - ([Day 11](./src/main/scala/org/bbstilson/aoc2018/Day11Part1.scala))

#### Scala

- Type aliasing - ([Day 4](./day4/Part1.scala))
- If you're creating really big lists, append to the head.
  - Since `List`s are `LinkedList`s, it has to traverse the whole list to append to the end. If if can be avoided, just append to the head.
