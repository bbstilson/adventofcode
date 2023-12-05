use std::ops::Range;

use anyhow::Result;
use itertools::Itertools;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        let input = Day::input_raw(2023, 5)?;
        //         let input = "seeds: 79 14 55 13

        // seed-to-soil map:
        // 50 98 2
        // 52 50 48

        // soil-to-fertilizer map:
        // 0 15 37
        // 37 52 2
        // 39 0 15

        // fertilizer-to-water map:
        // 49 53 8
        // 0 11 42
        // 42 0 7
        // 57 7 4

        // water-to-light map:
        // 88 18 7
        // 18 25 70

        // light-to-temperature map:
        // 45 77 23
        // 81 45 19
        // 68 64 13

        // temperature-to-humidity map:
        // 0 69 1
        // 1 0 69

        // humidity-to-location map:
        // 60 56 37
        // 56 93 4"
        //             .to_string();

        let almanac = parse_almanac(&input)?;

        println!("{}", part_1(&almanac));

        Ok(())
    }
}

fn part_1(almanac: &Almanac) -> isize {
    almanac
        .seeds
        .iter()
        .flat_map(|seed_range| {
            vec![
                almanac.seed_to_location(seed_range.start),
                almanac.seed_to_location(seed_range.end),
            ]
        })
        .min()
        .unwrap()
}

#[derive(Debug)]
struct Almanac {
    seeds: Vec<Range<isize>>,
    seed_to_soil: Vec<Map>,
    soil_to_fertilizer: Vec<Map>,
    fertilizer_to_water: Vec<Map>,
    water_to_light: Vec<Map>,
    light_to_temperature: Vec<Map>,
    temperature_to_humidity: Vec<Map>,
    humidity_to_location: Vec<Map>,
}

impl Almanac {
    fn seed_to_location(&self, seed: isize) -> isize {
        let soil = find_mapping(seed, &self.seed_to_soil);
        let fertilizer = find_mapping(soil, &self.soil_to_fertilizer);
        let water = find_mapping(fertilizer, &self.fertilizer_to_water);
        let light = find_mapping(water, &self.water_to_light);
        let temperature = find_mapping(light, &self.light_to_temperature);
        let humitidy = find_mapping(temperature, &self.temperature_to_humidity);
        find_mapping(humitidy, &self.humidity_to_location)
    }
}

fn find_mapping(source: isize, maps: &[Map]) -> isize {
    // Any source numbers that aren't mapped correspond to the same destination number.
    maps.iter()
        .find(|m| m.source.contains(&source))
        .map(|m| m.dest.start + (source - m.source.start))
        .unwrap_or(source)
}

#[derive(Debug)]
struct Map {
    dest: Range<isize>,
    source: Range<isize>,
}

fn parse_almanac(s: &str) -> Result<Almanac> {
    let mut sections = s.split("\n\n");
    let seeds = parse_seeds(sections.next().unwrap());
    let seed_to_soil = parse_map(sections.next().unwrap());
    let soil_to_fertilizer = parse_map(sections.next().unwrap());
    let fertilizer_to_water = parse_map(sections.next().unwrap());
    let water_to_light = parse_map(sections.next().unwrap());
    let light_to_temperature = parse_map(sections.next().unwrap());
    let temperature_to_humidity = parse_map(sections.next().unwrap());
    let humidity_to_location = parse_map(sections.next().unwrap());

    Ok(Almanac {
        seeds,
        seed_to_soil,
        soil_to_fertilizer,
        fertilizer_to_water,
        water_to_light,
        light_to_temperature,
        temperature_to_humidity,
        humidity_to_location,
    })
}
fn parse_seeds(s: &str) -> Vec<Range<isize>> {
    s.split_once(": ")
        .unwrap()
        .1
        .split_whitespace()
        .filter_map(|c| c.parse().ok())
        .tuples()
        .map(|(a, b)| a..b)
        .collect::<Vec<Range<isize>>>()
}

fn parse_map(s: &str) -> Vec<Map> {
    s.lines().skip(1).map(parse_range_line).collect()
}

fn parse_range_line(s: &str) -> Map {
    let mut nums = s.split_whitespace().filter_map(|n| n.parse().ok());
    let dest_start = nums.next().unwrap();
    let source_start = nums.next().unwrap();
    let range = nums.next().unwrap();
    Map {
        dest: dest_start..(dest_start + range),
        source: source_start..(source_start + range),
    }
}
