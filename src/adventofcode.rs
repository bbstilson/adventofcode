// https://github.com/bbstilson/advent-of-code-data/blob/main/aocd/src/aocd/Problem.scala

use std::fs::{self, File};
use std::io::{BufRead, BufReader, Write};
use std::path::PathBuf;

use anyhow::{Ok, Result};

use crate::api::Api;

pub trait AdventOfCode {
    fn solve() -> Result<()>;

    fn input_raw(year: u32, day: u32) -> Result<String> {
        let problem_file = cache_input(year, day)?;
        let contents = fs::read_to_string(problem_file)?.parse::<String>()?;
        Ok(contents)
    }

    fn input_lines(year: u32, day: u32) -> Result<Vec<String>> {
        let problem_file = cache_input(year, day)?;
        let file = File::open(problem_file)?;
        let reader = BufReader::new(file);
        let mut lines = vec![];
        for line in reader.lines() {
            lines.push(line?);
        }
        Ok(lines)
    }
}

fn input_dir(year: u32, day: u32) -> Result<PathBuf> {
    let problem_dir = home::home_dir()
        .unwrap()
        .join(format!(".aocd/{year}/{day}"));
    if !problem_dir.exists() {
        std::fs::create_dir_all(problem_dir.clone())?;
    }
    Ok(problem_dir)
}

fn cache_input(year: u32, day: u32) -> Result<PathBuf> {
    let problem_file = input_dir(year, day)?.join("input.txt");

    if !problem_file.exists() {
        let mut file = File::create(problem_file.clone())?;
        let input = Api::get_input(year, day)?;
        write!(file, "{}", input)?;
    }

    Ok(problem_file)
}
