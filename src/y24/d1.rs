use std::collections::HashMap;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let (mut ls, mut rs) =
            Day::input_lines(2024, 1)?
                .into_iter()
                .fold((vec![], vec![]), |(mut ls, mut rs), l| {
                    let (a, b) = l.split_once("   ").unwrap();
                    ls.push(a.parse::<i32>().unwrap());
                    rs.push(b.parse::<i32>().unwrap());
                    (ls, rs)
                });

        ls.sort_unstable();
        rs.sort_unstable();

        let part1 = ls
            .iter()
            .zip(rs.iter())
            .map(|(l, r)| (l - r).abs())
            .sum::<i32>();
        println!("{part1}");

        let freq_map = rs.into_iter().fold(HashMap::new(), |mut acc, r| {
            acc.entry(r).and_modify(|c| *c += 1).or_insert(1);
            acc
        });

        let part2 = ls
            .into_iter()
            .map(|l| l * freq_map.get(&l).unwrap_or(&0))
            .sum::<i32>();

        println!("{part2}");
        Ok(())
    }
}
