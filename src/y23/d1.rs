use std::collections::HashMap;

use anyhow::Result;
use fancy_regex::Regex;
use once_cell::sync::Lazy;

use crate::adventofcode::AdventOfCode;

static NUMBER_REGEX: Lazy<Regex> =
    Lazy::new(|| Regex::new(r"(?=(\d|one|two|three|four|five|six|seven|eight|nine))").unwrap());

static STR_TO_NUM: Lazy<HashMap<&str, &str>> = Lazy::new(|| {
    HashMap::from([
        ("one", "1"),
        ("two", "2"),
        ("three", "3"),
        ("four", "4"),
        ("five", "5"),
        ("six", "6"),
        ("seven", "7"),
        ("eight", "8"),
        ("nine", "9"),
    ])
});

pub struct Day1;

impl AdventOfCode for Day1 {
    fn solve() -> Result<()> {
        let input = Day1::input_lines(2023, 1)?;

        let mut sum = 0;
        for line in input {
            let caps = NUMBER_REGEX.captures_iter(&line).collect::<Vec<_>>();
            let f = caps
                .first()
                .unwrap()
                .as_ref()
                .unwrap()
                .get(1)
                .unwrap()
                .as_str();
            let l = caps
                .last()
                .unwrap()
                .as_ref()
                .unwrap()
                .get(1)
                .unwrap()
                .as_str();

            let f = str_to_num(f);
            let l = str_to_num(l);

            let as_n: String = vec![f, l].into_iter().collect();
            sum += as_n.parse::<usize>()?;
        }

        println!("{sum}");

        Ok(())
    }
}

fn str_to_num(s: &str) -> String {
    match s.parse::<usize>() {
        Ok(_) => s.into(),
        Err(_) => STR_TO_NUM[s].into(),
    }
}
