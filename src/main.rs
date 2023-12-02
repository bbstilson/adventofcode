use anyhow::{Ok, Result};

use adventofcode::AdventOfCode;

pub mod adventofcode;
pub mod api;
pub mod y23;
pub mod year2022;

use y23::d2::*;

fn main() -> Result<()> {
    Day2::solve()?;

    Ok(())
}
