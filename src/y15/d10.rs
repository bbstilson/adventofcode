use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let part_1 = Self::part1();
        println!("{}", part_1.len());
        Self::part2(part_1)?;

        Ok(())
    }
}

impl Day {
    fn part1() -> Vec<char> {
        let mut input = vec!['1', '1', '1', '3', '1', '2', '2', '1', '1', '3'];
        for _ in 0..40 {
            input = step(input);
        }
        input
    }

    fn part2(input: Vec<char>) -> Result<()> {
        let mut input = input;
        for _ in 0..10 {
            input = step(input);
        }

        println!("{}", input.len());

        Ok(())
    }
}

fn step(input: Vec<char>) -> Vec<char> {
    let mut out = vec![];
    let mut count = 0;
    let mut last_char = *input.first().unwrap();
    for c in input {
        if c != last_char {
            out.push(char::from_digit(count, 10).unwrap());
            out.push(last_char);
            count = 0;
            last_char = c;
        }
        count += 1;
    }
    out.push(char::from_digit(count, 10).unwrap());
    out.push(last_char);
    out
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_step() {
        let tests = vec![
            vec!['1', '1'],
            vec!['2', '1'],
            vec!['1', '2', '1', '1'],
            vec!['1', '1', '1', '2', '2', '1'],
            vec!['3', '1', '2', '2', '1', '1'],
            vec!['1', '3', '1', '1', '2', '2', '2', '1'],
        ];

        let mut input = vec!['1'];

        for expected in tests {
            assert_eq!(step(input), expected);
            input = expected;
        }
    }
}
