use core::panic;
use std::collections::HashMap;

use anyhow::Result;
use itertools::Itertools;
use once_cell::sync::Lazy;
use regex::Regex;

use crate::adventofcode::AdventOfCode;

static LINE: Lazy<Regex> =
    Lazy::new(|| Regex::new(r"([A-Za-z]+) would (gain|lose) (\d+) .+ ([A-Za-z]+)\.").unwrap());
pub struct Day;

type Pairs = HashMap<String, HashMap<String, i64>>;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let mut init: Pairs = HashMap::new();
        let pairs = Self::input_lines(2015, 13)?
            .into_iter()
            .map(|line| {
                let caps = LINE.captures(&line).unwrap();
                let a = caps.get(1).unwrap().as_str().to_string();
                let direction = caps.get(2).unwrap().as_str();
                let amount = caps.get(3).unwrap().as_str().parse::<i64>().unwrap();
                let b = caps.get(4).unwrap().as_str().to_string();

                let amount = match direction {
                    "gain" => amount,
                    "lose" => -amount,
                    d => panic!("direction: {d}"),
                };

                (a, b, amount)
            })
            .fold(&mut init, |acc, (a, b, amount)| {
                acc.entry(a)
                    .and_modify(|stats: &mut HashMap<String, i64>| {
                        stats.insert(b.clone(), amount);
                    })
                    .or_insert(HashMap::from([(b, amount)]));
                acc
            });

        Self::part1(&pairs)?;

        let mut pairs = pairs
            .into_iter()
            .map(|(a, b)| {
                b.insert("Me".to_string(), 0);
                (a.to_owned(), b.to_owned())
            })
            .collect::<Pairs>();

        let people = pairs.keys().clone().map(|a| (a.to_owned(), 0)).collect();

        pairs.insert("Me".to_string(), people);

        Self::part1(&pairs)?;

        Ok(())
    }
}

impl Day {
    fn part1(pairs: &Pairs) -> Result<()> {
        let num_seats = pairs.len();
        let max_idx = num_seats - 1;

        let best = pairs
            .keys()
            .permutations(pairs.len())
            .map(|seating| {
                seating.iter().enumerate().fold(0, |score, (idx, person)| {
                    let l = match idx {
                        0 => num_seats - 1,
                        i => i - 1,
                    };
                    let r = match idx {
                        i if i == max_idx => 0,
                        i => i + 1,
                    };
                    let scores = &pairs[*person];
                    score + scores[seating[l]] + scores[seating[r]]
                })
            })
            .max()
            .unwrap();

        println!("{best}");
        Ok(())
    }
}
