use anyhow::{Ok, Result};

use adventofcode::AdventOfCode;

pub mod adventofcode;
pub mod api;
pub mod y23;

use y23::d4::*;

fn main() -> Result<()> {
    Day::solve()?;

    Ok(())
}
