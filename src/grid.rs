use std::{collections::HashMap, fmt::Debug};

pub type Coord = (isize, isize);

pub struct Grid<T> {
    pub map: HashMap<Coord, T>,
    width: isize,
    height: isize,
}

impl<'a, V> Grid<V> {
    pub fn from_string_vec<
        T,
        J: IntoIterator<Item = T>,
        SSB: Fn(String) -> J,
        F: Fn(T) -> V + Copy,
    >(
        xs: Vec<String>,
        split_string_by: SSB,
        f: F,
    ) -> Grid<V> {
        let height = xs.len() as isize;
        let width = xs.first().unwrap().len() as isize;
        let map = xs
            .into_iter()
            .enumerate()
            .flat_map(|(y, line)| {
                split_string_by(line)
                    .into_iter()
                    .enumerate()
                    .map(move |(x, c)| ((y as isize, x as isize), f(c)))
            })
            .collect::<HashMap<Coord, V>>();

        Grid { map, width, height }
    }
}

impl<V: Debug> std::fmt::Debug for Grid<V> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        for y in 0..self.height {
            for x in 0..self.width {
                let coord = (y, x);
                let value = &self.map[&coord];
                write!(f, "{:?}", value)?;
            }
            writeln!(f)?;
        }
        writeln!(f)
    }
}
