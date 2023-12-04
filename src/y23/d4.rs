use std::collections::{HashMap, HashSet};

use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let input = Day::input_lines(2023, 4)?;

        let cards = input
            .into_iter()
            .filter_map(|l| parse_card(&l).ok())
            .collect::<Vec<_>>();

        println!("{} = 20407", part_1(&cards));
        println!("{} = 23806951", part_2(&cards));

        Ok(())
    }
}

#[derive(Debug)]
struct Card {
    id: usize,
    winning_numbers: HashSet<usize>,
    card_numbers: HashSet<usize>,
}

impl Card {
    fn matches(&self) -> usize {
        self.card_numbers
            .intersection(&self.winning_numbers)
            .count()
    }

    fn score(&self) -> usize {
        // The first match makes the card worth one point and each match after the first
        // doubles the point value of that card.
        match self.matches() {
            0 => 0,
            n => 2_usize.pow(n as u32 - 1),
        }
    }
}

fn parse_card(line: &str) -> Result<Card> {
    let (card, numbers) = line.split_once(": ").unwrap();
    let card_id = card.split_whitespace().nth(1).unwrap().parse::<usize>()?;
    let (winning_numbers, card_numbers) = numbers.split_once(" | ").unwrap();

    Ok(Card {
        id: card_id,
        winning_numbers: parse_numbers(winning_numbers),
        card_numbers: parse_numbers(card_numbers),
    })
}

fn parse_numbers(s: &str) -> HashSet<usize> {
    s.split_whitespace()
        .filter_map(|n| n.parse::<usize>().ok())
        .collect()
}

fn part_1(cards: &[Card]) -> usize {
    cards.iter().map(|c| c.score()).sum()
}

fn part_2(cards: &[Card]) -> usize {
    let cards_by_id = cards.iter().map(|c| (c.id, c)).collect::<HashMap<_, _>>();

    fn helper(
        card_id: usize,
        cards_by_id: &HashMap<usize, &Card>,
        memo: &mut HashMap<usize, usize>,
    ) -> usize {
        match memo.get(&card_id) {
            Some(winnings) => *winnings,
            None => {
                let winnings = (1..=cards_by_id[&card_id].matches())
                    .map(|offset| 1 + helper(card_id + offset, cards_by_id, memo))
                    .sum();

                memo.insert(card_id, winnings);
                winnings
            }
        }
    }

    let memo = &mut HashMap::new();
    cards
        .iter()
        .map(|c| 1 + helper(c.id, &cards_by_id, memo))
        .sum()
}

#[cfg(test)]
mod tests {
    use crate::y23::d4::*;

    #[test]
    fn example() {
        let input = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
            .split('\n')
            .map(|l| l.to_owned())
            .collect::<Vec<String>>();

        let cards = input
            .into_iter()
            .filter_map(|l| parse_card(&l).ok())
            .collect::<Vec<_>>();

        assert_eq!(part_1(&cards), 13);
        assert_eq!(part_2(&cards), 30);
    }
}
