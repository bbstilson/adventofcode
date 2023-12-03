use anyhow::Result;
use once_cell::sync::Lazy;
use regex::Regex;

use crate::adventofcode::AdventOfCode;

static SYMBOL_REGEX: Lazy<Regex> = Lazy::new(|| Regex::new(r"([^a-zA-Z0-9\.])").unwrap());
static NUMBERS_REGEX: Lazy<Regex> = Lazy::new(|| Regex::new(r"(\d+)").unwrap());

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let input = Day::input_lines(2023, 3)?;
        let schematic = parse_schematic(input)?;

        println!("{}", part_1(&schematic));
        println!("{}", part_2(&schematic));

        Ok(())
    }
}

#[derive(Debug)]
struct Schematic {
    symbols: Vec<Symbol>,
    numbers: Vec<Number>,
}

#[derive(Debug)]
struct Symbol {
    c: char,
    pos: (isize, isize),
}

#[derive(Debug)]
struct Number {
    n: usize,
    pos: (isize, std::ops::Range<usize>),
}

fn parse_schematic(lines: Vec<String>) -> Result<Schematic> {
    // get 2d index of symbols (row, col)
    // get 2d range of numbers. (row, range)

    let mut symbols = vec![];
    let mut numbers = vec![];

    for (row_idx, line) in lines.into_iter().enumerate() {
        for cap in SYMBOL_REGEX.captures_iter(&line) {
            let re_match = cap.get(0).unwrap();
            symbols.push(Symbol {
                c: re_match.as_str().chars().nth(0).unwrap(),
                pos: (row_idx as isize, re_match.range().start as isize),
            });
        }

        for cap in NUMBERS_REGEX.captures_iter(&line) {
            let re_match = cap.get(0).unwrap();
            numbers.push(Number {
                n: re_match.as_str().parse::<usize>()?,
                pos: (row_idx as isize, re_match.range()),
            });
        }
    }

    Ok(Schematic { symbols, numbers })
}

fn part_1(schematic: &Schematic) -> usize {
    // for each symbol, find any numbers on the same row or +- 1 row away.
    // for each found number, check if the range is +-1 away from the symbol column
    schematic
        .symbols
        .iter()
        .map(|symbol| get_adjacent_numbers(symbol, &schematic.numbers).sum::<usize>())
        .sum()
}

fn part_2(schematic: &Schematic) -> usize {
    // find all gears (symbols == '*')
    // for each gear, find any numbers on the same row or +- 1 row away.
    // if there are exactly 2 numbers, get their product
    schematic
        .symbols
        .iter()
        .filter(|symbol| symbol.c == '*')
        .filter_map(|symbol| {
            let gears = get_adjacent_numbers(symbol, &schematic.numbers).collect::<Vec<_>>();

            if gears.len() == 2 {
                Some(gears.iter().product::<usize>())
            } else {
                None
            }
        })
        .sum()
}

fn get_adjacent_numbers<'a>(
    symbol: &'a Symbol,
    numbers: &'a Vec<Number>,
) -> impl Iterator<Item = usize> + 'a {
    numbers
        .iter()
        .filter(|number| {
            // get numbers that are:
            // a) on the same row, or
            // b) one row up or down (for diagonal checks)
            let number_row = number.pos.0;
            let symbol_row = symbol.pos.0;
            number_row == symbol_row || number_row - 1 == symbol_row || number_row + 1 == symbol_row
        })
        .filter(|number| {
            // check if the symbol position is overlapping the number range
            // accounting for diagonals (+-1)
            let number_range = &number.pos.1;
            let symbol_col = symbol.pos.1;
            let left_shift = (symbol_col - 1) as usize;
            let right_shift = (symbol_col + 1) as usize;
            number_range.contains(&right_shift)
                || number_range.contains(&left_shift)
                || number_range.contains(&(symbol_col as usize))
        })
        .map(|number| number.n)
}
