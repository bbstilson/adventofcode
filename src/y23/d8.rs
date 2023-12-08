use std::collections::HashMap;

use anyhow::Result;
use num::Integer;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let input = Day::input_raw(2023, 8)?;
        let map = parse_input(&input);

        println!("{}", part_1(&map, "AAA", |s| s == "ZZZ"));
        println!("{}", part_2(&map));

        Ok(())
    }
}
fn part_1(map: &Map, start: &str, hit_target: fn(&str) -> bool) -> usize {
    let mut instructions = map.instructions.iter().cycle();

    let mut steps = 0;
    let mut curr = start;
    while let Some(direction) = instructions.next() {
        if hit_target(curr) {
            return steps;
        }

        match *direction {
            Direction::L => curr = &map.map[curr].0,
            Direction::R => curr = &map.map[curr].1,
        }
        steps += 1;
    }
    0
}

fn part_2(map: &Map) -> usize {
    // Collect all nodes that end in 'A'.
    // Then, for each start node, figure out how many steps to reach a 'Z'.
    // Finally, find LCM of those steps.
    map.map
        .keys()
        .filter(|k| k.ends_with('A'))
        .map(|k| *k)
        // start of the algorithm
        .map(|start| part_1(map, start, |s| s.ends_with('Z')))
        .fold(1, |l, r| l.lcm(&r))
}

#[derive(Debug)]
enum Direction {
    L,
    R,
}

impl From<char> for Direction {
    fn from(value: char) -> Self {
        match value {
            'L' => Direction::L,
            'R' => Direction::R,
            _ => panic!("bad direction char: {value}"),
        }
    }
}

#[derive(Debug)]
struct Map<'a> {
    instructions: Vec<Direction>,
    map: HashMap<&'a str, (&'a str, &'a str)>,
}

fn parse_input<'a>(input: &'a str) -> Map {
    let (instructions, map) = input.split_once("\n\n").unwrap();

    Map {
        instructions: instructions.chars().map(Direction::from).collect(),
        map: map.trim().split("\n").map(parse_map_line).collect(),
    }
}

fn parse_map_line<'a>(l: &'a str) -> (&'a str, (&'a str, &'a str)) {
    let (start, choices) = l.split_once(" = ").unwrap();
    let (left, right) = choices[1..choices.len() - 1].split_once(", ").unwrap();

    (start, (left, right))
}
