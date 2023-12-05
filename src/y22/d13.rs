use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day13;

impl AdventOfCode for Day13 {
    fn solve() -> Result<()> {
        let input = Day13::input_raw(2022, 13)?;
        let _paired = input
            .split("\n\n")
            .take(2)
            .for_each(|c| println!("\n{}", c));
        Ok(())
    }
}

// #[derive(Debug, Clone, PartialEq, Eq)]
// enum Input {
//     Val(u32),
//     List(Vec<Input>),
// }

// use Input::*;

// fn parse_line(line: String) -> Input {
//     fn helper<'a, I: Iterator<Item = &'a str>>(i: &mut I) -> Input {
//         let mut xs = vec![];
//         while let Some(t) = i.next() {
//             match t {
//                 "[" => xs.push(helper(i)),
//                 "]" => {}
//                 _ => xs.push(Val(t.parse::<u32>().expect("ahh"))),
//             }
//         }
//         List(xs)
//     }

//     helper(&mut line.split("").filter(|p| p != &"" && p != &","))
// }

// #[test]
// fn test_parse_line() {
//     let line = String::from("[1,2,3]");
//     assert_eq!(
//         parse_line(line),
//         List(vec![List(vec![Val(1), Val(2), Val(3)])])
//     );

//     let line = String::from("[1,[2,[5,[7]],4],3]");
//     assert_eq!(
//         parse_line(line),
//         List(vec![List(vec![
//             Val(1),
//             List(vec![Val(2), List(vec![Val(5), List(vec![Val(7)])]), Val(4)]),
//             Val(3)
//         ])])
//     );
// }
