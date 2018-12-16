# Advent of Code 2018

This repo is a collection of prompts, inputs, and solutions I produced for the [Advent of Code 2018](https://adventofcode.com/2018/) event.

I had dabbled in learning Haskell in the past, but never made it too far. This holiday season, I decided to try and learn Haskell and complete as many of the advent challenges as I could.

Unfortunately, that was a bit too much, so I've switched to writing solutions in Scala. I might go back an reimplement the solutions in Haskell.

## Things I've Learned

### CS Fundamentals

- Voronoi Diagram - ([Day 6](./day6/Part1.scala))
- Topological Sort - ([Day 7](./day7/Part1.scala))
- Circular Lists (Zipper) - ([Day 9](./day9/Part1.scala))
  - It looks like Scalaz has a Zipper class, but I didn't want to bring in a library, so I wrote my own to learn more.
- Summed Area Table - ([Day 11](./day11/Part1.scala))

### Scala

- Type aliasing - ([Day 4](./day4/Part1.scala))
- If you're creating really big lists, append to the head.
  - Since `List`s are `LinkedList`s, it has to traverse the whole list to append to the end. If if can be avoided, just append to the head.
