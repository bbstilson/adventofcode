use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day11;

impl AdventOfCode for Day11 {
    fn solve() -> Result<()> {
        // part(1, 20);
        for n in [1, 20, 1000, 2000, 10000] {
            part(n, n);
            println!();
        }

        Ok(())
    }
}

pub fn part(n: i32, rounds: i32) {
    let mut game = input::example();
    // let mut game = input::real();

    for _round in 0..rounds {
        game.play();
        // println!();
    }

    let mut monkeys: Vec<i64> = game.monkeys.iter().map(|m| m.inspected as i64).collect();
    monkeys.sort();
    monkeys.reverse();
    println!("{:?}", monkeys);
    let result: i64 = monkeys.iter().take(2).product();
    println!("part {}: {}", n, result);
}

pub struct Game {
    pub monkeys: Vec<Monkey>,
}

impl Game {
    pub fn new(monkeys: Vec<Monkey>) -> Game {
        Game { monkeys }
    }

    pub fn play(&mut self) {
        fn helper(monkeys: &mut Vec<Monkey>, id: usize) {
            if id < monkeys.len() {
                let monkey = &mut monkeys[id];
                for (id, item) in monkey.play() {
                    monkeys[id].receive(item);
                }
                helper(monkeys, id + 1);
            }
        }

        helper(&mut self.monkeys, 0);
    }

    pub fn print(&mut self) {
        for monkey in &mut self.monkeys {
            monkey.print();
        }
    }
}

#[derive(Debug, Clone)]
pub struct Monkey {
    pub id: usize,
    pub inspected: u32,
    items: Vec<i64>,
    inspect: fn(i64) -> i64,
    test: fn(i64) -> usize,
}

impl Monkey {
    pub fn new(
        id: usize,
        items: Vec<i64>,
        inspect: fn(i64) -> i64,
        test: fn(i64) -> usize,
    ) -> Monkey {
        Monkey {
            id,
            items,
            inspect,
            test,
            inspected: 0,
        }
    }

    pub fn receive(&mut self, item: i64) {
        self.items.push(item);
    }

    pub fn play(&mut self) -> Vec<(usize, i64)> {
        let items = self.items.clone();
        self.inspected += items.len() as u32;
        self.items.clear();

        items
            .into_iter()
            .map(|item| {
                // let next_item = (self.inspect)(item) / 3; // part 1
                let next_item = (self.inspect)(item); // part 2
                ((self.test)(next_item), next_item)
            })
            .collect()
    }

    pub fn print(&mut self) {
        println!("Monkey {}: {:?}", self.id, self.items);
    }
}

mod input {
    use super::{Game, Monkey};

    pub fn real() -> Game {
        let m0 = Monkey::new(
            0,
            vec![84, 66, 62, 69, 88, 91, 91],
            |x| x * 11,
            |x| {
                if x % 2 == 0 {
                    4
                } else {
                    7
                }
            },
        );
        let m1 = Monkey::new(
            1,
            vec![98, 50, 76, 99],
            |x| x * x,
            |x| {
                if x % 7 == 0 {
                    3
                } else {
                    6
                }
            },
        );
        let m2 = Monkey::new(
            2,
            vec![72, 56, 94],
            |x| x + 1,
            |x| {
                if x % 13 == 0 {
                    4
                } else {
                    0
                }
            },
        );
        let m3 = Monkey::new(
            3,
            vec![55, 88, 90, 77, 60, 67],
            |x| x + 2,
            |x| {
                if x % 3 == 0 {
                    6
                } else {
                    5
                }
            },
        );
        let m4 = Monkey::new(
            4,
            vec![69, 72, 63, 60, 72, 52, 63, 78],
            |x| x * 13,
            |x| {
                if x % 19 == 0 {
                    1
                } else {
                    7
                }
            },
        );
        let m5 = Monkey::new(
            5,
            vec![89, 73],
            |x| x + 5,
            |x| {
                if x % 17 == 0 {
                    2
                } else {
                    0
                }
            },
        );
        let m6 = Monkey::new(
            6,
            vec![78, 68, 98, 88, 66],
            |x| x + 6,
            |x| {
                if x % 11 == 0 {
                    2
                } else {
                    5
                }
            },
        );
        let m7 = Monkey::new(
            7,
            vec![70],
            |x| x + 7,
            |x| {
                if x % 5 == 0 {
                    1
                } else {
                    3
                }
            },
        );

        Game::new(vec![m0, m1, m2, m3, m4, m5, m6, m7])
    }

    pub fn example() -> Game {
        let m0 = Monkey::new(
            0,
            vec![79, 98],
            |x| x * 19,
            |x| {
                if x % 23 == 0 {
                    2
                } else {
                    3
                }
            },
        );
        let m1 = Monkey::new(
            1,
            vec![54, 65, 75, 74],
            |x| x + 6,
            |x| {
                if x % 19 == 0 {
                    2
                } else {
                    0
                }
            },
        );
        let m2 = Monkey::new(
            2,
            vec![79, 60, 97],
            |x| x * x,
            |x| {
                if x % 13 == 0 {
                    1
                } else {
                    3
                }
            },
        );
        let m3 = Monkey::new(
            3,
            vec![74],
            |x| x + 3,
            |x| {
                if x % 17 == 0 {
                    0
                } else {
                    1
                }
            },
        );

        Game::new(vec![m0, m1, m2, m3])
    }
}
