use anyhow::{Ok, Result};

use adventofcode::AdventOfCode;

mod adventofcode;
mod api;
mod data;
mod y15;
mod y23;

use y15::Day;

fn main() -> Result<()> {
    Day::solve()?;

    Ok(())
}
