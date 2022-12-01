use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day1;

impl AdventOfCode for Day1 {
    fn solve() -> Result<()> {
        let mut elfs: Vec<u32> = Vec::new();
        let mut elf: Vec<u32> = Vec::new();

        let input = Day1::input_lines(2022, 1)?;
        for line in input {
            match line.parse::<u32>() {
                Ok(cal) => elf.push(cal),
                Err(_) => {
                    let total_cals: u32 = elf.iter().sum();
                    elfs.push(total_cals);
                    elf.clear();
                }
            }
        }

        elfs.sort();
        elfs.reverse();

        let most_cals = elfs.iter().max().unwrap();
        println!("part 1: {}", most_cals);

        let top_elfs: u32 = elfs.iter().take(3).sum();
        println!("part 2: {}", top_elfs);

        Ok(())
    }
}
