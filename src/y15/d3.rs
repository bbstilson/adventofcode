use std::collections::HashSet;

use anyhow::Result;
use itertools::Itertools;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        Self::part1()?;
        Self::part2()?;

        Ok(())
    }
}

impl Day {
    fn part1() -> Result<()> {
        let input = Day::input_raw(2015, 3)?;

        let (_, seen) = input
            .chars()
            .fold(((0, 0), HashSet::new()), |(pos, mut seen), dir| {
                seen.insert(pos);
                let next_pos = Self::walk(dir, pos);
                (next_pos, seen)
            });
        println!("{}", seen.len());
        Ok(())
    }

    fn part2() -> Result<()> {
        let input = Day::input_raw(2015, 3)?;

        let mut s_pos = (0, 0);
        let mut r_pos = (0, 0);
        let mut seen = HashSet::new();
        for (d1, d2) in input.chars().tuples() {
            seen.insert(s_pos);
            seen.insert(r_pos);
            s_pos = Self::walk(d1, s_pos);
            r_pos = Self::walk(d2, r_pos);
        }
        println!("{}", seen.len());
        Ok(())
    }

    fn walk(dir: char, pos: (i32, i32)) -> (i32, i32) {
        match dir {
            '^' => (pos.0, pos.1 + 1),
            'v' => (pos.0, pos.1 - 1),
            '>' => (pos.0 + 1, pos.1),
            '<' => (pos.0 - 1, pos.1),
            _ => panic!("bad char {dir}"),
        }
    }
}
