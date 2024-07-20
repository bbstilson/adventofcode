use std::collections::{HashMap, HashSet};

use anyhow::Result;
use itertools::Itertools;

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
        let input = Day::input_lines(2015, 5)?;

        let num_nice = input.into_iter().filter(|s| nice_word(&s)).count();

        println!("{num_nice}");

        Ok(())
    }

    fn part2() -> Result<()> {
        let input = Day::input_lines(2015, 5)?;

        let num_nice = input.into_iter().filter(|s| nice_word_2(&s)).count();

        println!("{num_nice}");

        Ok(())
    }
}

fn nice_word(word: &str) -> bool {
    // It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
    // It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
    // It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.

    let mut vowels = 0;
    let mut last_letter: char = '_';
    let mut has_repeat = false;

    for letter in word.chars() {
        let is_vowel = match letter {
            'a' | 'e' | 'i' | 'o' | 'u' => true,
            _ => false,
        };
        if is_vowel {
            vowels += 1;
        }

        if last_letter == letter {
            has_repeat = true;
        }
        match letter {
            'b' if last_letter == 'a' => return false,
            'd' if last_letter == 'c' => return false,
            'q' if last_letter == 'p' => return false,
            'y' if last_letter == 'x' => return false,
            _ => (),
        }
        last_letter = letter;
    }

    has_repeat && vowels >= 3
}

fn nice_word_2(word: &str) -> bool {
    // It contains a pair of any two letters that appears at least twice in the string
    // without overlapping, like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa,
    // but it overlaps).
    // It contains at least one letter which repeats with exactly one letter between
    // them, like xyx, abcdefeghi (efe), or even aaa.

    let mut has_pair = false;
    let mut pairs: HashMap<(char, char), HashSet<usize>> = HashMap::new();
    for ((a_idx, a), (_, b)) in word.char_indices().tuple_windows() {
        match pairs.get_mut(&(a, b)) {
            Some(prev) => {
                if !prev.contains(&(a_idx - 1)) || prev.len() > 1 {
                    println!("found pair: {a}{b}");
                    has_pair = true;
                    break;
                } else {
                    prev.insert(a_idx);
                }
            }
            None => {
                pairs.insert((a, b), HashSet::from_iter(vec![a_idx].into_iter()));
            }
        }
    }

    let mut has_repeat = false;
    for (a, b, c) in word.chars().tuple_windows() {
        if a == c {
            println!("found repeat: {a}{b}{c}");
            has_repeat = true;
            break;
        }
    }

    println!("word: {word}");
    println!("has_repeat: {has_repeat}");
    println!("has_pair: {has_pair}");
    has_repeat && has_pair
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_nice_word() {
        assert!(nice_word("ugknbfddgicrmopn"));
        assert!(nice_word("aaa"));
        assert!(!nice_word("jchzalrnumimnmhp"));
        assert!(!nice_word("haegwjzuvuyypxyu"));
        assert!(!nice_word("dvszwmarrgswjxmb"));
    }

    #[test]
    fn test_nice_word_2() {
        assert!(nice_word_2("qjhvhtzxzqqjkmpb"));
        assert!(nice_word_2("xxyxx"));
        assert!(!nice_word_2("uurcxstgmygtbstg"));
        assert!(!nice_word_2("ieodomkazucvgmuy"));
        assert!(!nice_word_2("aaa"));
        assert!(nice_word_2("aaaa"));
        assert!(!nice_word_2("aaabcb"));
    }
}
