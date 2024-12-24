use once_cell::sync::Lazy;
use regex::Regex;

use crate::adventofcode::AdventOfCode;

static MUL_RE: Lazy<Regex> =
    Lazy::new(|| Regex::new(r#"mul\((\d+),(\d+)\)|don't\(\)|do\(\)"#).unwrap());

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let input = Day::input_raw(2024, 3).unwrap();
        let capture_matches = MUL_RE.captures_iter(&input);

        let mut vals = vec![];
        let mut enabled = true;
        for m in capture_matches {
            match m.get(0).unwrap().as_str() {
                "do()" => enabled = true,
                "don't()" => enabled = false,
                _ => {
                    if enabled {
                        let a = m.get(1).unwrap().as_str().parse::<i64>().unwrap();
                        let b = m.get(2).unwrap().as_str().parse::<i64>().unwrap();
                        vals.push(a * b)
                    }
                }
            }
        }
        println!("{}", vals.into_iter().sum::<i64>());
        Ok(())
    }
}
