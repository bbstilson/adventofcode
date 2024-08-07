use anyhow::Result;

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
        let _input = "cqjxjnds";
        Ok(())
    }

    fn part2() -> Result<()> {
        Ok(())
    }
}
