use anyhow::Result;

use crate::adventofcode::AdventOfCode;

struct Ingredient {
    name: &'static str,
    capacity: i64,
    durability: i64,
    flavor: i64,
    texture: i64,
    calories: i64,
}

const INGREDIENTS: [Ingredient; 4] = [
    Ingredient {
        name: "Frosting",
        capacity: 4,
        durability: -2,
        flavor: 0,
        texture: 0,
        calories: 5,
    },
    Ingredient {
        name: "Candy",
        capacity: 0,
        durability: 5,
        flavor: -1,
        texture: 0,
        calories: 8,
    },
    Ingredient {
        name: "Butterscotch",
        capacity: -1,
        durability: 0,
        flavor: 5,
        texture: 0,
        calories: 6,
    },
    Ingredient {
        name: "Sugar",
        capacity: 0,
        durability: 0,
        flavor: -2,
        texture: 2,
        calories: 1,
    },
];

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
        Ok(())
    }

    fn part2() -> Result<()> {
        Ok(())
    }
}

#[cfg(test)]
mod tests {
    #[test]
    fn test_step() {}
}
