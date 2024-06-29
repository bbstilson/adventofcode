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

#[derive(Debug)]
struct Present {
    l: usize,
    w: usize,
    h: usize,
}
impl Present {
    fn parse(s: String) -> Self {
        let mut parts = s.split('x');
        Self {
            l: parts.next().unwrap().parse().unwrap(),
            w: parts.next().unwrap().parse().unwrap(),
            h: parts.next().unwrap().parse().unwrap(),
        }
    }

    fn area(self) -> usize {
        // 2*l*w + 2*w*h + 2*h*l + smallest area
        let l_area = self.l * self.w;
        let w_area = self.w * self.h;
        let h_area = self.h * self.l;
        let smalleset = l_area.min(w_area.min(h_area));
        (2 * l_area) + (2 * w_area) + (2 * h_area) + smalleset
    }

    fn ribbon(self) -> usize {
        let l = self.l * 2;
        let w = self.w * 2;
        let h = self.h * 2;

        let perimeter = (l + w).min((l + h).min(h + w));
        // cubic feet of volume of the present

        perimeter + (self.l * self.w * self.h)
    }
}

impl Day {
    fn part1() -> Result<()> {
        let input = Day::input_lines(2015, 2)?;

        let ans: usize = input
            .into_iter()
            .map(Present::parse)
            .map(|b| b.area())
            .sum();

        println!("{ans}");
        Ok(())
    }

    fn part2() -> Result<()> {
        let input = Day::input_lines(2015, 2)?;
        let ans: usize = input
            .into_iter()
            .map(Present::parse)
            .map(|b| b.ribbon())
            .sum();

        println!("{ans}");
        Ok(())
    }
}

#[cfg(test)]
mod tests {
    use super::Present;

    #[test]
    fn area() {
        assert_eq!(Present { l: 2, w: 3, h: 4 }.area(), 58);
        assert_eq!(Present { l: 1, w: 1, h: 10 }.area(), 43);
        assert_eq!(Present { l: 2, w: 3, h: 4 }.ribbon(), 34);
        assert_eq!(Present { l: 1, w: 1, h: 10 }.ribbon(), 14);
    }
}
