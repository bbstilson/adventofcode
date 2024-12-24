use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let input = Day::input_lines(2024, 2)?
            .into_iter()
            .map(|l| {
                l.split(' ')
                    .map(|i| i.parse::<i32>().unwrap())
                    .collect::<Vec<_>>()
            })
            .collect::<Vec<_>>();

        let part1 = input.iter().filter(|xs| is_safe(xs.as_slice())).count();
        println!("{part1}");
        Ok(())
    }
}

fn is_safe(xs: &[i32]) -> bool {
    let mut ins = xs.windows(2);
    let abs = ins.next().unwrap();

    let a = abs[0];
    let b = abs[1];
    let is_increasing = a > b;
    let diff = (a - b).abs();
    println!("is_increasing = {is_increasing}");
    println!("diff = {diff}");
    if diff < 1 && diff > 3 {
        return false;
    }

    while let Some(abs) = ins.next() {
        let a = abs[0];
        let b = abs[1];
        let diff = (a - b).abs();
        if diff < 1 && diff > 3 {
            return false;
        }
        if (is_increasing && b > a) || (!is_increasing && a > b) {
            return false;
        }
    }
    true
}

#[test]
fn test_is_safe() {
    assert!(is_safe(&[1, 2, 3]));
    assert!(is_safe(&[3, 2, 1]));
    assert!(!is_safe(&[1, 4, 5]));
    assert!(!is_safe(&[1, 2, 4]));
}
