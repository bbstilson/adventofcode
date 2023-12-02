use anyhow::Result;
use once_cell::sync::Lazy;
use regex::Regex;

use crate::adventofcode::AdventOfCode;

static GAME_REGEX: Lazy<Regex> =
    Lazy::new(|| Regex::new(r"Game (?P<game_id>\d+): (?P<hands>.+)").unwrap());

pub struct Day2;

impl AdventOfCode for Day2 {
    fn solve() -> Result<()> {
        let input = Day2::input_lines(2023, 2)?;
        let games = input
            .into_iter()
            .filter_map(|l| parse_line(l).ok())
            .collect::<Vec<_>>();

        println!("{}", part_1(&games));
        println!("{}", part_2(&games));
        Ok(())
    }
}

fn part_1(games: &Vec<Game>) -> usize {
    // sum game ids that could possibly meet these criteria:
    // 12 red cubes, 13 green cubes, and 14 blue cubes

    games
        .iter()
        .filter(|g| all_hands_valid(&g.hands))
        .map(|g| g.id)
        .sum()
}

fn all_hands_valid(hands: &Vec<Hand>) -> bool {
    // make sure each hand does not have more than:
    // 12 red cubes, 13 green cubes, and 14 blue cubes
    hands
        .iter()
        .all(|hand| hand.red <= 12 && hand.green <= 13 && hand.blue <= 14)
}

fn part_2(games: &Vec<Game>) -> usize {
    // Get the SUM of the 'POWER' of the fewest number of cubes of each color that could
    // have been in the bag to make the game possible.
    //
    // The power of a set of cubes is equal to the numbers of red, green, and blue cubes
    // multiplied together.

    games.iter().map(calculate_min_power).sum()
}

fn calculate_min_power(game: &Game) -> usize {
    let init = &mut Hand::new();
    let mins = game.hands.iter().fold(init, |mins, hand| {
        mins.red = mins.red.max(hand.red);
        mins.green = mins.green.max(hand.green);
        mins.blue = mins.blue.max(hand.blue);
        mins
    });

    mins.red * mins.green * mins.blue
}

#[derive(Debug)]
struct Game {
    id: usize,
    hands: Vec<Hand>,
}

#[derive(Debug)]
struct Hand {
    red: usize,
    blue: usize,
    green: usize,
}

impl Hand {
    pub fn new() -> Self {
        Self {
            red: 0,
            blue: 0,
            green: 0,
        }
    }
}

enum Color {
    R,
    G,
    B,
}

fn parse_line(l: String) -> Result<Game> {
    let caps = GAME_REGEX.captures(&l).unwrap();

    let id = caps.name("game_id").unwrap().as_str().parse::<usize>()?;
    let hands = parse_hands(caps.name("hands").unwrap().as_str())?;

    Ok(Game { id, hands })
}

fn parse_hands(hs: &str) -> Result<Vec<Hand>> {
    let mut hands = vec![];
    for h in hs.split(';') {
        let mut hand = Hand::new();
        for dice_info in h.split(',') {
            let parts = dice_info.trim().split_whitespace().collect::<Vec<_>>();
            let count = parts[0].parse::<usize>()?;
            match parse_color(parts[1]) {
                Color::R => hand.red = count,
                Color::G => hand.green = count,
                Color::B => hand.blue = count,
            }
        }
        hands.push(hand);
    }
    Ok(hands)
}

fn parse_color(s: &str) -> Color {
    if s == "blue" {
        Color::B
    } else if s == "green" {
        Color::G
    } else if s == "red" {
        Color::R
    } else {
        panic!("'{s}' is not a color")
    }
}
