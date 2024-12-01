use anyhow::{Ok, Result};

use adventofcode::AdventOfCode;

mod adventofcode;
mod api;
mod data;
mod y15;
mod y23;
mod y24;

use y24::Day;

fn main() -> Result<()> {
    Day::solve()?;

    Ok(())
}
