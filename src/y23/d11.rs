use std::collections::HashSet;

use anyhow::Result;
use itertools::Itertools;

use crate::{adventofcode::AdventOfCode, data::coord::Coord};

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let input = Day::input_raw(2023, 11)?;
        let map = parse_input(&input);

        println!("{}", part_1(&map));

        Ok(())
    }
}

fn part_1(map: &StarMap) -> i64 {
    map.stars
        .iter()
        .combinations(2)
        .map(|pair| get_distance(pair, map))
        .sum()
}

fn get_distance(pair: Vec<&Coord>, map: &StarMap) -> i64 {
    // add up the y distance, then add in number of gaps in that range
    // add up the x distance, then add in number of gaps in that range
    // add both those up to get the distance
    let p0 = pair[0];
    let p1 = pair[1];
    let (p0, p1) = if p0.0 > p1.0 { (p1, p0) } else { (p0, p1) };

    let y_dist = (p0.0 - p1.0).abs();
    let x_dist = (p0.1 - p1.1).abs();

    let y_gaps = map
        .horizontal_gaps
        .iter()
        .filter(|c| (p0.0..p1.0).contains(c))
        .count() as i64;

    let (p0, p1) = if p0.1 > p1.1 { (p1, p0) } else { (p0, p1) };

    let x_gaps = map
        .vertical_gaps
        .iter()
        .filter(|c| (p0.1..p1.1).contains(c))
        .count() as i64;

    y_dist + (y_gaps * 999_999) + x_dist + (x_gaps * 999_999)
}

fn parse_input(input: &str) -> StarMap {
    let width = input.lines().count();
    let height = input.lines().collect_vec().first().unwrap().len();

    let stars = input
        .lines()
        .enumerate()
        .flat_map(|(y, line)| {
            line.chars()
                .enumerate()
                .filter_map(|(x, c)| {
                    if c == '#' {
                        Some(Coord(y as i64, x as i64))
                    } else {
                        None
                    }
                })
                .collect_vec()
        })
        .collect::<HashSet<_>>();

    let mut vertical_gaps = HashSet::new();
    let mut horizontal_gaps = HashSet::new();

    let counts = stars.clone().iter().counts_by(|c| c.0);
    for y in 0..height {
        if !counts.contains_key(&(y as i64)) {
            horizontal_gaps.insert(y as i64);
        }
    }
    let counts = stars.clone().iter().counts_by(|c| c.1);
    for x in 0..width {
        if !counts.contains_key(&(x as i64)) {
            vertical_gaps.insert(x as i64);
        }
    }

    StarMap {
        stars,
        vertical_gaps,
        horizontal_gaps,
    }
}

#[derive(Debug)]
struct StarMap {
    stars: HashSet<Coord>,
    vertical_gaps: HashSet<i64>,
    horizontal_gaps: HashSet<i64>,
}
