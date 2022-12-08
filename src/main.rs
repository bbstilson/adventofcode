#![allow(clippy::all)]

use anyhow::{Ok, Result};

use adventofcode::AdventOfCode;

pub mod adventofcode;
pub mod api;
pub mod year2022;

// use year2022::day1::Day1;
// use year2022::day2::Day2;
// use year2022::day3::Day3;
// use year2022::day4::Day4;
// use year2022::day5::Day5;
// use year2022::day6::Day6;
// use year2022::day7::Day7;
use year2022::day8::Day8;

fn main() -> Result<()> {
    // Day1::solve()?;
    // Day2::solve()?;
    // Day3::solve()?;
    // Day4::solve()?;
    // Day5::solve()?;
    // Day6::solve()?;
    // Day7::solve()?;
    Day8::solve()?;

    Ok(())
}
