use anyhow::Result;

use crate::adventofcode::AdventOfCode;

struct Ingredient {
    capacity: i64,
    durability: i64,
    flavor: i64,
    texture: i64,
    calories: i64,
}

const INGREDIENTS: [Ingredient; 4] = [
    Ingredient {
        // Frosting
        capacity: 4,
        durability: -2,
        flavor: 0,
        texture: 0,
        calories: 5,
    },
    Ingredient {
        // Candy
        capacity: 0,
        durability: 5,
        flavor: -1,
        texture: 0,
        calories: 8,
    },
    Ingredient {
        // Butterscotch
        capacity: -1,
        durability: 0,
        flavor: 5,
        texture: 0,
        calories: 6,
    },
    Ingredient {
        // Sugar
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
        let mut best = 0;
        let max = 100;
        for a in 0..=max {
            for b in 0..=(max - a) {
                for c in 0..=(max - b - a) {
                    for d in 0..=(max - c - b - a) {
                        best = score_1([a, b, c, d]).max(best);
                    }
                }
            }
        }

        println!("{best}");
        Ok(())
    }

    fn part2() -> Result<()> {
        let mut best = 0;
        let max = 100;
        for a in 0..=max {
            for b in 0..=(max - a) {
                for c in 0..=(max - b - a) {
                    for d in 0..=(max - c - b - a) {
                        best = score_2([a, b, c, d]).max(best);
                    }
                }
            }
        }

        println!("{best}");
        Ok(())
    }
}

fn score_1(amounts: [i64; 4]) -> u64 {
    // The total score of a cookie can be found by adding up each of the properties
    // (negative totals become 0) and then multiplying together everything except
    // calories.
    let (cap, dur, fla, tex) = INGREDIENTS.into_iter().zip(amounts).fold(
        (0, 0, 0, 0),
        |(cap, dur, fla, tex), (ing, amt)| {
            (
                cap + ing.capacity * amt,
                dur + ing.durability * amt,
                fla + ing.flavor * amt,
                tex + ing.texture * amt,
            )
        },
    );
    score_or_zero(cap) * score_or_zero(dur) * score_or_zero(fla) * score_or_zero(tex)
}

fn score_2(amounts: [i64; 4]) -> u64 {
    // The total score of a cookie can be found by adding up each of the properties
    // (negative totals become 0) and then multiplying together everything except
    // calories.
    let (cal, cap, dur, fla, tex) = INGREDIENTS.into_iter().zip(amounts).fold(
        (0, 0, 0, 0, 0),
        |(cal, cap, dur, fla, tex), (ing, amt)| {
            (
                cal + ing.calories * amt,
                cap + ing.capacity * amt,
                dur + ing.durability * amt,
                fla + ing.flavor * amt,
                tex + ing.texture * amt,
            )
        },
    );

    if cal <= 500 {
        score_or_zero(cap) * score_or_zero(dur) * score_or_zero(fla) * score_or_zero(tex)
    } else {
        0
    }
}

fn score_or_zero(x: i64) -> u64 {
    if x.is_negative() {
        0
    } else {
        x as u64
    }
}
