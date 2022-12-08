use std::collections::HashSet;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day8;

type Board = Vec<Vec<((usize, usize), u32)>>;

impl AdventOfCode for Day8 {
    fn solve() -> Result<()> {
        let input = Day8::input_lines(2022, 8)?
            .iter()
            .enumerate()
            .map(|(row, line)| {
                line.split("")
                    .flat_map(|num| num.parse::<u32>())
                    .enumerate()
                    .map(|(col, num)| ((row, col), num))
                    .collect::<Vec<((usize, usize), u32)>>()
            })
            .collect::<Board>();

        // let input = vec!["30373", "25512", "65332", "33549", "35390"]
        //     .iter()
        //     .enumerate()
        //     .map(|(row, line)| {
        //         line.split("")
        //             .flat_map(|num| num.parse::<u32>())
        //             .enumerate()
        //             .map(|(col, num)| ((row, col), num))
        //             .collect::<Vec<((usize, usize), u32)>>()
        //     })
        //     .collect::<Board>();

        part_1(&input);
        part_2(&input);

        Ok(())
    }
}

fn part_1(input: &Board) {
    let board = input.clone();
    let width = input.first().unwrap().len();
    let height = input.len();

    let left = scan_left_right(&board);
    let right = scan_right_left(&board);

    // for row in &input {
    //     println!("{:?}", row);
    // }
    // println!();
    let rotated = rotate(board);
    // for row in &rotated {
    //     println!("{:?}", row);
    // }

    let top = scan_left_right(&rotated);
    let bottom = scan_right_left(&rotated);

    let interior_count = left
        .union(&right)
        .map(|t| t.to_owned())
        .collect::<HashSet<_>>()
        .union(&top)
        .map(|t| t.to_owned())
        .collect::<HashSet<_>>()
        .union(&bottom)
        .collect::<HashSet<_>>()
        .len();

    let exterior_count = (width * 2) + (height * 2) - 4;

    // println!();
    // println!("left: {:?}", left);
    // println!("right: {:?}", right);
    // println!("top: {:?}", top);
    // println!("bottom: {:?}", bottom);
    // println!();

    println!("interior count: {}", interior_count);
    println!("exterior count: {}", exterior_count);

    println!("part 1: {}", exterior_count + interior_count);
}

fn rotate(v: Board) -> Board {
    let tranposed = transpose(v);
    tranposed
        .into_iter()
        .map(|row| row.into_iter().rev().collect::<Vec<_>>())
        .collect()
}

fn part_2(input: &Board) {}

fn transpose(v: Board) -> Board {
    assert!(!v.is_empty());
    let len = v[0].len();
    let mut iters: Vec<_> = v.into_iter().map(|row| row.into_iter()).collect();
    (0..len)
        .map(|x| {
            iters
                .iter_mut()
                .map(|row| row.next().unwrap())
                .collect::<Vec<((usize, usize), u32)>>()
        })
        .collect()
}

fn scan_left_right(xxs: &Board) -> HashSet<(usize, usize)> {
    let mut seen: HashSet<(usize, usize)> = HashSet::new();
    for row in xxs
        .iter()
        .skip(1) // skip top
        .take(xxs.len() - 2)
    {
        let toadd = row
            .iter()
            .skip(1) // skip left edge
            .take(row.len() - 2)
            .scan((row[0].1, vec![]), |(tallest, seen), (coord, tree)| {
                if tree > tallest {
                    seen.push(coord.clone());
                    *tallest = *tree;
                }
                Some((*tree, seen.clone()))
            })
            .last()
            .unwrap()
            .1;

        for t in toadd.iter() {
            seen.insert(t.clone());
        }
    }

    seen
}

fn scan_right_left(xxs: &Board) -> HashSet<(usize, usize)> {
    let mut seen: HashSet<(usize, usize)> = HashSet::new();
    for row in xxs
        .iter()
        .skip(1) // skip top
        .take(xxs.len() - 2)
    {
        let toadd = row
            .iter()
            .rev()
            .skip(1) // skip left edge
            .take(row.len() - 2)
            .scan(
                (row.last().unwrap().1, vec![]),
                |(tallest, seen), (coord, tree)| {
                    if tree > tallest {
                        seen.push(coord.clone());
                        *tallest = *tree;
                    }
                    Some((*tree, seen.clone()))
                },
            )
            .last()
            .unwrap()
            .1;

        for t in toadd.iter() {
            seen.insert(t.to_owned());
        }
    }

    seen
}
