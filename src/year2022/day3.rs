use std::collections::HashSet;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day3;

impl AdventOfCode for Day3 {
    fn solve() -> Result<()> {
        let input = Day3::input_lines(2022, 3)?;

        // https://en.wikipedia.org/wiki/ASCII#Printable_characters
        let ascii_to_score = |code: u32| {
            if code > 90 {
                code - 96 // lower case
            } else {
                code - 38 // upper case
            }
        };

        let part1: u32 = input
            .iter()
            .map(|rucksack| {
                let (l, r) = rucksack.split_at(rucksack.len() / 2);
                let l_set: HashSet<char> = l.chars().collect();
                let r_set: HashSet<char> = r.chars().collect();
                l_set
                    .intersection(&r_set)
                    .map(|c| ascii_to_score(u32::from(*c)))
                    .sum::<u32>()
            })
            .sum();

        let part2: u32 = input
            .chunks_exact(3)
            .map(|sacks| {
                sacks
                    .to_vec()
                    .iter()
                    .map(|sack| sack.chars().collect::<HashSet<char>>())
                    .reduce(|l, r| l.intersection(&r).map(|c| *c).collect())
                    .unwrap()
                    .iter()
                    .map(|c| ascii_to_score(u32::from(*c)))
                    .sum::<u32>()
            })
            .sum();

        println!("part 1: {}", part1);
        println!("part 2: {}", part2);

        Ok(())
    }
}
