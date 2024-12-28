use std::collections::HashMap;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let input = Day::input_raw(2024, 10).unwrap();
        let mut tm = TopoMap::from_input(&input);
        let scores = tm.traverse();
        let part1 = scores.values().sum::<u32>();
        println!("{part1}");
        assert_eq!(part1, 646);
        let part2 = tm.traverse2().into_iter().sum::<u32>();
        println!("{part2}");
        assert_eq!(part2, 1494);
        Ok(())
    }
}

struct TopoMap {
    topo_map: Vec<u32>,
    width: usize,
    starts: Vec<usize>,
    ends: Vec<usize>,
    score_map: Vec<u32>,
}

impl TopoMap {
    pub fn from_input(input: &str) -> Self {
        let topo_map = input
            .lines()
            .map(str::trim)
            .filter(|l| !l.is_empty())
            .map(|l| {
                l.chars()
                    .map(|c| c.to_digit(10).unwrap())
                    .collect::<Vec<_>>()
            })
            .collect::<Vec<_>>();

        let height = topo_map.len();
        let width = topo_map[0].len();
        let topo_map = topo_map.into_iter().flatten().collect::<Vec<_>>();
        let starts = topo_map
            .iter()
            .enumerate()
            .filter_map(|(idx, n)| if *n == 0 { Some(idx) } else { None })
            .collect();
        let ends = topo_map
            .iter()
            .enumerate()
            .filter(|(_, n)| **n == 9)
            .map(|(idx, _)| idx)
            .collect();
        Self {
            topo_map,
            width,
            starts,
            ends,
            score_map: vec![0; width * height],
        }
    }

    pub fn traverse(&self) -> HashMap<usize, u32> {
        let mut scores: HashMap<usize, u32> =
            self.starts.clone().into_iter().map(|i| (i, 0)).collect();
        // for each end, try to get to each start.
        for end_idx in self.ends.clone() {
            for start_idx in self.starts.clone() {
                if self.can_reach(end_idx, start_idx) {
                    scores.entry(start_idx).and_modify(|c| *c += 1);
                }
            }
        }

        scores
    }

    fn can_reach(&self, curr: usize, target: usize) -> bool {
        if curr == target {
            return true;
        }

        let mut can_reach = false;
        for neighbor in self.get_valid_neighbors(curr) {
            can_reach = can_reach || self.can_reach(neighbor, target);
        }
        can_reach
    }

    pub fn traverse2(&mut self) -> Vec<u32> {
        // starting at the ends:
        // traverse, dfs, in all valid directions, updating the scores as you go
        for end_idx in self.ends.clone() {
            self.dfs(end_idx);
        }

        self.starts.iter().map(|s| self.score_map[*s]).collect()
    }

    fn dfs(&mut self, curr: usize) {
        self.score_map[curr] += 1;

        for dir in self.get_valid_neighbors(curr) {
            self.dfs(dir);
        }
    }

    fn get_valid_neighbors(&self, idx: usize) -> Vec<usize> {
        let curr_value = self.topo_map[idx];
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
        .filter(|idx| {
            // in map
            *idx < self.topo_map.len() &&
            // one lower
            self.topo_map[*idx] + 1 == curr_value
        })
        .collect()
    }
}
