use std::{
    collections::{HashMap, HashSet},
    hash::Hash,
};

use itertools::Either;
use once_cell::sync::Lazy;
use regex::Regex;

use crate::adventofcode::AdventOfCode;

static ASSIGN: Lazy<Regex> = Lazy::new(|| regex::Regex::new(r"^(\d+|\w+) -> (.+)").unwrap());
static AND: Lazy<Regex> = Lazy::new(|| regex::Regex::new(r"(.+) AND (.+) -> (.+)").unwrap());
static OR: Lazy<Regex> = Lazy::new(|| regex::Regex::new(r"(.+) OR (.+) -> (.+)").unwrap());
static NOT: Lazy<Regex> = Lazy::new(|| regex::Regex::new(r"NOT (.+) -> (.+)").unwrap());
static LSHIFT: Lazy<Regex> = Lazy::new(|| regex::Regex::new(r"(.+) LSHIFT (\d+) -> (.+)").unwrap());
static RSHIFT: Lazy<Regex> = Lazy::new(|| regex::Regex::new(r"(.+) RSHIFT (\d+) -> (.+)").unwrap());

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let ops = Self::parse_input(Day::input_lines(2015, 7)?);

        let registers = Self::run_program(ops.clone(), HashMap::new());
        let part_1 = registers[&"a".to_string()];
        println!("{part_1}");

        let registers = Self::run_program(ops, HashMap::from([("b".to_string(), part_1)]));
        let part_2 = registers[&"a".to_string()];
        println!("{part_2}");

        Ok(())
    }
}

impl Day {
    fn parse_input(input: Vec<String>) -> Vec<Op> {
        input.into_iter().map(Op::parse).collect()
    }

    fn run_program(ops: Vec<Op>, init: HashMap<String, u16>) -> HashMap<String, u16> {
        let mut registers = init;
        let mut solved = HashSet::new();

        while solved.len() != ops.len() {
            for op in &ops {
                if solved.contains(op) {
                    continue;
                }
                match op {
                    Op::Assign(a, b) => match a {
                        Either::Left(a) => {
                            if let Some(a) = registers.get(a) {
                                registers.insert(b.to_owned(), *a);
                                solved.insert(op);
                            }
                        }
                        Either::Right(a) => {
                            registers.insert(b.to_owned(), *a);
                            solved.insert(op);
                        }
                    },
                    Op::And(a, b, c) => {
                        let a = unwrap_either(&registers, a);
                        let b = unwrap_either(&registers, b);
                        if let (Some(a), Some(b)) = (a, b) {
                            registers.insert(c.to_owned(), a & b);
                            solved.insert(op);
                        }
                    }
                    Op::Or(a, b, c) => {
                        let a = unwrap_either(&registers, a);
                        let b = unwrap_either(&registers, b);
                        if let (Some(a), Some(b)) = (a, b) {
                            registers.insert(c.to_owned(), a | b);
                            solved.insert(op);
                        }
                    }
                    Op::Not(a, b) => {
                        if let Some(a) = unwrap_either(&registers, a) {
                            registers.insert(b.to_owned(), !a);
                            solved.insert(op);
                        }
                    }
                    Op::Lshift(a, b, c) => {
                        if let Some(a) = unwrap_either(&registers, a) {
                            registers.insert(c.to_owned(), a << b);
                            solved.insert(op);
                        }
                    }
                    Op::Rshift(a, b, c) => {
                        if let Some(a) = unwrap_either(&registers, a) {
                            registers.insert(c.to_owned(), a >> b);
                            solved.insert(op);
                        }
                    }
                }
            }
        }

        registers
    }
}

fn unwrap_either(registers: &HashMap<String, u16>, e: &Either<String, u16>) -> Option<u16> {
    match e {
        Either::Left(a) => registers.get(a).copied(),
        Either::Right(a) => Some(*a),
    }
}

