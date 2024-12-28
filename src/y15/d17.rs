use std::collections::HashMap;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let inventory = Inventory::new(&[
            11, 30, 47, 31, 32, 36, 3, 1, 5, 3, 32, 36, 15, 11, 46, 26, 28, 1, 19, 3,
        ]);
        Self::part1(inventory.clone())?;
        Self::part2()?;

        Ok(())
    }
}

impl Day {
    fn part1(inventory: Inventory) -> Result<()> {
        let mut inventory = inventory;
        let combinations = combinate(150, &mut inventory);
        println!("{combinations}");
        Ok(())
    }

    fn part2() -> Result<()> {
        Ok(())
    }
}

#[derive(Debug, Clone, Hash, PartialEq, Eq)]
struct Inventory {
    inner: Vec<u8>,
    quantity: Vec<usize>,
}

impl Inventory {
    fn new(containers: &[u8]) -> Self {
        Self {
            inner: containers.into(),
            quantity: vec![1; containers.len()],
        }
    }

    fn options(&self, remaining: u8) -> Vec<usize> {
        self.inner
            .iter()
            .zip(self.quantity.iter())
            .enumerate()
            .filter(|(_, (size, quantity))| **quantity == 1 && **size <= remaining)
            .map(|(c, _)| c)
            .collect()
    }

    fn fill(&mut self, container: usize) {
        self.quantity[container] = 0;
    }

    fn unfill(&mut self, container: usize) {
        self.quantity[container] = 1;
    }

    fn get_value(&self, idx: usize) -> u8 {
        self.inner[idx]
    }
}

fn combinate(target: u8, inventory: &mut Inventory) -> u32 {
    fn helper(
        inventory: &mut Inventory,
        remaining: u8,
        cache: &mut HashMap<Vec<usize>, u32>,
    ) -> u32 {
        if remaining == 0 {
            return 1;
        }
        let options = inventory.options(remaining);
        if let Some(prev) = cache.get(&options) { *prev } else {
            let mut combinations = 0;
            for option in &options {
                inventory.fill(*option);
                let next_remaining = remaining - inventory.get_value(*option);
                let ans = helper(inventory, next_remaining, cache);
                println!("{option} | {remaining} -> {next_remaining} | {ans}");
                inventory.unfill(*option);
                combinations += ans;
            }
            println!("{options:?} -> {remaining} -> {combinations}");
            cache.insert(options.clone(), combinations / 2);
            combinations / 2
        }
    }

    let mut cache = HashMap::new();
    let ans = helper(inventory, target, &mut cache);
    dbg!(cache);
    ans
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_combinate() {
        // let mut inventory = Inventory::new(&[20, 15, 10, 5, 5]);
        // let combinations = combinate(25, &mut inventory);
        // assert_eq!(combinations, 4);

        let mut inventory = Inventory::new(&[10, 5, 5, 5]);
        let combinations = combinate(10, &mut inventory);
        assert_eq!(combinations, 4);
    }
}
