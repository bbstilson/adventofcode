// TODO: part 2. point in polygon needs edge case handling of a point being on a line

use std::{
    collections::{hash_map::Entry, HashMap, HashSet, VecDeque},
    ops::Add,
};

use anyhow::Result;
use itertools::Itertools;
use num::Integer;

use crate::{
    adventofcode::AdventOfCode,
    data::{coord::Coord, direction::Direction, grid::Map},
};

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let _input = Day::input_raw(2023, 10)?;
        //         let input = "FF7FSF7F7F7F7F7F---7
        // L|LJ||||||||||||F--J
        // FL-7LJLJ||||||LJL-77
        // F--JF--7||LJLJ7F7FJ-
        // L---JF-JLJ.||-FJLJJ7
        // |F|F-JF---7F7-L7L|7|
        // |FFJF7L7F-JF7|JL---7
        // 7-L-JL7||F7|L7F-7F7|
        // L.L7LFJ|||||FJL7||LJ
        // L7JLJL-JLJLJL--JLJ.L"
        //             .to_string();
        let input = "...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
..........."
            .to_string();

        let map: Map<Tile> = parse_input(&input);

        let start = map
            .iter()
            .find(|(_, tile)| **tile == Tile::Start)
            .unwrap()
            .0;

        println!("{}", part_1(*start, &map));
        println!("{}", part_2(*start, &map));

        Ok(())
    }
}

fn parse_input(input: &str) -> Map<Tile> {
    input
        .lines()
        .enumerate()
        .flat_map(|(y, l)| {
            l.chars()
                .enumerate()
                .map(|(x, c)| (Coord(y as i64, x as i64), Tile::from(c)))
                .collect::<Vec<_>>()
        })
        .collect()
}

fn part_1(start: Coord, map: &Map<Tile>) -> i64 {
    find_pipe(start, map)
        .into_iter()
        .max_by(|(_, s1), (_, s2)| s1.cmp(s2))
        .unwrap()
        .1
}

fn part_2(start: Coord, map: &Map<Tile>) -> usize {
    let loop_map = find_pipe(start, map);
    let loop_pipes = loop_map.keys().cloned().collect::<HashSet<_>>();

    // arrange loop pipes by their column
    let column_to_pipes = loop_map.keys().cloned().into_group_map_by(|c| c.0);

    println!("{:?}", column_to_pipes);

    map.keys()
        .filter(|c| !loop_pipes.contains(*c) && is_inside_polygon(c, &column_to_pipes))
        .inspect(|c| println!("{c:?}"))
        .count()
}

fn is_inside_polygon(c: &Coord, column_to_pipes: &HashMap<i64, Vec<Coord>>) -> bool {
    // scan across the veritcal line the coordinate is apart of.
    // if the count is odd, it's inside, if it's even, it's outside
    // tricky edge case is a ray being cast on an edge
    // https://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm
    if let Some(cs) = column_to_pipes.get(&c.0) {
        // let is_line = cs.iter().sorted_by(|a, b| a.1.cmp(b.1))
        cs.iter().filter(|c1| c1.1 > c.1).count().is_odd()
    } else {
        false
    }
}

fn find_pipe(start: Coord, map: &Map<Tile>) -> Map<i64> {
    let mut scores: HashMap<Coord, i64> = HashMap::from([(start, 0)]);
    let mut frontier = VecDeque::from([(start, 0)]);

    while let Some((coord, cost)) = frontier.pop_front() {
        let ns = adjacent_pipes(coord, map);

        for neighbor in ns {
            if let Entry::Vacant(e) = scores.entry(neighbor) {
                let next_cost = cost + 1;
                e.insert(next_cost);
                frontier.push_back((neighbor, next_cost));
            }
        }
    }

    scores
}

fn adjacent_pipes(c: Coord, map: &Map<Tile>) -> Vec<Coord> {
    pipe_to_possible_directions(c, map)
        .into_iter()
        .map(|d| c + d.to_coord())
        .filter(|c| map.contains_key(c))
        .collect()
}

fn pipe_to_possible_directions(c: Coord, map: &Map<Tile>) -> Vec<Direction> {
    match map[&c] {
        Tile::EW => vec![Direction::L, Direction::R],
        Tile::NS => vec![Direction::U, Direction::D],
        Tile::NE => vec![Direction::U, Direction::R],
        Tile::NW => vec![Direction::U, Direction::L],
        Tile::SE => vec![Direction::D, Direction::R],
        Tile::SW => vec![Direction::D, Direction::L],
        Tile::Start => vec![Direction::D, Direction::R],
        _ => vec![],
    }
}

impl Add for Coord {
    type Output = Coord;

    fn add(self, rhs: Self) -> Self::Output {
        Coord(self.0 + rhs.0, self.1 + rhs.1)
    }
}

#[derive(Debug, PartialEq, Eq, Clone, Copy)]
enum Tile {
    NS,
    EW,
    NE,
    NW,
    SW,
    SE,
    Start,
    Dirt,
}

impl From<Tile> for char {
    fn from(value: Tile) -> Self {
        match value {
            Tile::NS => '|',
            Tile::EW => '-',
            Tile::NE => 'L',
            Tile::NW => 'J',
            Tile::SW => '7',
            Tile::SE => 'F',
            Tile::Start => 'S',
            Tile::Dirt => '.',
        }
    }
}

impl std::fmt::Display for Tile {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        let c: char = (*self).into();
        write!(f, "{c}")
    }
}

impl From<char> for Tile {
    fn from(value: char) -> Self {
        match value {
            '|' => Self::NS,
            '-' => Self::EW,
            'L' => Self::NE,
            'J' => Self::NW,
            '7' => Self::SW,
            'F' => Self::SE,
            'S' => Self::Start,
            '.' => Self::Dirt,
            v => panic!("bad pipe: {v}"),
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_is_inside_polygon() {
        let column_to_pipes = vec![
            Coord(0, 0),
            Coord(0, 1),
            Coord(0, 2),
            Coord(1, 0),
            Coord(1, 2),
            Coord(2, 0),
            Coord(2, 1),
            Coord(2, 2),
        ]
        .into_iter()
        .into_group_map_by(|c| c.0);

        assert!(is_inside_polygon(&Coord(1, 1), &column_to_pipes))
    }
}
