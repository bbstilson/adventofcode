use anyhow::{Ok, Result};

use adventofcode::AdventOfCode;

pub mod adventofcode;
pub mod api;
pub mod year2022;

use year2022::day1::Day1;
use year2022::day2::Day2;

fn main() -> Result<()> {
    // Day1::solve()?;
    Day2::solve()?;

    Ok(())
}
