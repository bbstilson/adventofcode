use std::{
    collections::{HashMap, HashSet, VecDeque},
    hash::Hash,
};

use anyhow::{Ok, Result};

use crate::adventofcode::AdventOfCode;

pub struct Day12;

impl AdventOfCode for Day12 {
    fn solve() -> Result<()> {
        // let input = Day12::input_lines(2022, 12)?;

        let input = samples::MAP
            .to_vec()
            .iter()
            .map(|l| l.to_string())
            .collect::<Vec<_>>();

        let map = Map::from_input(input);

        part1(&map);

        Ok(())
    }
}

fn part1(map: &Map) {
    println!("start: {:?}", map.start);
    println!("end: {:?}", map.end);
    println!();
    // set end cost to 0
    let mut cost_map = HashMap::from([(map.end, 0)]);
    // add valid end neighbors to queue
    let mut frontier = UniqQueue::from_iter(map.get_neighbors_that_can_reach_me(map.end));

    while let Some(coord) = frontier.dequeue() {
        if let Some(cost) = map
            .get_neighbors_that_can_reach_me(coord)
            .into_iter()
            .flat_map(|neighbor| cost_map.get(&neighbor))
            .min()
        {
            cost_map.insert(coord, cost + 1);
            // Add neighbors that are reachable, real, and haven't had their costs
            // set to frontier.
            let valid_neighbors = map
                .get_neighbors_that_can_reach_me(coord)
                .into_iter()
                .filter(|c| !cost_map.contains_key(c));

            for neighbor in valid_neighbors {
                frontier.enqueue(neighbor);
            }
        }

        if coord == map.start {
            println!("done...");
            frontier.clear();
        }
    }

    for y in 0..map.size.0 {
        for x in 0..map.size.1 {
            let coord = (y, x);
            match cost_map.get(&coord) {
                Some(cost) => print!("{:4}", cost),
                None => print!("    "),
            }
        }
        println!();
    }
    println!();

    println!("part 1: {}", cost_map[&map.start]);
}

pub struct UniqQueue<T: Eq + Hash + Copy> {
    seen: HashSet<T>,
    queue: VecDeque<T>,
}

impl<T: Eq + Hash + Copy> UniqQueue<T> {
    pub fn dequeue(&mut self) -> Option<T> {
        match self.queue.pop_front() {
            Some(v) => {
                self.seen.remove(&v);
                Some(v)
            }
            None => None,
        }
    }

    pub fn enqueue(&mut self, t: T) {
        if !self.seen.contains(&t) {
            self.seen.insert(t);
            self.queue.push_back(t);
        }
    }

    pub fn clear(&mut self) {
        self.queue.clear()
    }
}

impl<T: Eq + Hash + Copy> FromIterator<T> for UniqQueue<T> {
    fn from_iter<I: IntoIterator<Item = T>>(iter: I) -> Self {
        let hax = Vec::from_iter(iter);
        UniqQueue {
            seen: HashSet::from_iter(hax.clone()),
            queue: VecDeque::from_iter(hax),
        }
    }
}

type Coord = (isize, isize);

struct Map {
    map: HashMap<Coord, i32>,
    start: Coord,
    end: Coord,
    size: Coord,
}

impl Map {
    pub fn from_input(input: Vec<String>) -> Map {
        let size = (input.len() as isize, input.first().unwrap().len() as isize);
        let char_map = input
            .iter()
            .enumerate()
            .flat_map(|(y, line)| {
                line.chars()
                    .enumerate()
                    .map(move |(x, c)| ((y as isize, x as isize), c))
            })
            .collect::<HashMap<Coord, char>>();

        let start: Coord = char_map
            .iter()
            .find(|(_, char)| char == &&'S')
            .unwrap()
            .0
            .clone();
        let end: Coord = char_map
            .iter()
            .find(|(_, char)| char == &&'E')
            .unwrap()
            .0
            .clone();

        let map = char_map
            .into_iter()
            .map(|(coord, c)| {
                let fixed = match c {
                    'S' => 'a',
                    'E' => 'z',
                    o => o,
                };
                let n = u32::from(fixed) - 96;
                (coord, n as i32)
            })
            .collect::<HashMap<Coord, i32>>();

        Map {
            map,
            start,
            end,
            size,
        }
    }

    pub fn get_neighbors_that_can_reach_me(&self, coord: Coord) -> Vec<Coord> {
        let (x, y) = coord;
        [(x, y + 1), (x, y - 1), (x + 1, y), (x - 1, y)]
            .iter()
            .filter(|neighbor| self.can_reach(&coord, *neighbor))
            .map(|c| *c)
            .collect::<Vec<_>>()
    }

    fn can_reach(&self, a: &Coord, b: &Coord) -> bool {
        if self.map.contains_key(b) {
            let a_height = self.map[a];
            let b_height = self.map[b];
            if b_height >= a_height || b_height + 1 == a_height {
                true
            } else {
                false
            }
        } else {
            false
        }
    }
}

impl std::fmt::Debug for Map {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        for y in 0..self.size.0 {
            for x in 0..self.size.1 {
                let coord = (y, x);
                let value = self.map[&coord];
                write!(f, "{:3}", value)?;
            }
            writeln!(f)?;
        }
        writeln!(f)
    }
}

#[test]
fn test_get_neighbors_that_can_reach_me() {
    let map = Map::from_input(vec![
        "Eba".to_string(),
        "Scc".to_string(),
        "ada".to_string(),
    ]);

    assert_eq!(
        map.get_neighbors_that_can_reach_me((1, 1)),
        vec![(1, 2), (1, 0), (2, 1), (0, 1)]
    );
}

#[test]
fn test_big_height_diff() {
    let map = Map::from_input(vec![
        "SE".to_string(), //
        "za".to_string(), //
    ]);

    assert_eq!(
        map.get_neighbors_that_can_reach_me((1, 1)),
        vec![(1, 0), (0, 1)]
    );
    assert_eq!(map.get_neighbors_that_can_reach_me((1, 0)), vec![]);

    let map = Map::from_input(vec!["SabcdefaE".to_string()]);
    assert_eq!(map.get_neighbors_that_can_reach_me((0, 6)), vec![(0, 5)]);
    assert_eq!(
        map.get_neighbors_that_can_reach_me((0, 7)),
        vec![(0, 8), (0, 6)]
    );
}

mod samples {
    #[allow(unused)]
    pub const MAP: [&str; 5] = ["Sabqponm", "abcryxxl", "accszExk", "acctuvwj", "abdefghi"];
}
