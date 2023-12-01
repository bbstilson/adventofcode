use std::collections::HashSet;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

#[derive(Debug, Clone, Copy)]
enum Dir {
    Up,
    Down,
    Left,
    Right,
}

#[derive(PartialEq, Eq, Debug)]
enum Pos {
    Diag,
    Ortho,
}

#[derive(Debug)]
enum Quad {
    UR,
    DR,
    DL,
    UL,
}

#[derive(Debug, Clone, PartialEq, Eq, Hash)]
struct Coord {
    pub x: i32,
    pub y: i32,
}

impl Coord {
    pub fn new() -> Coord {
        Coord { x: 0, y: 0 }
    }

    pub fn add(&self, other: (i32, i32)) -> Coord {
        Coord {
            x: self.x + other.0,
            y: self.y + other.1,
        }
    }

    pub fn should_move(&self, other: &Coord) -> Option<(Pos, Quad)> {
        let distance = (((other.x - self.x).pow(2) + (other.y - self.y).pow(2)) as f32).sqrt();

        let quad = if other.x > self.x && other.y > self.y {
            Quad::UR
        } else if other.x > self.x && other.y < self.y {
            Quad::DR
        } else if other.x < self.x && other.y < self.y {
            Quad::DL
        } else {
            Quad::UL
        };

        if distance == 2.0 {
            Some((Pos::Ortho, quad))
        } else if distance > 2.0 {
            Some((Pos::Diag, quad))
        } else {
            None
        }
    }
}

#[derive(Debug)]
struct Rope {
    knots: Vec<Coord>,
    seen: HashSet<Coord>,
}

impl Rope {
    pub fn size(n: usize) -> Rope {
        Rope {
            knots: std::iter::repeat(Coord::new()).take(n).collect(),
            seen: HashSet::from([Coord::new()]),
        }
    }

    fn step(&mut self, direction: Dir) {
        let first = self.knots.first().unwrap().clone();
        let first = match direction {
            Dir::Up => first.add((0, 1)),
            Dir::Down => first.add((0, -1)),
            Dir::Left => first.add((-1, 0)),
            Dir::Right => first.add((1, 0)),
        };
        let next = self
            .knots
            .iter()
            .skip(1)
            .enumerate()
            .scan(first.clone(), |prev, (idx, curr)| {
                let next = if let Some((pos, quad)) = curr.should_move(prev) {
                    match pos {
                        Pos::Ortho => match direction {
                            Dir::Up => curr.add((0, 1)),
                            Dir::Down => curr.add((0, -1)),
                            Dir::Left => curr.add((-1, 0)),
                            Dir::Right => curr.add((1, 0)),
                        },
                        Pos::Diag => match quad {
                            Quad::UR => curr.add((1, 1)),
                            Quad::DR => curr.add((1, -1)),
                            Quad::DL => curr.add((-1, -1)),
                            Quad::UL => curr.add((-1, 1)),
                        },
                    }
                } else {
                    curr.clone()
                };

                if idx == self.knots.len() - 2 {
                    self.seen.insert(next.clone());
                }

                Some(next)
            })
            .map(|c| c.clone());

        self.knots = std::iter::once(first).chain(next).collect::<Vec<_>>();
    }
}

pub struct Day9;

impl AdventOfCode for Day9 {
    fn solve() -> Result<()> {
        let input = Day9::input_lines(2022, 9)?;
        // let input = vec!["R 4", "U 4", "L 3", "D 1", "R 4", "D 1", "L 5", "R 2"];
        // let input = vec!["R 5", "U 8", "L 8", "D 3", "R 17", "D 10", "L 25", "U 20"];

        let steps = input
            .iter()
            .map(|line| {
                let split = line.split_whitespace().collect::<Vec<_>>();
                let d = match split[0] {
                    "U" => Dir::Up,
                    "D" => Dir::Down,
                    "L" => Dir::Left,
                    "R" => Dir::Right,
                    _ => unimplemented!(),
                };
                let s = split[1].parse::<u32>().expect("ah");
                (d, s)
            })
            .collect::<Vec<_>>();

        println!("part 1: {}", part_1(steps.clone())); // 5981
        println!("part 2: {}", part_2(steps));

        Ok(())
    }
}

fn part_1(steps: Vec<(Dir, u32)>) -> usize {
    let mut rope = Rope::size(2);

    for (dir, s) in steps {
        for _ in 0..s {
            rope.step(dir);
        }
    }

    rope.seen.len()
}

fn part_2(steps: Vec<(Dir, u32)>) -> usize {
    let mut rope = Rope::size(10);

    for (dir, s) in steps {
        for _ in 0..s {
            rope.step(dir);
        }
    }

    rope.seen.len()
}

// mod test {
//     use super::*;

//     #[test]
//     fn test_moves() {
//         let mut rope = Rope::size(2);

//         rope.step(Dir::Up);
//         rope.step(Dir::Right);
//         rope.step(Dir::Up);

//         assert_eq!(rope.knots[0], Coord { x: 1, y: 2 });
//         assert_eq!(rope.knots[1], Coord { x: 1, y: 1 });

//         let mut rope = Rope::size(2);

//         rope.step(Dir::Up);
//         rope.step(Dir::Right);
//         rope.step(Dir::Right);

//         assert_eq!(rope.knots[0], Coord { x: 2, y: 1 });
//         assert_eq!(rope.knots[1], Coord { x: 1, y: 1 });
//     }
// }
