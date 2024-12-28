use std::fmt::Debug;

use itertools::Itertools;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let input = Day::input_raw(2024, 9).unwrap();
        let input = input.trim();
        let mut cs = CheckSum::from_input(input);

        let mut part1 = cs.clone();
        part1.defrag();
        let checksum = part1.checksum();
        println!("{checksum}");
        assert_eq!(checksum, 6_415_184_586_041);

        cs.defrag2();
        let checksum = cs.checksum();
        println!("{checksum}");
        assert_eq!(checksum, 6_436_819_084_274);

        Ok(())
    }
}

#[derive(Clone)]
struct CheckSum {
    disk: Vec<(Option<u32>, u32)>,
}

impl CheckSum {
    pub fn from_input(input: &str) -> Self {
        let nums = input
            .chars()
            .map(|c| c.to_digit(10).unwrap())
            .collect::<Vec<_>>();

        let mut disk = vec![];
        let mut id = 0;
        for (count, space) in nums.iter().tuples() {
            disk.push((Some(id), *count));
            disk.push((None, *space));
            id += 1;
        }

        if input.len() % 2 == 1 {
            disk.push((Some(id), nums[nums.len() - 1]));
        }

        Self { disk }
    }

    pub fn defrag(&mut self) {
        let mut front_idx = 0;
        let mut back_idx = self.disk.len() - 1;

        while back_idx >= front_idx {
            let (front_id, space) = self.disk[front_idx];
            let (back_id, count) = self.disk[back_idx];

            if back_id.is_none() {
                back_idx -= 1;
                continue;
            }
            if front_id.is_none() {
                if space >= count {
                    self.disk[front_idx] = self.disk[back_idx];
                    self.disk[back_idx] = (None, 0);
                    back_idx -= 1;
                    if space > count {
                        self.disk.insert(front_idx + 1, (None, space - count));
                    }
                } else {
                    // not enough space...split back file
                    self.disk[front_idx] = (back_id, space);
                    self.disk[back_idx] = (back_id, count - space);
                }
            }
            front_idx += 1;
        }
    }

    pub fn defrag2(&mut self) {
        for back_idx in (1..self.disk.len()).rev() {
            let (back_id, count) = self.disk[back_idx];
            if back_id.is_some() {
                for front_idx in 0..back_idx {
                    let (front_id, space) = self.disk[front_idx];
                    if front_id.is_none() && space >= count {
                        self.disk[front_idx] = self.disk[back_idx];
                        self.disk[back_idx] = (None, count);
                        if space > count {
                            self.disk.insert(front_idx + 1, (None, space - count));
                        }
                        break;
                    }
                }
            }
        }
    }

    pub fn checksum(&self) -> u64 {
        let mut idx = 0;
        let mut sum = 0;
        for (id, count) in &self.disk {
            if let Some(id) = id {
                for _ in 0..*count {
                    sum += u64::from(*id) * idx as u64;
                    idx += 1;
                }
            } else {
                idx += *count as usize;
            }
        }
        sum
    }
}

impl Debug for CheckSum {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        for (id, count) in &self.disk {
            let c = id
                .map(|i| i.to_string().chars().next().unwrap())
                .unwrap_or('.')
                .to_string();

            write!(f, "{}", c.repeat(*count as usize)).unwrap();
        }
        Ok(())
    }
}

#[test]
fn test_parts() {
    let input = "2333133121414131402";
    let cs = CheckSum::from_input(input);
    assert_eq!(
        format!("{cs:?}"),
        "00...111...2...333.44.5555.6666.777.888899".to_string(),
    );

    let mut part1 = cs.clone();
    part1.defrag();

    assert_eq!(
        format!("{part1:?}").trim_end_matches('.'),
        "0099811188827773336446555566".to_string()
    );

    assert_eq!(part1.checksum(), 1928);

    let mut part2 = cs.clone();
    part2.defrag2();

    assert_eq!(
        format!("{part2:?}").trim_end_matches('.'),
        "00992111777.44.333....5555.6666.....8888".to_string()
    );

    assert_eq!(part2.checksum(), 2858);
}
