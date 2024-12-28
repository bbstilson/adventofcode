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
        let input = Day::input_lines(2015, 6)?;
        let instructions = input.into_iter().map(parse_instruction).collect::<Vec<_>>();
        // todo: bitvec
        let mut bulbs = [0u32; 1_000_000];

        // todo: rayon this bitch
        for bulb in 0..bulbs.len() {
            for (action, range) in &instructions {
                if range.contains(bulb) {
                    match (bulbs[bulb], action) {
                        (_, Action::On) => bulbs[bulb] = 1,
                        (_, Action::Off) => bulbs[bulb] = 0,
                        (1, Action::Toggle) => bulbs[bulb] = 0,
                        (0, Action::Toggle) => bulbs[bulb] = 1,
                        _ => unimplemented!("wahhhh"),
                    }
                }
            }
        }

        let num_lit: u32 = bulbs.to_vec().into_iter().sum();

        println!("{num_lit}");

        Ok(())
    }

    fn part2() -> Result<()> {
        let input = Day::input_lines(2015, 6)?;
        let instructions = input.into_iter().map(parse_instruction).collect::<Vec<_>>();

        let mut bulbs = [0u64; 1_000_000];

        // todo: rayon this bitch
        for bulb in 0..bulbs.len() {
            for (action, range) in &instructions {
                if range.contains(bulb) {
                    // The phrase turn on actually means that you should increase the
                    // brightness of those lights by 1.
                    // The phrase turn off actually means that you should decrease the
                    // brightness of those lights by 1, to a minimum of zero.
                    // The phrase toggle actually means that you should increase the
                    // brightness of those lights by 2.
                    match action {
                        Action::On => bulbs[bulb] += 1,
                        Action::Off => {
                            let cur = bulbs[bulb];
                            if cur > 0 {
                                bulbs[bulb] = cur - 1;
                            }
                        }
                        Action::Toggle => bulbs[bulb] += 2,
                    }
                }
            }
        }

        // What is the total brightness of all lights combined after following
        // Santa's instructions?
        let num_lit: u64 = bulbs.to_vec().into_iter().sum();

        println!("{num_lit}");

        Ok(())
    }
}

#[derive(Debug, PartialEq)]
enum Action {
    On,
    Off,
    Toggle,
}

#[derive(Debug, PartialEq)]
struct Range {
    tl: (u32, u32),
    br: (u32, u32),
}

impl Range {
    pub fn contains(&self, bulb: usize) -> bool {
        // convert bulb to idx
        // warning: flipped indices?
        let y = bulb as u32 % 1000;
        let x = bulb as u32 / 1000;
        self.tl.0 <= x && self.tl.1 <= y && self.br.0 >= x && self.br.1 >= y
    }
}

fn parse_instruction(l: String) -> (Action, Range) {
    let toks = l.split(' ').collect::<Vec<_>>();
    let action = match (toks[0], toks[1]) {
        ("turn", "on") => Action::On,
        ("turn", "off") => Action::Off,
        ("toggle", _) => Action::Toggle,
        _ => unimplemented!("wah"),
    };

    let tl = match action {
        Action::Toggle => toks[1],
        _ => toks[2],
    };
    let br = match action {
        Action::Toggle => toks[3],
        _ => toks[4],
    };
    let (tl_a, tl_b) = tl.split_once(',').unwrap();
    let (br_a, br_b) = br.split_once(',').unwrap();

    let range = Range {
        tl: (tl_a.parse().unwrap(), tl_b.parse().unwrap()),
        br: (br_a.parse().unwrap(), br_b.parse().unwrap()),
    };

    (action, range)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn something() {
        let lines = vec![
            "turn on 489,959 through 759,964".to_string(),
            "turn off 820,516 through 871,914".to_string(),
            "toggle 756,965 through 812,992".to_string(),
        ];

        let expected = vec![
            (
                Action::On,
                Range {
                    tl: (489, 959),
                    br: (759, 964),
                },
            ),
            (
                Action::Off,
                Range {
                    tl: (820, 516),
                    br: (871, 914),
                },
            ),
            (
                Action::Toggle,
                Range {
                    tl: (756, 965),
                    br: (812, 992),
                },
            ),
        ];

        let parsed = lines.into_iter().map(parse_instruction).collect::<Vec<_>>();
        assert_eq!(parsed, expected);
    }
}
