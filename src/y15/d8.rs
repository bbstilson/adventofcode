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
        let input = Day::input_lines(2015, 8)?;

        input
            .into_iter()
            .take(10)
            .for_each(|l| println!("{} - {l}", l.len()));

        Ok(())
    }

    fn part2() -> Result<()> {
        let input = Day::input_lines(2015, 8)?;

        Ok(())
    }
}

#[cfg(test)]
mod tests {

    #[test]
    fn test_parse() {
        let input = vec![
            ("\"\"", 2, 0),            // 2 characters zero characters.
            ("\"abc\"", 5, 3),         // 5 characters 3 characters.
            ("\"aaa\\\"aaa\"", 10, 7), // 10 characters 7 characters.
            ("\"\\x27\"", 6, 1),       // 6 characters one.
        ];

        for (line, expected_chars, expected_mem_chars) in input {
            let raw_len = line.len();
            println!("{:?}", line.as_bytes());
            let parsed = std::str::from_utf8(line.as_bytes()).unwrap();
            let parsed_len = parsed.len();

            println!(
                "{line} : {parsed} | {raw_len} : {expected_chars} | {parsed_len} : {expected_mem_chars}"
            );
        }

        println!("{}", std::str::from_utf8(b"\xf0\x9f\x8c\xb8").unwrap());
    }
}
