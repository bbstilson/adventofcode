use anyhow::Result;

use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> Result<()> {
        println!("{}", part_1());
        println!("{}", part_2());

        Ok(())
    }
}

fn part_1() -> usize {
    let times = vec![40, 81, 77, 72];
    let dists = vec![219, 1012, 1365, 1089];

    let comps = times.into_iter().zip(dists).collect::<Vec<_>>();
    comps.into_iter().map(ways_to_win).product()
}

fn part_2() -> usize {
    let time = 40817772;
    let dist = 219101213651089;

    ways_to_win((time, dist))
}

fn ways_to_win(t: (usize, usize)) -> usize {
    (0..t.0)
        .filter(|speed| {
            let time_to_travel = t.0 - speed;
            (speed * time_to_travel) > t.1
        })
        .count()
}
