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
        let input = Day::input_raw(2015, 1)?;

        let ans = input.chars().into_iter().fold(0, |floor, dir| match dir {
            '(' => floor + 1,
            ')' => floor - 1,
            _ => panic!("bad char: {dir}"),
        });
        println!("{ans}");
        Ok(())
    }

    fn part2() -> Result<()> {
        let input = Day::input_raw(2015, 1)?;

        let mut floor = 0;
        let mut dir_change = 0;

        for dir in input.chars() {
            match dir {
                '(' => floor += 1,
                ')' => floor -= 1,
                _ => panic!("bad char: {dir}"),
            }
            dir_change += 1;

            if floor == -1 {
                break;
            }
        }

        println!("{dir_change}");
        Ok(())
    }
}
