use anyhow::Result;
use lazy_static::lazy_static;
use regex::Regex;

use crate::adventofcode::AdventOfCode;

lazy_static! {
    static ref LINE_REGEX: Regex = Regex::new(r"(?P<l>[ABC]) (?P<r>[XYZ])").unwrap();
}

pub struct Day2;

impl AdventOfCode for Day2 {
    fn solve() -> Result<()> {
        let input = Day2::input_lines(2022, 2)?;

        let part1 = input
            .iter()
            .map(|line| {
                let captures = LINE_REGEX.captures(line).unwrap();
                let l = captures.name("l").unwrap().as_str();
                let r = captures.name("r").unwrap().as_str();

                // A for Rock
                // B for Paper
                // C for Scissors
                match (l, r) {
                    ("A", "X") => 4, // 1 + draw
                    ("A", "Y") => 8, // 2 + win
                    ("A", "Z") => 3, // 3 + lose
                    ("B", "X") => 1, // 1 + lose
                    ("B", "Y") => 5, // 2 + draw
                    ("B", "Z") => 9, // 3 + win
                    ("C", "X") => 7, // 1 + win
                    ("C", "Y") => 2, // 2 + lose
                    ("C", "Z") => 6, // 3 + draw
                    _ => unimplemented!(),
                }
            })
            .sum::<i32>();

        let part2 = input
            .iter()
            .map(|line| {
                let captures = LINE_REGEX.captures(line).unwrap();
                let l = captures.name("l").unwrap().as_str();
                let r = captures.name("r").unwrap().as_str();

                // X means you need to lose
                // Y means you need to end the round in a draw
                // Z means you need to win.

                match (l, r) {
                    ("A", "X") => 3, // scissors + lose
                    ("A", "Y") => 4, // rock + draw
                    ("A", "Z") => 8, // paper + win
                    ("B", "X") => 1, // rock + lose
                    ("B", "Y") => 5, // paper + draw
                    ("B", "Z") => 9, // scissors + win
                    ("C", "X") => 2, // paper + lose
                    ("C", "Y") => 6, // scissors + draw
                    ("C", "Z") => 7, // rock + win
                    _ => unimplemented!(),
                }
            })
            .sum::<i32>();

        println!("{:?}", part1);
        println!("{:?}", part2);

        Ok(())
    }
}
