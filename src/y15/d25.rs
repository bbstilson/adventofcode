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
        let target = offset(2981, 3075);
        let start: i64 = 20151125;
        let ans = (1..target).into_iter().fold(start, |acc, _| step(acc));

        println!("{ans}");
        Ok(())
    }

    fn part2() -> Result<()> {
        Ok(())
    }
}

fn step(n: i64) -> i64 {
    n * 252533 % 33554393
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_step() {
        assert_eq!(step(20151125), 31916031);
        assert_eq!(step(31916031), 18749137);
    }

    #[test]
    fn test_offset() {
        for i in 1..=5 {
            println!("({i}, 2) = {}", offset(i, 2));
        }
    }
}

fn offset(row: usize, col: usize) -> usize {
    let mut add = 0;
    let mut out = sum_factorial(col);
    for _ in 0..(row - 1) {
        out += col + add;
        add += 1;
    }
    out
}

fn sum_factorial(n: usize) -> usize {
    (n.pow(2) + n) / 2
}
