use anyhow::{Ok, Result};

use adventofcode::AdventOfCode;

pub mod adventofcode;
pub mod api;
pub mod y23;
pub mod year2022;

use y23::d3::*;

fn main() -> Result<()> {
    Day::solve()?;

    Ok(())
}
