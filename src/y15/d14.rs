use anyhow::Result;

use crate::adventofcode::AdventOfCode;

const RACE_DURATION: u32 = 2503;

struct Deer {
    duration: u32,
    rest: u32,
    speed: u32,
}
// const DEER: [Deer; 2] = [
//     Deer {
//         speed: 14,
//         duration: 10,
//         rest: 127,
//     },
//     Deer {
//         speed: 16,
//         duration: 11,
//         rest: 162,
//     },
// ];

const DEER: [Deer; 9] = [
    Deer {
        speed: 22,
        duration: 8,
        rest: 165,
    },
    Deer {
        speed: 8,
        duration: 17,
        rest: 114,
    },
    Deer {
        speed: 18,
        duration: 6,
        rest: 103,
    },
    Deer {
        speed: 25,
        duration: 6,
        rest: 145,
    },
    Deer {
        speed: 11,
        duration: 12,
        rest: 125,
    },
    Deer {
        speed: 21,
        duration: 6,
        rest: 121,
    },
    Deer {
        speed: 18,
        duration: 3,
        rest: 50,
    },
    Deer {
        speed: 20,
        duration: 4,
        rest: 75,
    },
    Deer {
        speed: 7,
        duration: 20,
        rest: 119,
    },
];

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
        let mut race = Race::new();
        for _ in 0..RACE_DURATION {
            race.step();
        }

        let winner = race.positions.iter().max().unwrap();
        println!("{winner}");

        Ok(())
    }

    fn part2() -> Result<()> {
        let mut scores = [0; DEER.len()];

        let mut race = Race::new();
        for _ in 0..=RACE_DURATION {
            race.step();
            let furthest = race.positions.iter().max().unwrap();
            race.positions
                .iter()
                .enumerate()
                .filter(|(_, pos)| *pos == furthest)
                .map(|(deer, _)| deer)
                .for_each(|deer| {
                    scores[deer] += 1;
                });
        }

        println!("{}", scores.iter().max().unwrap());

        Ok(())
    }
}

#[derive(Debug, Clone, Copy)]
enum State {
    Moving(u32),
    Resting(u32),
}

struct Race {
    positions: [u32; DEER.len()],
    states: [State; DEER.len()],
}

impl Race {
    pub fn new() -> Self {
        let mut states = [State::Moving(0); DEER.len()];
        for (deer_idx, deer) in DEER.iter().enumerate() {
            states[deer_idx] = State::Moving(deer.duration);
        }

        Self {
            positions: [0; DEER.len()],
            states,
        }
    }
    pub fn step(&mut self) {
        self.states
            .iter_mut()
            .enumerate()
            .for_each(|(deer, state)| match state {
                State::Moving(remaining) => match remaining.checked_sub(1) {
                    Some(new) => {
                        self.positions[deer] += DEER[deer].speed;
                        *remaining = new;
                    }
                    // rest - 2 because we've waited at Moving(0) and we should start
                    // moving at Resting(0)
                    None => *state = State::Resting(DEER[deer].rest - 2),
                },
                State::Resting(remaining) => match remaining.checked_sub(1) {
                    Some(new) => *remaining = new,
                    None => *state = State::Moving(DEER[deer].duration),
                },
            });
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_step() {
        let mut race = Race::new();
        let mut scores = [0; DEER.len()];

        for _ in 0..=1000 {
            race.step();
            let (leader, _) = race
                .positions
                .iter()
                .enumerate()
                .max_by(|(_, a), (_, b)| a.cmp(b))
                .unwrap();
            scores[leader] += 1;
        }

        println!("{scores:?}");
        // assert_eq!(race.positions, [1120, 1056]);
    }
}
