use std::collections::HashMap;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let input = Day::input_raw(2023, 12)?;

        let rows = parse_input(&input);

        println!("{} == 7922", part_1(&rows));
        println!("{} == 18093821750095", part_2(&rows));

        Ok(())
    }
}

fn part_1(rows: &[Row]) -> usize {
    rows.iter().map(|r| solve(&r.springs, &r.segments)).sum()
}

fn part_2(rows: &[Row]) -> usize {
    rows.iter()
        .map(unfold)
        .map(|r| solve(&r.springs, &r.segments))
        .sum()
}

fn unfold(row: &Row) -> Row {
    let springs = vec![row.springs.clone()]
        .into_iter()
        .chain(["?".to_string()])
        .cycle()
        .take(5 + 4)
        .collect();

    Row {
        springs,
        segments: row.segments.repeat(5),
    }
}

#[derive(Debug, Clone)]
struct Row {
    springs: String,
    segments: Vec<usize>,
}

fn parse_input(input: &str) -> Vec<Row> {
    input.lines().map(parse_line).collect()
}

fn parse_line(line: &str) -> Row {
    let (springs, segments) = line.split_once(' ').unwrap();
    Row {
        springs: springs.to_owned(),
        segments: segments
            .split(',')
            .filter_map(|c| c.parse::<usize>().ok())
            .collect(),
    }
}

fn solve(springs: &str, segments: &[usize]) -> usize {
    fn helper(
        springs: &str,
        segments: &[usize],
        memo: &mut HashMap<(String, Vec<usize>), usize>,
    ) -> usize {
        if segments.is_empty() {
            if springs.contains('#') {
                0
            } else {
                1
            }
        } else if springs.is_empty() {
            0
        } else {
            match springs.chars().next().unwrap() {
                '.' => {
                    let next_springs = springs[1..].to_owned();
                    let k = (next_springs.clone(), segments.to_vec());
                    match memo.get(&k) {
                        Some(n) => *n,
                        None => {
                            let ans = helper(&next_springs, segments, memo);
                            memo.insert(k, ans);
                            ans
                        }
                    }
                }
                '?' => {
                    helper(&replace_first_char(springs, '.'), segments, memo)
                        + helper(&replace_first_char(springs, '#'), segments, memo)
                }
                '#' => {
                    let segment = segments[0];
                    let at_end = segment == springs.len();
                    let range = if at_end { segment } else { segment + 1 };
                    match springs.get(0..range) {
                        Some(s) => {
                            if s.chars().take(segment).all(|c| c != '.') {
                                if at_end && segments.len() == 1 {
                                    1
                                } else if !s.ends_with('#') {
                                    let next_springs =
                                        &springs.chars().skip(segment + 1).collect::<String>();
                                    let next_segments = &segments[1..];
                                    let k = (next_springs.clone(), next_segments.to_vec());
                                    match memo.get(&k) {
                                        Some(n) => *n,
                                        None => {
                                            let ans = helper(&next_springs, next_segments, memo);
                                            memo.insert(k, ans);
                                            ans
                                        }
                                    }
                                } else {
                                    0
                                }
                            } else {
                                0
                            }
                        }
                        _ => 0,
                    }
                }
                _ => panic!("wtf"),
            }
        }
    }
    helper(springs, segments, &mut HashMap::new())
}

fn replace_first_char(s: &str, c: char) -> String {
    s.char_indices()
        .map(|(i, ch)| if i == 0 { c } else { ch })
        .collect()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_unfold() {
        let tests = vec![
            (".#", vec![1], ".#?.#?.#?.#?.#", vec![1, 1, 1, 1, 1]),
            (
                "???.###",
                vec![1, 1, 3],
                "???.###????.###????.###????.###????.###",
                vec![1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3],
            ),
        ];

        for (springs, segments, expected_springs, expected_segments) in tests {
            let unfolded = unfold(&Row {
                springs: springs.to_string(),
                segments: segments.clone(),
            });
            assert_eq!(
                unfolded.springs, expected_springs,
                "failed on ({springs}, {segments:?})"
            );
            assert_eq!(
                unfolded.segments, expected_segments,
                "failed on ({springs}, {segments:?})"
            );
        }
    }

    #[test]
    fn test_insert_segment() {
        let tests = vec![
            (".", vec![1], 0),
            ("#", vec![1], 1),
            ("#.#", vec![1, 1], 1),
            ("?.#", vec![1, 1], 1),
            ("#.?", vec![1, 1], 1),
            ("##.?", vec![2, 1], 1),
            ("##.?", vec![1, 1], 0),
            ("????", vec![1, 1], 3),
            ("?###????????", vec![3, 2, 1], 10),
            ("#.#.#.#", vec![1, 1, 1], 0),
        ];

        for (springs, segments, expected) in tests {
            let ans = solve(springs, &segments);

            assert_eq!(ans, expected, "failed on ({}, {:?})", springs, segments);
        }
    }
}
