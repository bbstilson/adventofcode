use anyhow::{Ok, Result};

use adventofcode::AdventOfCode;

mod adventofcode;
mod api;
mod data;
mod y23;

use y23::Day;

fn main() -> Result<()> {
    Day::solve()?;

    Ok(())
}
