use std::collections::HashMap;

use itertools::Itertools;

use crate::adventofcode::AdventOfCode;

type Rules = HashMap<u32, Vec<u32>>;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let input = Day::input_raw(2024, 5).unwrap();
        let (rules, updates) = parse_input(input);

        part1(&rules, &updates);
        part2(&rules, &updates);

        Ok(())
    }
}

fn parse_input(input: String) -> (Rules, Vec<Vec<u32>>) {
    let (rules, updates): (Vec<String>, Vec<String>) = input
        .lines()
        .map(|l| l.to_string())
        .partition(|l| l.contains('|'));

    let rules = rules
        .into_iter()
        .map(|l| {
            let (l, r) = l.split_once('|').unwrap();

            (l.parse::<u32>().unwrap(), r.parse::<u32>().unwrap())
        })
        .into_group_map();

    let updates = updates
        .into_iter()
        .filter(|l| !l.is_empty())
        .map(|l| {
            l.split(',')
                .map(|n| n.parse::<u32>().unwrap())
                .collect::<Vec<_>>()
        })
        .collect::<Vec<_>>();

    (rules, updates)
}

fn part1(rules: &Rules, updates: &Vec<Vec<u32>>) {
    let mut valid_updates = vec![];
    for update in updates {
        let mut update_valid = true;
        for (page_position, page) in update.iter().enumerate() {
            if let Some(child) = rules.get(&page) {
                // O(n^2)
                if let Some((child_position, _)) =
                    update.iter().enumerate().find(|(_, x)| child.contains(x))
                {
                    if page_position > child_position {
                        update_valid = false;
                    }
                }
            }
        }
        if update_valid {
            valid_updates.push(update);
        }
    }

    let ans = valid_updates
        .into_iter()
        .map(|update| update[update.len() / 2])
        .sum::<u32>();

    println!("{ans}");
}

fn part2(rules: &Rules, updates: &Vec<Vec<u32>>) {
    let mut invalid_updates = vec![];
    for update in updates {
        let mut update_valid = true;
        for (page_position, page) in update.iter().enumerate() {
            if let Some(child) = rules.get(&page) {
                // O(n^2)
                if let Some((child_position, _)) =
                    update.iter().enumerate().find(|(_, x)| child.contains(x))
                {
                    if page_position > child_position {
                        update_valid = false;
                    }
                }
            }
        }
        if !update_valid {
            invalid_updates.push(update);
        }
    }

    // invalid_updates.iter().for_each(|update| {
    //     update.sort_by(|a, b| {});
    // });
    // let ans = invalid_updates
    //     .into_iter()
    //     .map(|update| update[update.len() / 2])
    //     .sum::<u32>();
    let ans = invalid_updates.len();

    println!("{ans}");
}

#[test]
fn test_parts() {
    let test_input = "47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47"
        .to_string();

    let (rules, updates) = parse_input(test_input);

    part1(&rules, &updates);
    part2(&rules, &updates);
}
