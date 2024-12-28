use std::{collections::HashSet, fmt::Debug};

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let input = Day::input_raw(2024, 6).unwrap();
        let mut guard_map = GuardMap::from_input(&input);
        let ans = guard_map.march();
        println!("{ans}");
        Ok(())
    }
}

#[derive(Debug, Clone, Copy)]
enum Dir {
    U,
    R,
    D,
    L,
}

impl Dir {
    pub fn rotate_90(self) -> Self {
        match self {
            Self::U => Self::R,
            Self::R => Self::D,
            Self::D => Self::L,
            Self::L => Self::U,
        }
    }
}

type Coord = (usize, usize);

struct GuardMap {
    map: Vec<bool>,
    visited: HashSet<Coord>,
    dir: Dir,
    pos: Coord,
    width: usize,
    height: usize,
}

impl GuardMap {
    pub fn from_input(input: &str) -> Self {
        let input = input
            .lines()
            .map(|l| l.chars().collect::<Vec<_>>())
            .collect::<Vec<_>>();

        let height = input.len();
        let width = input[0].len();

        let mut pos = None;
        for (y, row) in input.iter().enumerate() {
            for (x, c) in row.iter().enumerate() {
                if *c == '^' {
                    pos = Some((x, y));
                }
            }
        }
        let pos = pos.unwrap();
        let map: Vec<bool> = input
            .into_iter()
            .flat_map(|r| r.into_iter().map(|c| matches!(c, '#')))
            .collect();

        Self {
            map,
            visited: HashSet::new(),
            pos,
            dir: Dir::U,
            width,
            height,
        }
    }

    pub fn march(&mut self) -> usize {
        self.visited.insert(self.pos);
        while let Some(next_pos) = self.get_next_pos() {
            // println!("{self:?}");
            let (nx, ny) = next_pos;
            let hit_wall = self.map[(ny * self.width) + nx];

            if hit_wall {
                // rotate, try again
                self.dir = self.dir.rotate_90();
            } else {
                self.pos = next_pos;
                self.visited.insert(self.pos);
            }
        }

        self.visited.len()
    }

    fn get_next_pos(&self) -> Option<Coord> {
        let (cx, cy) = self.pos;

        let up = cy.checked_add_signed(-1);
        let right = Some(cx + 1);
        let down = Some(cy + 1);
        let left = cx.checked_add_signed(-1);

        let (nx, ny) = match self.dir {
            Dir::U => (Some(cx), up),
            Dir::R => (right, Some(cy)),
            Dir::D => (Some(cx), down),
            Dir::L => (left, Some(cy)),
        };

        nx.and_then(|x| ny.map(|y| (x, y)))
            .filter(|c| self.is_valid_pos(*c))
    }

    fn is_valid_pos(&self, pos: Coord) -> bool {
        let (x, y) = pos;
        x < self.width && y < self.height
    }
}

impl Debug for GuardMap {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        for i in 0..self.height {
            let line = &self.map[(i * self.width)..((i + 1) * self.width)];
            for (j, b) in line.iter().enumerate() {
                if (j, i) == self.pos {
                    let guard = match self.dir {
                        Dir::U => '^',
                        Dir::D => 'v',
                        Dir::L => '<',
                        Dir::R => '>',
                    };

                    write!(f, "{guard}").unwrap();
                } else if self.visited.contains(&(j, i)) {
                    write!(f, "X").unwrap();
                } else if *b {
                    write!(f, "#").unwrap();
                } else {
                    write!(f, ".").unwrap();
                }
            }
            writeln!(f).unwrap();
        }
        writeln!(f, "~~~~")
    }
}

#[test]
fn test_guard_map() {
    let input = "....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...";
    let mut gm = GuardMap::from_input(input);
    let ans = gm.march();
    println!("{ans}");
}
