use std::fmt::Display;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

#[derive(Debug, Clone, Copy, PartialEq)]
enum State {
    On,
    Off,
}

struct Board {
    height: usize,
    width: usize,
    states: Vec<State>,
}

impl Board {
    fn from_lines(lines: &[String]) -> Self {
        let height = lines.len();
        let width = lines[0].len();

        let mut states = vec![State::On; height * width];
        let mut idx = 0;
        for line in lines {
            for s in line.chars() {
                let state = match s {
                    '#' => State::On,
                    _ => State::Off,
                };
                states[idx] = state;
                idx += 1;
            }
        }

        Self {
            height,
            width,
            states,
        }
    }

    fn step(&mut self) {
        let next_board = self
            .states
            .clone()
            .into_iter()
            .enumerate()
            .map(|(idx, state)| {
                let neighbors = self.num_neighbors(idx);

                // The state a light should have next is based on its current state
                // (on or off) plus the number of neighbors that are on:
                // A light which is on stays on when 2 or 3 neighbors are on, and turns
                // off otherwise.
                // A light which is off turns on if exactly 3 neighbors are on, and
                // stays off otherwise.
                match state {
                    State::On if neighbors != 2 && neighbors != 3 => State::Off,
                    State::Off if neighbors == 3 => State::On,
                    State::On => State::On,
                    State::Off => State::Off,
                }
            })
            .collect();

        self.states = next_board;
    }

    fn num_neighbors(&mut self, idx: usize) -> u8 {
        let up = idx.checked_sub(self.width);
        let down = idx + self.width;
        let down = if down >= self.states.len() {
            None
        } else {
            Some(down)
        };
        let left = if idx % self.width != 0 {
            Some(idx - 1)
        } else {
            None
        };
        let right = if (idx + 1) % self.width != 0 {
            Some(idx + 1)
        } else {
            None
        };
        let up_left = if up.is_some() && left.is_some() {
            Some(idx - self.width - 1)
        } else {
            None
        };
        let up_right = if up.is_some() && right.is_some() {
            Some(idx - self.width + 1)
        } else {
            None
        };
        let down_left = if down.is_some() && left.is_some() {
            Some(idx + self.width - 1)
        } else {
            None
        };
        let down_right = if down.is_some() && right.is_some() {
            Some(idx + self.width + 1)
        } else {
            None
        };

        vec![
            up, down, left, right, up_left, up_right, down_left, down_right,
        ]
        .into_iter()
        .flatten()
        .filter(|idx| self.get(idx) == State::On)
        .count() as u8
    }

    fn get(&self, idx: &usize) -> State {
        self.states[*idx]
    }
}

impl Display for Board {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        for row in 0..self.height {
            let offset = self.width * row;
            let mut line = String::new();
            for state in &self.states[offset..offset + self.width] {
                match state {
                    State::On => line.push('#'),
                    State::Off => line.push('.'),
                }
            }
            writeln!(f, "{line}")?;
        }
        Ok(())
    }
}

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
        let mut board = Board::from_lines(&Self::input_lines(2015, 18)?);
        for _ in 0..100 {
            board.step();
        }

        let lights_on = board
            .states
            .into_iter()
            .filter(|state| *state == State::On)
            .count();
        println!("{lights_on}");

        Ok(())
    }

    fn part2() -> Result<()> {
        Ok(())
    }
}

#[cfg(test)]
mod tests {
    use super::Board;

    #[test]
    fn test_foo() {
        let lines = vec![
            ".#.#.#".to_string(),
            "...##.".to_string(),
            "#....#".to_string(),
            "..#...".to_string(),
            "#.#..#".to_string(),
            "####..".to_string(),
        ];

        let mut board = Board::from_lines(&lines);

        assert_eq!(board.num_neighbors(0), 1);
        assert_eq!(board.num_neighbors(1), 0);
        assert_eq!(board.num_neighbors(2), 3);
        assert_eq!(board.num_neighbors(3), 2);
        assert_eq!(board.num_neighbors(4), 4);
        assert_eq!(board.num_neighbors(5), 1);
        assert_eq!(board.num_neighbors(6), 2);

        println!("{board}");
        board.step();
        println!("{board}");
        board.step();
        println!("{board}");
        board.step();
        println!("{board}");
        board.step();
        println!("{board}");
    }
}
