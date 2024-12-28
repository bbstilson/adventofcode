use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let input = vec![5, 89749, 6061, 43, 867, 1965860, 0, 206250];

        blink(input.clone(), 25);
        // blink(input.clone(), 75);

        Ok(())
    }
}

fn blink(stones: Vec<u64>, n: usize) {
    let (first_iters, _) =
        (0..n)
            .into_iter()
            .fold((vec![stones.len()], stones), |(mut lens, stones), _| {
                let next = blink_once(stones);
                println!("{}", next.len());
                lens.push(next.len());
                (lens, next)
            });
    println!("{first_iters:?}");
}

fn fib(a: usize, b: usize, n: usize) -> usize {
    assert!(a < b, "a should be less than b");

    let mut a = a;
    let mut b = b;
    for _ in 0..n {
        let nb = a + b;
        a = b;
        b = nb;
    }
    b
}

fn blink_once(stones: Vec<u64>) -> Vec<u64> {
    // If the stone is engraved with the number 0, it is replaced by a stone engraved
    // with the number 1.
    //
    // If the stone is engraved with a number that has an even number of digits, it is
    // replaced by two stones. The left half of the digits are engraved on the new left
    // stone, and the right half of the digits are engraved on the new right stone.
    // (The new numbers don't keep extra leading zeroes: 1000 would become stones 10
    // and 0.)
    //
    // If none of the other rules apply, the stone is replaced by a new stone; the old
    // stone's number multiplied by 2024 is engraved on the new stone.
    let mut next_stones = vec![];
    for stone in stones {
        match stone {
            0 => next_stones.push(1),
            s if has_even_num_digies(s) => {
                let (front, back) = split_num(s);
                next_stones.push(front);
                next_stones.push(back);
            }
            _ => next_stones.push(stone * 2024),
        }
    }
    next_stones
}

fn has_even_num_digies(n: u64) -> bool {
    exponent(n) % 2 == 1
}

fn exponent(n: u64) -> u32 {
    (n as f64).log10().floor() as u32
}

fn split_num(n: u64) -> (u64, u64) {
    let exp = exponent(n);
    let half = 10u64.pow(exp / 2) * 10;
    (n / half, n % half)
}

#[test]
fn test_split_num() {
    assert_eq!(split_num(1234), (12, 34));
    assert_eq!(split_num(123456), (123, 456));
}

#[test]
fn test_fib() {
    blink(vec![125, 17], 6);
}

#[test]
fn ahh() {
    let mut stones = vec![125, 17];
    let mut prev_len = stones.len();
    for _ in 0..25 {
        stones = blink_once(stones);
        println!(
            "{:?} - {prev_len} = {}",
            stones.len(),
            stones.len() - prev_len
        );
        prev_len = stones.len();
    }
}
