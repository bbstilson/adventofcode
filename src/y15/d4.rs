use anyhow::Result;
use md5::Digest;

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
        let input = "yzbqklnj";
        cwypto(input, "00000");
        Ok(())
    }

    fn part2() -> Result<()> {
        let input = "yzbqklnj";
        cwypto(input, "000000");
        Ok(())
    }
}

fn hash(s: &str) -> String {
    let mut hasher = md5::Md5::default();

    hasher.update(s.as_bytes());
    // hasher.update(b"hello world");

    let result = hasher.finalize();
    let a: &[u8] = &result;
    hex::encode(a)
}

fn cwypto(input: &str, target: &str) {
    let mut i = 0;

    loop {
        if i % 10_000 == 0 {
            println!("{i}");
        }
        if hash(&format!("{input}{i}")).starts_with(target) {
            break;
        }
        i += 1;
    }

    println!("{i}");
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_hasher() {
        assert_eq!(hash("abcdef609043"), "000001dbbfa3a5c83a2d506429c7b00e");
    }
}
