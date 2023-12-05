use anyhow::Result;
use lazy_static::lazy_static;
use regex::Regex;

use crate::adventofcode::AdventOfCode;

lazy_static! {
    static ref LINE_REGEX: Regex =
        Regex::new(r"(?P<a>\d+)\-(?P<b>\d+),(?P<c>\d+)\-(?P<d>\d+)").unwrap();
}

pub struct Day4;

impl AdventOfCode for Day4 {
    fn solve() -> Result<()> {
        let input = Day4::input_lines(2022, 4)?;

        let parse_line = |line: &String| {
            let captures = LINE_REGEX.captures(line).unwrap();
            let a = captures.name("a").unwrap().as_str().parse::<i32>().unwrap();
            let b = captures.name("b").unwrap().as_str().parse::<i32>().unwrap();
            let c = captures.name("c").unwrap().as_str().parse::<i32>().unwrap();
            let d = captures.name("d").unwrap().as_str().parse::<i32>().unwrap();
            (a, b, c, d)
        };

        let fully_contains =
            |(a, b, c, d): (i32, i32, i32, i32)| (a <= c && b >= d) || (c <= a && d >= b);

        let no_overlap = |(a, b, c, d): (i32, i32, i32, i32)| b < c || d < a;

        let part1 = input
            .iter()
            .filter(|line| fully_contains(parse_line(line)))
            .count();

        let part2 = input
            .iter()
            .filter(|line| !no_overlap(parse_line(line)))
            .count();

        println!("part 1: {:?}", part1);
        println!("part 2: {:?}", part2);

        Ok(())
    }
}
