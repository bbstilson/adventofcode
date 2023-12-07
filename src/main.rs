use anyhow::{Ok, Result};

use adventofcode::AdventOfCode;

pub mod adventofcode;
pub mod api;
pub mod y23;

use y23::Day;

fn main() -> Result<()> {
    Day::solve()?;

    Ok(())
}
