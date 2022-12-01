// https://github.com/bbstilson/advent-of-code-data/blob/main/aocd/src/aocd/Problem.scala

use std::fs::File;
use std::io::{BufRead, BufReader, Write};

use anyhow::{Ok, Result};

use crate::api::Api;

pub trait AdventOfCode {
    fn solve() -> Result<()>;

    fn input_lines(year: u32, day: u32) -> Result<Vec<String>> {
        let problem_dir = home::home_dir()
            .unwrap()
            .join(format!(".aocd/{year}/{day}"));
        let problem_file = problem_dir.join("input.txt");

        if !problem_dir.exists() {
            std::fs::create_dir_all(problem_dir.clone())?;
        }

        if !problem_file.exists() {
            let mut file = File::create(problem_file.clone())?;
            let input = Api::get_input(year, day)?;
            write!(file, "{}", input)?;
        }

        let file = File::open(problem_file)?;
        let reader = BufReader::new(file);
        let mut lines = vec![];
        for line in reader.lines() {
            lines.push(line?);
        }
        Ok(lines)
    }
}
