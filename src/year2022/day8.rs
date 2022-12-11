use std::collections::HashSet;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day8;

type Row = Vec<((usize, usize), u32)>;
type Coord = (usize, usize);
type Tree = (Coord, u32);

#[derive(Debug, Clone)]
struct Board {
    board: Vec<Row>,
}

impl Board {
    pub fn new() -> Board {
        Board { board: vec![] }
    }

    pub fn transpose(self) -> Board {
        let len = self.board[0].len();
        let mut iters: Vec<_> = self.board.into_iter().map(|row| row.into_iter()).collect();
        (0..len)
            .map(|_| {
                iters
                    .iter_mut()
                    .map(|row| row.next().unwrap())
                    .collect::<Vec<((usize, usize), u32)>>()
            })
            .collect()
    }

    // Terribly inefficient because we copy the data twice.
    // This could be done in-place.
    pub fn rotate(self) -> Board {
        self.transpose()
            .board
            .into_iter()
            .map(|row| row.into_iter().rev().collect::<Vec<_>>())
            .collect()
    }

    // this is really inefficient. each coord should cache how many it can see from
    // every direction. then the next time it's queried, we can just return that number.
    pub fn viewable_from(&self, tree: &Tree) -> u32 {
        self.view_left(tree) * self.view_right(tree) * self.view_up(tree) * self.view_down(tree)
    }

    fn view_left(&self, tree: &Tree) -> u32 {
        let ((y, x), height) = tree;
        self.board
            .iter()
            .skip(y - 1)
            .take(1)
            .map(|row| {
                row.iter()
                    .take(*x)
                    .rev()
                    .take_while(|(_, t)| t < height)
                    .collect::<Vec<_>>()
                    .len()
            })
            .sum::<usize>() as u32
    }

    fn view_right(&self, tree: &Tree) -> u32 {
        let ((y, x), height) = tree;
        self.board
            .iter()
            .skip(y - 1)
            .take(1)
            .map(|row| {
                row.iter()
                    .skip(*x - 1)
                    .take_while(|(_, t)| t < height)
                    .collect::<Vec<_>>()
                    .len()
            })
            .sum::<usize>() as u32
    }

    fn view_up(&self, tree: &Tree) -> u32 {
        let ((y, x), height) = tree;
        self.board
            .iter()
            .skip(y - 1)
            .take(1)
            .map(|row| {
                row.iter()
                    .skip(*x - 1)
                    .take_while(|(_, t)| t < height)
                    .collect::<Vec<_>>()
                    .len()
            })
            .sum::<usize>() as u32
    }

    fn view_down(&self, _tree: &Tree) -> u32 {
        // let ((y, x), height) = tree;
        todo!()
    }

    pub fn scan_right_left(&self) -> HashSet<(usize, usize)> {
        let mut seen: HashSet<(usize, usize)> = HashSet::new();
        for row in self
            .board
            .iter()
            .skip(1) // skip top
            .take(self.board.len() - 2)
        {
            let toadd = row
                .iter()
                .rev()
                .skip(1) // skip left edge
                .take(row.len() - 2)
                .scan(
                    (row.last().unwrap().1, vec![]),
                    |(tallest, seen), (coord, tree)| scan_for_tallest(tallest, seen, coord, tree),
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

    fn scan_left_right(&self) -> HashSet<(usize, usize)> {
        let mut seen: HashSet<(usize, usize)> = HashSet::new();
        for row in self
            .board
            .iter()
            .skip(1) // skip top
            .take(self.board.len() - 2)
        {
            let toadd = row
                .iter()
                .skip(1) // skip left edge
                .take(row.len() - 2)
                .scan((row[0].1, vec![]), |(tallest, seen), (coord, tree)| {
                    scan_for_tallest(tallest, seen, coord, tree)
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
}

fn scan_for_tallest(
    tallest: &mut u32,
    seen: &mut Vec<Coord>,
    coord: &Coord,
    tree: &u32,
) -> Option<(u32, Vec<Coord>)> {
    if tree > tallest {
        seen.push(coord.clone());
        *tallest = *tree;
    }
    Some((*tree, seen.clone()))
}

impl FromIterator<Row> for Board {
    fn from_iter<T: IntoIterator<Item = Row>>(iter: T) -> Self {
        let mut board = Board::new();

        for i in iter {
            board.board.push(i);
        }

        board
    }
}

impl AdventOfCode for Day8 {
    fn solve() -> Result<()> {
        let _input = Day8::input_lines(2022, 8)?
            .iter()
            .enumerate()
            .map(|(row, line)| {
                line.split("")
                    .flat_map(|num| num.parse::<u32>())
                    .enumerate()
                    .map(|(col, num)| ((row, col), num))
                    .collect::<Row>()
            })
            .collect::<Board>();

        let input = vec!["30373", "25512", "65332", "33549", "35390"]
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

        part_1(&input);
        part_2(&input);

        Ok(())
    }
}

fn part_1(board: &Board) {
    let width = board.board.first().unwrap().len();
    let height = board.board.len();

    let left = &board.scan_left_right();
    let right = &board.scan_right_left();

    let rotated = board.clone().rotate();

    let top = &rotated.scan_left_right();
    let bottom = &rotated.scan_right_left();

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

    println!("interior count: {}", interior_count);
    println!("exterior count: {}", exterior_count);

    println!("part 1: {}", exterior_count + interior_count);
}

fn part_2(board: &Board) {
    let biggest = board
        .clone()
        .board
        .iter()
        .flatten()
        .map(|t| t.clone())
        .max_by(|a, b| board.viewable_from(a).cmp(&board.viewable_from(b)))
        .unwrap()
        .1;

    println!("part 2: {}", biggest);
}
