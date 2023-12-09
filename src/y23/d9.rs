use anyhow::Result;
use itertools::Itertools;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let input = Day::input_lines(2023, 9)?;

        let histories = input
            .into_iter()
            .map(|l| {
                l.split_whitespace()
                    .filter_map(|s| s.parse::<isize>().ok())
                    .collect_vec()
            })
            .collect_vec();

        println!("{}", part_1(&histories));
        println!("{}", part_2(&histories));

        Ok(())
    }
}

fn part_1(histories: &Vec<Vec<isize>>) -> isize {
    histories.iter().map(predict_next).sum()
}

fn part_2(histories: &Vec<Vec<isize>>) -> isize {
    histories.iter().map(predict_previous).sum()
}

fn predict_previous(xs: &Vec<isize>) -> isize {
    if xs.iter().all(|x| *x == 0) {
        0
    } else {
        xs.first().unwrap() - predict_previous(&diffs(xs))
    }
}

fn predict_next(xs: &Vec<isize>) -> isize {
    if xs.iter().all(|x| *x == 0) {
        0
    } else {
        xs.last().unwrap() + predict_next(&diffs(xs))
    }
}

fn diffs(xs: &[isize]) -> Vec<isize> {
    xs.iter()
        .tuple_windows::<(_, _)>()
        .map(|(a, b)| b - a)
        .collect_vec()
}
