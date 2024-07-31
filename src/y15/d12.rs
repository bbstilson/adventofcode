use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        Self::part1()?;
        Self::part2()?;

        Ok(())
    }
}

impl Day {
    fn part1() -> Result<()> {
        let input = Self::input_raw(2015, 12)?;
        let input = serde_json::from_str::<serde_json::Value>(&input)?;

        println!("{}", sum_json_1(&input));
        Ok(())
    }

    fn part2() -> Result<()> {
        let input = Self::input_raw(2015, 12)?;
        let input = serde_json::from_str::<serde_json::Value>(&input)?;

        println!("{}", sum_json_2(&input));
        Ok(())
    }
}

fn sum_json_1(input: &serde_json::Value) -> i64 {
    match input {
        serde_json::Value::Array(xs) => xs.iter().map(sum_json_1).sum(),
        serde_json::Value::Number(n) => n.as_i64().unwrap(),
        serde_json::Value::Object(o) => o.values().map(sum_json_1).sum(),
        _ => 0,
    }
}

fn sum_json_2(input: &serde_json::Value) -> i64 {
    match input {
        serde_json::Value::Array(xs) => xs.iter().map(sum_json_2).sum(),
        serde_json::Value::Number(n) => n.as_i64().unwrap(),
        serde_json::Value::Object(o) if !has_red(o) => o.values().map(sum_json_2).sum(),
        _ => 0,
    }
}

fn has_red(o: &serde_json::Map<String, serde_json::Value>) -> bool {
    o.values().any(|v| match v {
        serde_json::Value::String(s) => s == "red",
        _ => false,
    })
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_step() {}
}
