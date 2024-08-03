use std::collections::HashMap;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        Self::part1()?;
        Self::part2()?;

        Ok(())
    }
}

impl Day {
    fn part1() -> Result<()> {
        let mut program = Program::new(&Self::input_lines(2015, 23)?, HashMap::new());
        program.run();
        println!("{:#?}", program.registers);
        Ok(())
    }

    fn part2() -> Result<()> {
        let mut program = Program::new(&Self::input_lines(2015, 23)?, HashMap::from([('a', 1)]));
        program.run();
        println!("{:#?}", program.registers);
        Ok(())
    }
}

#[derive(Debug)]
enum Instruction {
    Hlf(char),
    Tpl(char),
    Inc(char),
    Jmp(isize),
    Jie(char, isize),
    Jio(char, isize),
}

struct Program {
    instructions: Vec<Instruction>,
    pos: usize,
    registers: HashMap<char, i64>,
}

impl Program {
    fn new(lines: &[String], registers: HashMap<char, i64>) -> Self {
        let mut instructions = Vec::with_capacity(lines.len());
        for line in lines {
            let (inst, rest) = line.split_at(3);
            let rest = rest.trim();
            let instruction = match inst {
                "hlf" => Instruction::Hlf(rest.chars().next().unwrap()),
                "tpl" => Instruction::Tpl(rest.chars().next().unwrap()),
                "inc" => Instruction::Inc(rest.chars().next().unwrap()),
                "jmp" => Instruction::Jmp(rest.parse().unwrap()),
                _ => {
                    let (reg, amt) = rest.split_once(", ").unwrap();
                    let reg = reg.chars().next().unwrap();
                    match inst {
                        "jie" => Instruction::Jie(reg, amt.parse().unwrap()),
                        "jio" => Instruction::Jio(reg, amt.parse().unwrap()),
                        _ => unimplemented!("wahh: {inst}"),
                    }
                }
            };

            instructions.push(instruction);
        }

        Self {
            instructions,
            pos: 0,
            registers,
        }
    }

    fn run(&mut self) {
        while self.pos < self.instructions.len() {
            match self.instructions[self.pos] {
                Instruction::Hlf(reg) => {
                    *self.registers.get_mut(&reg).unwrap() /= 2;
                    self.pos += 1;
                }
                Instruction::Tpl(reg) => {
                    *self.registers.get_mut(&reg).unwrap() *= 3;
                    self.pos += 1;
                }
                Instruction::Inc(reg) => {
                    self.registers
                        .entry(reg)
                        .and_modify(|n| *n += 1)
                        .or_insert(1);
                    self.pos += 1;
                }
                Instruction::Jmp(amt) => {
                    match self.pos.checked_add_signed(amt) {
                        Some(next_pos) => self.pos = next_pos,
                        None => self.pos = usize::MAX,
                    };
                }
                Instruction::Jie(reg, amt) => match self.registers.get(&reg) {
                    Some(value) if *value % 2 == 0 => {
                        match self.pos.checked_add_signed(amt) {
                            Some(next_pos) => self.pos = next_pos,
                            None => self.pos = usize::MAX,
                        };
                    }
                    _ => {
                        self.pos += 1;
                    }
                },
                Instruction::Jio(reg, amt) => match self.registers.get(&reg) {
                    Some(value) if *value == 1 => {
                        match self.pos.checked_add_signed(amt) {
                            Some(next_pos) => self.pos = next_pos,
                            None => self.pos = usize::MAX,
                        };
                    }
                    _ => {
                        self.pos += 1;
                    }
                },
            }
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn parse() {
        let lines = vec![
            "inc a".to_string(),
            "jio a, +2".to_string(),
            "tpl a".to_string(),
            "inc a".to_string(),
        ];

        let mut program = Program::new(&lines, HashMap::new());
        program.run();
        println!("{:#?}", program.registers);
    }
}