#[derive(Debug, Clone, Hash, PartialEq, Eq)]
enum Op {
    Assign(Either<String, u16>, String),
    And(Either<String, u16>, Either<String, u16>, String),
    Or(Either<String, u16>, Either<String, u16>, String),
    Lshift(Either<String, u16>, u16, String),
    Rshift(Either<String, u16>, u16, String),
    Not(Either<String, u16>, String),
}

impl Op {
    pub fn parse(line: String) -> Op {
        Self::parse_assign(&line)
            .or(Self::parse_and(&line))
            .or(Self::parse_lshift(&line))
            .or(Self::parse_not(&line))
            .or(Self::parse_or(&line))
            .or(Self::parse_rshift(&line))
            .unwrap()
    }

    fn parse_assign(line: &str) -> Option<Op> {
        ASSIGN.captures(line).map(|caps| {
            let a = parse_str_or_int(caps.get(1).unwrap().as_str());
            let b = caps.get(2).unwrap().as_str().to_string();
            Op::Assign(a, b)
        })
    }

    fn parse_and(line: &str) -> Option<Op> {
        AND.captures(line).map(|caps| {
            let a = parse_str_or_int(caps.get(1).unwrap().as_str());
            let b = parse_str_or_int(caps.get(2).unwrap().as_str());
            let c = caps.get(3).unwrap().as_str().to_string();
            Op::And(a, b, c)
        })
    }

    fn parse_or(line: &str) -> Option<Op> {
        OR.captures(line).map(|caps| {
            let a = parse_str_or_int(caps.get(1).unwrap().as_str());
            let b = parse_str_or_int(caps.get(2).unwrap().as_str());
            let c = caps.get(3).unwrap().as_str().to_string();
            Op::Or(a, b, c)
        })
    }

    fn parse_not(line: &str) -> Option<Op> {
        NOT.captures(line).map(|caps| {
            let a = parse_str_or_int(caps.get(1).unwrap().as_str());
            let b = caps.get(2).unwrap().as_str().to_string();
            Op::Not(a, b)
        })
    }

    fn parse_lshift(line: &str) -> Option<Op> {
        LSHIFT.captures(line).map(|caps| {
            let a = parse_str_or_int(caps.get(1).unwrap().as_str());
            let b = caps.get(2).unwrap().as_str().parse::<u16>().unwrap();
            let c = caps.get(3).unwrap().as_str().to_string();
            Op::Lshift(a, b, c)
        })
    }

    fn parse_rshift(line: &str) -> Option<Op> {
        RSHIFT.captures(line).map(|caps| {
            let a = parse_str_or_int(caps.get(1).unwrap().as_str());
            let b = caps.get(2).unwrap().as_str().parse::<u16>().unwrap();
            let c = caps.get(3).unwrap().as_str().to_string();
            Op::Rshift(a, b, c)
        })
    }
}

fn parse_str_or_int(a: &str) -> Either<String, u16> {
    a.parse::<u16>()
        .map(Either::Right)
        .unwrap_or(Either::Left(a.to_string()))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_parse() {
        let raw = vec![
            "NOT y -> i".to_string(),
            "NOT x -> h".to_string(),
            "y RSHIFT 2 -> g".to_string(),
            "x LSHIFT 2 -> f".to_string(),
            "x OR y -> e".to_string(),
            "x AND y -> d".to_string(),
            "456 -> y".to_string(),
            "123 -> x".to_string(),
        ];

        // let mut day = Day::new();
        // let program = day.generate_program(raw);
        let expected = HashMap::from([
            ("d".to_string(), 72),
            ("e".to_string(), 507),
            ("f".to_string(), 492),
            ("g".to_string(), 114),
            ("h".to_string(), 65412),
            ("i".to_string(), 65079),
            ("x".to_string(), 123),
            ("y".to_string(), 456),
        ]);

        let ops = Day::parse_input(raw);
        let registers = Day::run_program(ops, HashMap::new());
        assert_eq!(registers, expected);
    }
}
