use std::collections::{HashMap, HashSet, VecDeque};

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let input = Day::input_raw(2024, 12).unwrap();
        let plot = Plot::from_input(&input);
        println!("{}", plot.value());
        Ok(())
    }
}

struct Plot {
    item_to_subplots: HashMap<char, Vec<Subplot>>,
}

impl Plot {
    pub fn from_input(input: &str) -> Self {
        let rows = input
            .lines()
            .map(str::trim)
            .filter(|l| !l.is_empty())
            .collect::<Vec<_>>();
        let width = rows[0].len();
        let height = rows.len();
        let map = Map::init(width, height);
        let mut item_to_subplots: HashMap<char, Vec<Subplot>> = HashMap::new();

        let idx_to_item = rows
            .into_iter()
            .enumerate()
            .flat_map(|(row_idx, row)| {
                let width = row.len();
                row.chars()
                    .enumerate()
                    .map(|(idx, c)| (row_idx * width + idx, c))
                    .collect::<Vec<_>>()
            })
            .collect::<HashMap<_, _>>();

        // collect all points
        // take a point, and flood fill, removing points from points set
        let mut unexplored = idx_to_item.keys().cloned().collect::<HashSet<_>>();
        for (idx, item) in &idx_to_item {
            if unexplored.contains(idx) {
                let mut points = HashSet::new();

                let mut frontier = VecDeque::from([*idx]);
                while let Some(idx) = frontier.pop_front() {
                    points.insert(idx);
                    unexplored.remove(&idx);

                    let to_explore = map
                        .get_neighbors(idx)
                        .into_iter()
                        .filter(|i| idx_to_item[i] == *item)
                        .filter(|i| unexplored.contains(i))
                        .collect::<Vec<_>>();

                    for i in to_explore {
                        unexplored.remove(&i);
                        frontier.push_back(i);
                    }
                }
                let subplot = Subplot {
                    map_width: width,
                    points,
                };
                item_to_subplots
                    .entry(*item)
                    .and_modify(|sps| sps.push(subplot.clone()))
                    .or_insert(vec![subplot]);
            }
        }

        Self { item_to_subplots }
    }

    pub fn value(&self) -> usize {
        self.item_to_subplots
            .iter()
            .map(|(_item, subplots)| {
                subplots
                    .iter()
                    .map(|subplot| subplot.area() * subplot.perimeter())
                    .sum::<usize>()
            })
            .sum()
    }
}

#[derive(Debug, Clone)]
struct Subplot {
    map_width: usize,
    points: HashSet<usize>,
}

impl Subplot {
    pub fn area(&self) -> usize {
        self.points.len()
    }

    pub fn perimeter(&self) -> usize {
        // for each point, check the neighbors. the number of neighbors determines
        // how many edges are exposed. if a point as 4 neighbors, then it has 0
        // perimeter if it has no neighbors, then it has 4 perimeter.
        self.points
            .iter()
            .map(|idx| {
                let num_neighbors = vec![
                    // up
                    idx.checked_sub(self.map_width),
                    // down
                    Some(idx + self.map_width),
                    // left
                    if idx % self.map_width == 0 {
                        None
                    } else {
                        idx.checked_sub(1)
                    },
                    // right
                    if idx % self.map_width == (self.map_width - 1) {
                        None
                    } else {
                        Some(idx + 1)
                    },
                ]
                .into_iter()
                .flatten()
                .filter(|idx| self.points.contains(idx))
                .count();

                4 - num_neighbors
            })
            .sum()
    }
}

struct Map {
    width: usize,
    max_idx: usize,
}

impl Map {
    pub fn init(width: usize, height: usize) -> Self {
        Self {
            width,
            max_idx: width * height,
        }
    }

    fn get_neighbors(&self, idx: usize) -> HashSet<usize> {
        vec![
            // up
            idx.checked_sub(self.width),
            // down
            Some(idx + self.width),
            // left
            if idx % self.width == 0 {
                None
            } else {
                idx.checked_sub(1)
            },
            // right
            if idx % self.width == (self.width - 1) {
                None
            } else {
                Some(idx + 1)
            },
        ]
        .into_iter()
        .flatten()
        .filter(|idx| *idx < self.max_idx) // in
        .collect()
    }
}

#[test]
fn test_plot() {
    let input = "AAAA
BBCD
BBCC
EEEC";

    let input = "RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE";
    let p = Plot::from_input(input);
    println!("{}", p.value());
}
