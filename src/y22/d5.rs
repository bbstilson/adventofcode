use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day5;

impl AdventOfCode for Day5 {
    fn solve() -> Result<()> {
        let input = Day5::input_lines(2022, 5)?;

        // Just lifted this from the first 10 lines of my input.
        let init_state = vec![
            vec!['D', 'M', 'S', 'Z', 'R', 'F', 'W', 'N'],
            vec!['W', 'P', 'Q', 'G', 'S'],
            vec!['W', 'R', 'V', 'Q', 'F', 'N', 'J', 'C'],
            vec!['F', 'Z', 'P', 'C', 'G', 'D', 'L'],
            vec!['T', 'P', 'S'],
            vec!['H', 'D', 'F', 'W', 'R', 'L'],
            vec!['Z', 'N', 'D', 'C'],
            vec!['W', 'N', 'R', 'F', 'V', 'S', 'J', 'Q'],
            vec!['R', 'M', 'S', 'G', 'Z', 'W', 'V'],
        ];

        let parse_command = |line: &String| {
            let parsed = line
                .split_whitespace()
                .flat_map(|str| str.parse::<usize>())
                .collect::<Vec<usize>>();

            (parsed[0], parsed[1] - 1, parsed[2] - 1)
        };

        let get_boxes_on_top = |state: Vec<Vec<char>>| {
            state
                .iter()
                .map(|col| col.last().unwrap())
                .collect::<String>()
        };

        let part_1_state = input
            .iter()
            .skip(10)
            .fold(init_state.clone(), |mut state, cmd| {
                let (num, from, to) = parse_command(cmd);

                let from_len = state[from].len();
                let mut crates = state[from].split_off(from_len - num);
                crates.reverse();
                state[to].extend(crates);

                state
            });

        let part_2_state = input.iter().skip(10).fold(init_state, |mut state, cmd| {
            let (num, from, to) = parse_command(cmd);

            let from_len = state[from].len();
            let crates = state[from].split_off(from_len - num);
            state[to].extend(crates);

            state
        });

        let part_1 = get_boxes_on_top(part_1_state);
        let part_2 = get_boxes_on_top(part_2_state);

        println!("part 1: {}", part_1);
        println!("part 2: {}", part_2);

        Ok(())
    }
}
