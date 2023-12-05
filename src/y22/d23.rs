use core::panic;
use std::collections::{HashMap, HashSet};

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day23;

impl AdventOfCode for Day23 {
    fn solve() -> Result<()> {
        let input = Day23::input_lines(2022, 23)?;
        let mut sim = Simulation::from_input(input);

        for _ in 0..10 {
            sim.step();
        }

        Ok(())
    }
}

#[derive(Debug, Clone, Copy)]
enum Offset {
    A,
    B,
    C,
    D,
}

impl Offset {
    pub fn next(self) -> Offset {
        match self {
            Offset::A => Offset::B,
            Offset::B => Offset::C,
            Offset::C => Offset::D,
            Offset::D => Offset::A,
        }
    }
}

type Coord = (isize, isize);

struct Simulation {
    pub elfs: HashSet<Coord>,
    offset: Offset,
}

impl Simulation {
    pub fn from_input(input: Vec<String>) -> Simulation {
        let elfs = input
            .iter()
            .enumerate()
            .flat_map(|(y, line)| {
                line.chars()
                    .enumerate()
                    .map(move |(x, c)| ((y as isize, x as isize), c))
            })
            .filter(|(_, char)| char == &'#')
            .map(|(coord, _)| coord)
            .collect::<HashSet<Coord>>();

        Simulation {
            elfs,
            offset: Offset::A,
        }
    }

    pub fn step(&mut self) {
        let mut updates: HashMap<Coord, Vec<Coord>> = HashMap::new();
        // for each elf, look at neighbors and suggest movement.
        for coord in self.elfs.iter() {
            let (y, x) = coord;
            let direction = match SimCheck::get_move(self.offset, coord, &self.elfs) {
                Some(dir) => match dir {
                    Dir::N => (y - 1, *x),
                    Dir::S => (y + 1, *x),
                    Dir::W => (*y, x - 1),
                    Dir::E => (*y, x + 1),
                },
                None => *coord,
            };
            updates.entry(*coord).or_insert(vec![]).push(direction);
        }

        // if more than one suggestion for a location, copy all original locations to
        // original spots
        for (new_spot, originals) in updates {
            if originals.len() == 1 {
                let original = originals[0];
                self.elfs.remove(&original);
                self.elfs.insert(new_spot);
            }
        }

        // update first direction offset
        let next_offset = self.offset.next();
        self.offset = next_offset;
    }
}

struct SimCheck;
impl SimCheck {
    pub fn get_move(offset: Offset, coord: &Coord, elfs: &HashSet<Coord>) -> Option<Dir> {
        match offset {
            Offset::A => SimCheck::get_move_a(coord, elfs),
            Offset::B => SimCheck::get_move_b(coord, elfs),
            Offset::C => SimCheck::get_move_c(coord, elfs),
            Offset::D => SimCheck::get_move_d(coord, elfs),
        }
    }

    fn get_move_a(coord: &Coord, elfs: &HashSet<Coord>) -> Option<Dir> {
        if SimCheck::should_move_north(coord, elfs) {
            Some(Dir::N)
        } else if SimCheck::should_move_south(coord, elfs) {
            Some(Dir::S)
        } else if SimCheck::should_move_west(coord, elfs) {
            Some(Dir::W)
        } else if SimCheck::should_move_east(coord, elfs) {
            Some(Dir::E)
        } else {
            panic!("apparently this can happen?")
        }
    }

    fn get_move_b(coord: &Coord, elfs: &HashSet<Coord>) -> Option<Dir> {
        if SimCheck::should_move_south(coord, elfs) {
            Some(Dir::S)
        } else if SimCheck::should_move_west(coord, elfs) {
            Some(Dir::W)
        } else if SimCheck::should_move_east(coord, elfs) {
            Some(Dir::E)
        } else if SimCheck::should_move_north(coord, elfs) {
            Some(Dir::N)
        } else {
            panic!("apparently this can happen?")
        }
    }

    fn get_move_c(coord: &Coord, elfs: &HashSet<Coord>) -> Option<Dir> {
        if SimCheck::should_move_west(coord, elfs) {
            Some(Dir::W)
        } else if SimCheck::should_move_east(coord, elfs) {
            Some(Dir::E)
        } else if SimCheck::should_move_north(coord, elfs) {
            Some(Dir::N)
        } else if SimCheck::should_move_south(coord, elfs) {
            Some(Dir::S)
        } else {
            panic!("apparently this can happen?")
        }
    }

    fn get_move_d(coord: &Coord, elfs: &HashSet<Coord>) -> Option<Dir> {
        if SimCheck::should_move_east(coord, elfs) {
            Some(Dir::E)
        } else if SimCheck::should_move_north(coord, elfs) {
            Some(Dir::N)
        } else if SimCheck::should_move_south(coord, elfs) {
            Some(Dir::S)
        } else if SimCheck::should_move_west(coord, elfs) {
            Some(Dir::W)
        } else {
            panic!("apparently this can happen?")
        }
    }

    fn should_move_north(coord: &Coord, elfs: &HashSet<Coord>) -> bool {
        // If there is no Elf in the N, NE, or NW adjacent positions,
        // the Elf proposes moving north one step.
        let (y, x) = coord;
        SimCheck::should_move([(y - 1, x - 1), (y - 1, *x), (y - 1, x + 1)], elfs)
    }

    fn should_move_south(coord: &Coord, elfs: &HashSet<Coord>) -> bool {
        // If there is no Elf in the S, SE, or SW adjacent positions,
        // the Elf proposes moving south one step.
        let (y, x) = coord;
        SimCheck::should_move([(y - 1, x - 1), (y - 1, *x), (y - 1, x + 1)], elfs)
    }

    fn should_move_west(coord: &Coord, elfs: &HashSet<Coord>) -> bool {
        // If there is no Elf in the W, NW, or SW adjacent positions,
        // the Elf proposes moving west one step.
        let (y, x) = coord;
        SimCheck::should_move([(y + 1, x - 1), (*y, x - 1), (y - 1, x - 1)], elfs)
    }

    fn should_move_east(coord: &Coord, elfs: &HashSet<Coord>) -> bool {
        // If there is no Elf in the E, NE, or SE adjacent positions,
        // the Elf proposes moving east one step.
        let (y, x) = coord;
        SimCheck::should_move([(y + 1, x + 1), (*y, x + 1), (y - 1, x + 1)], elfs)
    }

    fn should_move(coords: [Coord; 3], elfs: &HashSet<Coord>) -> bool {
        coords
            .iter()
            .filter(|c| elfs.contains(*c))
            .collect::<Vec<_>>()
            .is_empty()
    }
}

#[derive(Debug)]
enum Dir {
    N,
    S,
    E,
    W,
}

#[test]
fn test_should_move_north() {
    let s = Simulation::from_input(vec![
        "###".to_string(), //
        ".#.".to_string(), //
        "...".to_string(), //
    ]);

    assert!(!SimCheck::should_move_north(&(1, 1), &s.elfs));

    let s = Simulation::from_input(vec![
        "#.#".to_string(), //
        ".#.".to_string(), //
        "...".to_string(), //
    ]);

    assert!(!SimCheck::should_move_north(&(1, 1), &s.elfs));

    let s = Simulation::from_input(vec![
        "..#".to_string(), //
        ".#.".to_string(), //
        "...".to_string(), //
    ]);

    assert!(!SimCheck::should_move_north(&(1, 1), &s.elfs));

    let s = Simulation::from_input(vec![
        "...".to_string(), //
        ".#.".to_string(), //
        "...".to_string(), //
    ]);

    assert!(SimCheck::should_move_north(&(1, 1), &s.elfs));
}
