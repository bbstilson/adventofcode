use std::collections::HashSet;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day10;

impl AdventOfCode for Day10 {
    fn solve() -> Result<()> {
        let _program = Day10::input_lines(2022, 10)?
            .iter()
            .map(|line| {
                let split = line.split_whitespace().collect::<Vec<_>>();
                match split[0] {
                    "addx" => {
                        let value = split[1].parse::<i32>().expect("wuh woh");
                        Instruction::Addx(value)
                    }
                    _ => Instruction::Noop,
                }
            })
            .collect::<Vec<_>>();

        let mut cpu = CPU::new(_program);
        // let mut cpu = CPU::new(sample::PROGRAM.to_vec());
        part1(&mut cpu);
        cpu.restart();
        part2(&mut cpu);

        Ok(())
    }
}

fn part1(cpu: &mut CPU) {
    let checkins = HashSet::from([20, 60, 100, 140, 180, 220]);
    let mut signals = vec![];
    for cycle in 1..=220 {
        if checkins.contains(&cycle) {
            let v = cpu.get_register_value();
            signals.push(v * cycle);
        }
        cpu.tick();
    }

    println!("part 1: {}", signals.iter().sum::<i32>());
}

fn part2(cpu: &mut CPU) {
    let mut screen: [char; 240] = ['.'; 240];
    let screen_width = 40;

    for cycle in 1..=240 {
        cpu.tick();
        let sprite_position = cpu.get_register_value() as usize;
        let offset = cycle % screen_width;
        if offset == sprite_position - 1
            || offset == sprite_position
            || offset == sprite_position + 1
        {
            screen[cycle - 1] = '#';
        }
    }

    for y in 0..6 {
        for x in 0..screen_width {
            let idx = (y * screen_width) + x;
            print!("{}", screen[idx]);
        }
        println!();
    }
}

#[derive(Debug, Clone, Copy)]
pub enum Instruction {
    Noop,
    Addx(i32),
}

pub struct CPU {
    register: i32,
    program: Vec<Instruction>,
    idx: usize,
    cache: Option<Instruction>,
}

impl CPU {
    pub fn new(program: Vec<Instruction>) -> CPU {
        CPU {
            program,
            idx: 0,
            register: 1,
            cache: None,
        }
    }

    pub fn restart(&mut self) {
        self.idx = 0;
        self.cache = None;
        self.register = 1;
    }

    pub fn get_register_value(&self) -> i32 {
        self.register
    }

    pub fn tick(&mut self) {
        if let Some(inst) = self.cache {
            match inst {
                Instruction::Addx(value) => self.register += value,
                Instruction::Noop => {}
            }
            // clear cache and go to next instruction
            self.cache = None;
            self.idx += 1;
        } else {
            let inst = self.program[self.idx];
            match inst {
                Instruction::Addx(_) => self.cache = Some(inst),
                Instruction::Noop => self.idx += 1,
            }
        }
    }
}

impl std::fmt::Debug for CPU {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.debug_struct("CPU")
            .field("register", &self.register)
            .field("idx", &self.idx)
            .field("cache", &self.cache)
            .finish()
    }
}

mod sample {
    use super::Instruction;

    #[allow(dead_code)]
    pub const PROGRAM: [Instruction; 146] = [
        Instruction::Addx(15),
        Instruction::Addx(-11),
        Instruction::Addx(6),
        Instruction::Addx(-3),
        Instruction::Addx(5),
        Instruction::Addx(-1),
        Instruction::Addx(-8),
        Instruction::Addx(13),
        Instruction::Addx(4),
        Instruction::Noop,
        Instruction::Addx(-1),
        Instruction::Addx(5),
        Instruction::Addx(-1),
        Instruction::Addx(5),
        Instruction::Addx(-1),
        Instruction::Addx(5),
        Instruction::Addx(-1),
        Instruction::Addx(5),
        Instruction::Addx(-1),
        Instruction::Addx(-35),
        Instruction::Addx(1),
        Instruction::Addx(24),
        Instruction::Addx(-19),
        Instruction::Addx(1),
        Instruction::Addx(16),
        Instruction::Addx(-11),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(21),
        Instruction::Addx(-15),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(-3),
        Instruction::Addx(9),
        Instruction::Addx(1),
        Instruction::Addx(-3),
        Instruction::Addx(8),
        Instruction::Addx(1),
        Instruction::Addx(5),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(-36),
        Instruction::Noop,
        Instruction::Addx(1),
        Instruction::Addx(7),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(2),
        Instruction::Addx(6),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(1),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(7),
        Instruction::Addx(1),
        Instruction::Noop,
        Instruction::Addx(-13),
        Instruction::Addx(13),
        Instruction::Addx(7),
        Instruction::Noop,
        Instruction::Addx(1),
        Instruction::Addx(-33),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(2),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(8),
        Instruction::Noop,
        Instruction::Addx(-1),
        Instruction::Addx(2),
        Instruction::Addx(1),
        Instruction::Noop,
        Instruction::Addx(17),
        Instruction::Addx(-9),
        Instruction::Addx(1),
        Instruction::Addx(1),
        Instruction::Addx(-3),
        Instruction::Addx(11),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(1),
        Instruction::Noop,
        Instruction::Addx(1),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(-13),
        Instruction::Addx(-19),
        Instruction::Addx(1),
        Instruction::Addx(3),
        Instruction::Addx(26),
        Instruction::Addx(-30),
        Instruction::Addx(12),
        Instruction::Addx(-1),
        Instruction::Addx(3),
        Instruction::Addx(1),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(-9),
        Instruction::Addx(18),
        Instruction::Addx(1),
        Instruction::Addx(2),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(9),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(-1),
        Instruction::Addx(2),
        Instruction::Addx(-37),
        Instruction::Addx(1),
        Instruction::Addx(3),
        Instruction::Noop,
        Instruction::Addx(15),
        Instruction::Addx(-21),
        Instruction::Addx(22),
        Instruction::Addx(-6),
        Instruction::Addx(1),
        Instruction::Noop,
        Instruction::Addx(2),
        Instruction::Addx(1),
        Instruction::Noop,
        Instruction::Addx(-10),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Addx(20),
        Instruction::Addx(1),
        Instruction::Addx(2),
        Instruction::Addx(2),
        Instruction::Addx(-6),
        Instruction::Addx(-11),
        Instruction::Noop,
        Instruction::Noop,
        Instruction::Noop,
    ];
}
