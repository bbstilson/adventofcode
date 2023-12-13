use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let input = Day::input_raw(2023, 13)?;

        let patterns = parse_input(&input);

        println!("{}", part_1(&patterns));

        Ok(())
    }
}

fn part_1(patterns: &[Pattern]) -> usize {
    let cols: usize = patterns
        .iter()
        .filter_map(|p| find_reflection(&p.cols))
        .sum();
    let rows: usize = patterns
        .iter()
        .filter_map(|p| find_reflection(&p.rows))
        .sum();

    cols + (rows * 100)
}

fn find_reflection(xs: &[String]) -> Option<usize> {
    for start in 1..xs.len() {
        let min_dist_to_wall = start.min(xs.len() - start);
        if has_reflection(xs, start, min_dist_to_wall) {
            return Some(start);
        }
    }
    None
}

fn has_reflection(xs: &[String], start: usize, rows_to_compare: usize) -> bool {
    for offset in 1..=rows_to_compare {
        let left_idx = start - offset;
        let right_idx = start + offset - 1;
        if xs[left_idx] != xs[right_idx] {
            return false;
        }
    }
    true
}

#[derive(Debug)]
struct Pattern {
    rows: Vec<String>,
    cols: Vec<String>,
}

fn parse_input(input: &str) -> Vec<Pattern> {
    input.split("\n\n").map(parse_pattern).collect()
}

fn parse_pattern(input: &str) -> Pattern {
    let mut cols = vec![];
    let width = input.lines().next().unwrap().len();

    for i in 0..width {
        let mut s = String::with_capacity(width);
        for line in input.lines() {
            s.push(line.chars().nth(i).unwrap());
        }
        cols.push(s);
    }

    Pattern {
        rows: input.lines().map(|l| l.to_string()).collect(),
        cols,
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_find_reflection() {
        let p1 = parse_pattern(
            "#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.",
        );
        let p2 = parse_pattern(
            "#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#",
        );
        let p3 = parse_pattern(
            "##...##
..#.#..
#...#..
.###.##
##.#.##
##..###
##.....
.#.##..
.#..#..",
        );

        let tests = vec![
            (p1.cols, Some(5)),
            (p1.rows, None),
            (p2.rows, Some(4)),
            (p2.cols, None),
            (p3.cols, Some(6)),
            (p3.rows, None),
        ];

        for (idx, (xs, expected)) in tests.into_iter().enumerate() {
            assert_eq!(find_reflection(&xs), expected, "Failed on test {}", idx + 1)
        }
    }
}
