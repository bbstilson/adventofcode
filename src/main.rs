use adventofcode::AdventOfCode;
use anyhow::{Ok, Result};

pub mod adventofcode;
pub mod api;
pub mod year2022;

use year2022::day1;

fn main() -> Result<()> {
    day1::Day1::solve()?;

    Ok(())
}
