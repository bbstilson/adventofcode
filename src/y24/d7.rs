use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let input = Day::input_raw(2024, 7).unwrap();
        let probs = parse_input(input);
        part1(&probs);
        part2(&probs);
        Ok(())
    }
}

fn part1(probs: &[(i64, Vec<i64>)]) {
    let mut ans = vec![];
    for (target, nums) in probs {
        let num_solutions = solve1(*target, nums);
        if num_solutions > 0 {
            ans.push(*target);
        }
    }

    let ans = ans.into_iter().sum::<i64>();
    println!("{ans}");
}

fn part2(probs: &[(i64, Vec<i64>)]) {
    let mut ans = vec![];
    for (target, nums) in probs {
        let num_solutions = solve2(*target, nums);
        if num_solutions > 0 {
            ans.push(*target);
        }
    }

    let ans = ans.into_iter().sum::<i64>();
    println!("{ans}");
}

fn solve1(target: i64, nums: &[i64]) -> u64 {
    // println!("{target} - {nums:?}");
    if nums.len() == 1 {
        if target == nums[0] {
            1
        } else {
            0
        }
    } else if target < 0 {
        0
    } else {
        let a = nums[0];
        let b = nums[1];
        let mut nums_l = vec![0; nums.len() - 1];
        let mut nums_r = vec![0; nums.len() - 1];

        nums_l.clone_from_slice(&nums[1..]);
        nums_r.clone_from_slice(&nums[1..]);

        nums_l[0] = a * b;
        nums_r[0] = a + b;

        solve1(target, &nums_l) + solve1(target, &nums_r)
    }
}

fn solve2(target: i64, nums: &[i64]) -> u64 {
    // println!("{target} - {nums:?}");
    if nums.len() == 1 {
        if target == nums[0] {
            1
        } else {
            0
        }
    } else if target < 0 {
        0
    } else {
        let a = nums[0];
        let b = nums[1];

        let mut ans = 0;

        // +
        let mut nums_r = vec![0; nums.len() - 1];
        nums_r.clone_from_slice(&nums[1..]);
        nums_r[0] = a + b;
        ans += solve2(target, &nums_r);

        // *
        if let Some(l) = a.checked_mul(b) {
            let mut nums_l = vec![0; nums.len() - 1];
            nums_l.clone_from_slice(&nums[1..]);
            nums_l[0] = l;
            ans += solve2(target, &nums_l);
        }

        // ||
        if let Some(sa) = shift(a) {
            let mut nums_m = vec![0; nums.len() - 1];
            nums_m.clone_from_slice(&nums[1..]);
            nums_m[0] = sa + b;
            ans += solve2(target, &nums_m);
        }

        ans
    }
}

fn shift(n: i64) -> Option<i64> {
    let shift = 10i64.pow((n as f32).log10().floor() as u32);
    n.checked_mul(shift)
}

fn parse_input(input: String) -> Vec<(i64, Vec<i64>)> {
    input
        .lines()
        .map(|l| {
            let (total, nums) = l.split_once(':').unwrap();
            (
                total.parse::<i64>().unwrap(),
                nums.trim()
                    .split(' ')
                    .map(|n| n.parse::<i64>().unwrap())
                    .collect(),
            )
        })
        .collect()
}

#[test]
fn test_parts() {
    let input = "190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20"
        .to_string();
    let probs = parse_input(input);
    part1(&probs);
    part2(&probs);
}
