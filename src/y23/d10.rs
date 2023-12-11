use std::{
    collections::{hash_map::Entry, HashMap, HashSet, VecDeque},
    ops::Add,
};

use anyhow::Result;

use crate::{
    adventofcode::AdventOfCode,
    data::{coord::Coord, direction::Direction, grid::Map},
};

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let input = Day::input_raw(2023, 10)?;
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

    let non_loop_pipes = map
        .keys()
        .filter(|c| !loop_pipes.contains(*c))
        .cloned()
        .collect::<Vec<_>>();

    cluster(non_loop_pipes)
        .iter()
        .filter_map(|cluster| {
            if is_contained(cluster, &loop_pipes) {
                Some(cluster.len())
            } else {
                None
            }
        })
        .sum()
}

fn is_contained(cluster: &HashSet<Coord>, pipe: &HashSet<Coord>) -> bool {
    // for every node in the cluster, check that its neighbors are either other
    // members of the cluster OR members of the pipe.
    cluster.iter().all(|c| {
        Direction::all()
            .map(|d| d.to_coord())
            .map(|change| *c + change)
            .iter()
            .all(|n| pipe.contains(n) || cluster.contains(n))
    })
}

fn cluster(dirt: Vec<Coord>) -> Vec<HashSet<Coord>> {
    let mut clusters = vec![];

    fn helper(xs: Vec<Coord>, clusters: &mut Vec<HashSet<Coord>>) -> Vec<HashSet<Coord>> {
        // take first element
        // for each remaining element, if it connects to any elements in the current
        // cluster, move it to the cluster list. otherwise, move the element to another
        // list. repeat until no more elements.
        if let Some((start, remaining)) = xs.split_first() {
            let mut cluster = HashSet::from([start.clone()]);
            let mut next = Vec::with_capacity(remaining.len());

            for x in remaining {
                if x != start && cluster.iter().any(|c| c.is_neighbor(&x)) {
                    cluster.insert(x.clone());
                } else {
                    next.push(x.clone());
                }
            }

            clusters.push(cluster);

            helper(next, clusters)
        } else {
            clusters.to_vec()
        }
    }

    helper(dirt, &mut clusters)
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
        Tile::Start => vec![Direction::U, Direction::L],
        // Tile::Start => vec![Direction::D, Direction::L],
        _ => vec![], // Tile::Start => vec![Direction::L, Direction::R],
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
    fn test_is_contained() {
        let pipe = HashSet::from([
            Coord(0, 0),
            Coord(0, 1),
            Coord(0, 2),
            Coord(1, 0),
            Coord(1, 2),
            Coord(2, 0),
            Coord(2, 1),
            Coord(2, 2),
        ]);

        let cluster = HashSet::from([Coord(1, 1)]);

        assert!(is_contained(&cluster, &pipe));

        let pipe = HashSet::from([
            Coord(0, 0),
            Coord(0, 1),
            Coord(0, 2),
            Coord(0, 3),
            Coord(0, 4),
            Coord(1, 0),
            Coord(1, 4),
            Coord(2, 0),
            Coord(2, 4),
            Coord(3, 0),
            Coord(3, 4),
            Coord(4, 0),
            Coord(4, 1),
            Coord(4, 2),
            Coord(4, 3),
            Coord(4, 4),
        ]);

        let cluster = HashSet::from([
            Coord(1, 1),
            Coord(1, 2),
            Coord(1, 3),
            Coord(2, 1),
            Coord(2, 2),
            Coord(2, 3),
            Coord(3, 1),
            Coord(3, 2),
            Coord(3, 3),
        ]);

        assert!(is_contained(&cluster, &pipe));
    }
}
