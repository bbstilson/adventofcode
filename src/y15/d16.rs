use std::u32;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

#[derive(Debug, Default)]
struct Aunt {
    id: u32,
    children: Option<u32>,
    cats: Option<u32>,
    samoyeds: Option<u32>,
    pomeranians: Option<u32>,
    akitas: Option<u32>,
    vizslas: Option<u32>,
    goldfish: Option<u32>,
    trees: Option<u32>,
    cars: Option<u32>,
    perfumes: Option<u32>,
}

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let aunts = Self::input_lines(2015, 16)?
            .into_iter()
            .map(|line| {
                // Sue 353: samoyeds: 7, akitas: 6, vizslas: 4
                let mut aunt = Aunt::default();
                let data = &line[4..];
                let (id, properties) = data.split_once(": ").unwrap();
                aunt.id = id.parse().unwrap();
                for property in properties.split(", ") {
                    let (category, amt) = property.split_once(": ").unwrap();
                    match category {
                        "children" => aunt.children = Some(amt.parse().unwrap()),
                        "cats" => aunt.cats = Some(amt.parse().unwrap()),
                        "samoyeds" => aunt.samoyeds = Some(amt.parse().unwrap()),
                        "pomeranians" => aunt.pomeranians = Some(amt.parse().unwrap()),
                        "akitas" => aunt.akitas = Some(amt.parse().unwrap()),
                        "vizslas" => aunt.vizslas = Some(amt.parse().unwrap()),
                        "goldfish" => aunt.goldfish = Some(amt.parse().unwrap()),
                        "trees" => aunt.trees = Some(amt.parse().unwrap()),
                        "cars" => aunt.cars = Some(amt.parse().unwrap()),
                        "perfumes" => aunt.perfumes = Some(amt.parse().unwrap()),
                        _ => panic!("unknown category: {category}"),
                    }
                }
                aunt
            })
            .collect::<Vec<_>>();
        let target = Aunt {
            id: 0,
            children: Some(3),
            cats: Some(7),
            samoyeds: Some(2),
            pomeranians: Some(3),
            akitas: Some(0),
            vizslas: Some(0),
            goldfish: Some(5),
            trees: Some(3),
            cars: Some(2),
            perfumes: Some(1),
        };

        Self::part1(&target, &aunts)?;
        Self::part2(&target, &aunts)?;

        Ok(())
    }
}

impl Day {
    fn part1(target: &Aunt, aunts: &[Aunt]) -> Result<()> {
        let aunt_id = aunts
            .iter()
            .filter(|aunt| {
                equivalent(aunt.children, target.children)
                    && equivalent(aunt.cats, target.cats)
                    && equivalent(aunt.samoyeds, target.samoyeds)
                    && equivalent(aunt.pomeranians, target.pomeranians)
                    && equivalent(aunt.akitas, target.akitas)
                    && equivalent(aunt.vizslas, target.vizslas)
                    && equivalent(aunt.goldfish, target.goldfish)
                    && equivalent(aunt.trees, target.trees)
                    && equivalent(aunt.cars, target.cars)
                    && equivalent(aunt.perfumes, target.perfumes)
            })
            .map(|aunt| aunt.id)
            .next()
            .unwrap();

        println!("{aunt_id}");
        Ok(())
    }

    fn part2(target: &Aunt, aunts: &[Aunt]) -> Result<()> {
        let aunt_id = aunts
            .iter()
            .filter(|aunt| {
                equivalent(aunt.children, target.children)
                    && gt(aunt.cats, target.cats)
                    && equivalent(aunt.samoyeds, target.samoyeds)
                    && lt(aunt.pomeranians, target.pomeranians)
                    && equivalent(aunt.akitas, target.akitas)
                    && equivalent(aunt.vizslas, target.vizslas)
                    && lt(aunt.goldfish, target.goldfish)
                    && gt(aunt.trees, target.trees)
                    && equivalent(aunt.cars, target.cars)
                    && equivalent(aunt.perfumes, target.perfumes)
            })
            .map(|aunt| aunt.id)
            .next()
            .unwrap();

        println!("{aunt_id}");
        Ok(())
    }
}

fn equivalent(a: Option<u32>, b: Option<u32>) -> bool {
    a.is_none() || a == b
}

fn gt(a: Option<u32>, b: Option<u32>) -> bool {
    a.is_none() || a > b
}

fn lt(a: Option<u32>, b: Option<u32>) -> bool {
    a.is_none() || a < b
}

#[cfg(test)]
mod tests {
    #[test]
    fn test_step() {}
}
