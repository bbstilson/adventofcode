use std::{
    collections::{HashMap, HashSet},
    u32,
};

use anyhow::Result;
use itertools::Itertools;

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
        let input = Day::input_lines(2015, 9)?;
        let adj = AdjacencyList::from_input(input);

        let mut shortest_dist = u32::MIN;
        for start in &adj.starts() {
            if let Some(dist) = adj.traverse(start) {
                shortest_dist = shortest_dist.max(dist);
            }
        }

        println!("{shortest_dist}");

        Ok(())
    }

    fn part2() -> Result<()> {
        let input = Day::input_lines(2015, 9)?;

        Ok(())
    }
}

type InnerAdjacencyList = HashMap<String, HashMap<String, u32>>;

struct AdjacencyList {
    locations: HashSet<String>,
    map: InnerAdjacencyList,
}

impl AdjacencyList {
    fn from_input(lines: Vec<String>) -> Self {
        let input = lines.into_iter().map(|line| {
            let (a, right) = line.split_once(" to ").unwrap();
            let (b, dist) = right.split_once(" = ").unwrap();
            (a.to_string(), b.to_string(), dist.parse::<u32>().unwrap())
        });

        let mut locations = HashSet::new();
        let mut map: InnerAdjacencyList = HashMap::new();
        for (a, b, dist) in input {
            locations.insert(a.clone());
            locations.insert(b.clone());
            map.entry(a.clone())
                .and_modify(|paths: &mut HashMap<String, u32>| {
                    paths.insert(b.clone(), dist);
                })
                .or_insert(HashMap::from([(b.clone(), dist)]));
            map.entry(b)
                .and_modify(|paths: &mut HashMap<String, u32>| {
                    paths.insert(a.clone(), dist);
                })
                .or_insert(HashMap::from([(a, dist)]));
        }

        Self { map, locations }
    }

    fn traverse(&self, start: &String) -> Option<u32> {
        // returns Some(u32) if all paths were visited.

        fn helper(
            curr: &String,
            cost: u32,
            path: Vec<(&String, u32)>,
            adj_list: &AdjacencyList,
            visited: HashSet<&String>,
        ) -> Option<u32> {
            let mut path = path;
            path.push((curr, cost));

            match adj_list.map.get(curr) {
                Some(neighbors) => {
                    let unvisited_neighbors = neighbors
                        .iter()
                        .filter(|(neighbor, _)| !visited.contains(neighbor))
                        .collect_vec();

                    if unvisited_neighbors.len() == 0 {
                        match path.len() == adj_list.locations.len() {
                            true => Some(sum_path_cost(path)),
                            false => None,
                        }
                    } else {
                        unvisited_neighbors
                            .into_iter()
                            .filter_map(|(neighbor, cost)| {
                                let mut visited = visited.clone();
                                visited.insert(neighbor);
                                // println!("{:?} | {:?} | {:?} | {:?}", curr, neighbor, cost, visited);
                                helper(neighbor, *cost, path.clone(), adj_list, visited)
                            })
                            .max()
                    }
                }
                None => match path.len() == adj_list.locations.len() {
                    true => Some(sum_path_cost(path)),
                    false => None,
                },
            }
        }

        let visited = HashSet::from([start]);
        helper(start, 0, vec![], &self, visited)
    }

    fn starts(&self) -> HashSet<String> {
        self.map.keys().cloned().collect()
    }
}

fn sum_path_cost(path: Vec<(&String, u32)>) -> u32 {
    path.into_iter().map(|(_, cost)| cost).sum()
}
