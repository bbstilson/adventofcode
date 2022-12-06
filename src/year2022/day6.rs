use std::{collections::HashSet, fmt::Debug};

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day6;

impl AdventOfCode for Day6 {
    fn solve() -> Result<()> {
        let input = Day6::input_lines(2022, 6)?;
        let signal = input.first().unwrap().chars().collect::<Vec<char>>();

        let all_chars_different =
            |cs: &&[char]| cs.to_vec().into_iter().collect::<HashSet<char>>().len() == cs.len();

        let (part_1, _) = signal
            .windows(4)
            .enumerate()
            .find(|(idx, sequence)| all_chars_different(sequence))
            .unwrap();

        let (part_2, _) = signal
            .windows(14)
            .enumerate()
            .find(|(idx, sequence)| all_chars_different(sequence))
            .unwrap();

        println!("{:?}", part_1 + 4);
        println!("{:?}", part_2 + 14);

        Ok(())
    }
}
