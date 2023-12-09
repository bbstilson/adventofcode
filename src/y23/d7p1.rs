use core::panic;

use anyhow::Result;
use itertools::Itertools;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let input = Day::input_lines(2023, 7)?;

        let hands = input
            .into_iter()
            .filter_map(|l| parse_hand(&l).ok())
            .collect::<Vec<_>>();

        println!("{}", part_1(&hands));

        Ok(())
    }
}

fn part_1(hands: &[Hand]) -> usize {
    hands
        .iter()
        .sorted_by(|a, b| {
            if a.hand_type == b.hand_type {
                let (a, b) = a.hand.iter().zip(&b.hand).find(|(a, b)| a != b).unwrap();

                if a > b {
                    std::cmp::Ordering::Greater
                } else {
                    std::cmp::Ordering::Less
                }
            } else {
                a.hand_type.cmp(&b.hand_type)
            }
        })
        .enumerate()
        .map(|(rank, hand)| hand.bid * (rank + 1))
        .sum()
}

#[derive(Debug)]
struct Hand {
    hand: Vec<usize>,
    bid: usize,
    hand_type: HandType,
}

#[derive(Debug, PartialEq, Eq, PartialOrd, Ord)]
enum HandType {
    HighCard,
    OnePair,
    TwoPair,
    ThreeKind,
    FullHouse,
    FourKind,
    FiveKind,
}

#[cfg(test)]
mod tests {
    use super::HandType;

    #[test]
    fn hand_type_from_vec() {
        let tests = vec![
            (vec![2, 2, 2, 2, 2], HandType::FiveKind),
            (vec![2, 2, 2, 2, 3], HandType::FourKind),
            (vec![2, 2, 2, 3, 3], HandType::FullHouse),
            (vec![2, 2, 2, 3, 4], HandType::ThreeKind),
            (vec![2, 2, 3, 3, 4], HandType::TwoPair),
            (vec![2, 2, 3, 4, 5], HandType::OnePair),
            (vec![2, 3, 4, 5, 6], HandType::HighCard),
        ];

        for (ns, expected) in tests {
            assert_eq!(HandType::from_vec(&ns), expected);
        }
    }

    #[test]
    fn ordering() {
        assert!(HandType::FourKind < HandType::FiveKind)
    }
}

impl HandType {
    fn from_vec(ns: &[usize]) -> Self {
        let map = ns.iter().counts();

        match map.len() {
            1 => HandType::FiveKind,
            5 => HandType::HighCard,
            4 => HandType::OnePair,
            2 => {
                // it's either a FourKind or a FullHouse
                let n = **map.values().collect::<Vec<_>>().first().unwrap();
                // either this count is 4, 3, 2, or 1. the 4 or 1 makes it a FourKind
                // and the 3 or 2 makes it a FullHouse
                match n {
                    4 | 1 => HandType::FourKind,
                    3 | 2 => HandType::FullHouse,
                    _ => panic!("got {n} evaluating map len 2"),
                }
            }
            3 => {
                // the map has 3 keys. It's either a TwoPair or a ThreeKind.
                // if we find one count of 1, then we know it's a TwoPair.
                // otherwise, it's a ThreeKind.
                match map.keys().filter(|k| map[**k] == 1).count() {
                    1 => HandType::TwoPair,
                    2 => HandType::ThreeKind,
                    n => panic!("got {n} evaluating map size 3"),
                }
            }
            n => panic!("got {n} evaluating map"),
        }
    }
}

fn parse_hand(l: &str) -> Result<Hand> {
    let (hand, bid) = l.split_once(' ').unwrap();
    let hand = hand.chars().map(card_to_value).collect::<Vec<_>>();
    let hand_type = HandType::from_vec(&hand);

    Ok(Hand {
        hand,
        hand_type,
        bid: bid.parse()?,
    })
}

fn card_to_value(c: char) -> usize {
    match c {
        'A' => 14,
        'K' => 13,
        'Q' => 12,
        'J' => 11,
        'T' => 10,
        '9' => 9,
        '8' => 8,
        '7' => 7,
        '6' => 6,
        '5' => 5,
        '4' => 4,
        '3' => 3,
        '2' => 2,
        _ => panic!("wahh: {c}"),
    }
}
