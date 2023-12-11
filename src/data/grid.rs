use std::collections::HashMap;

use crate::data::coord::Coord;

pub type Map<T> = HashMap<Coord, T>;

pub struct Grid<T> {
    pub map: Map<T>,
    pub width: i64,
    pub height: i64,
}

impl<T: ToString> From<Grid<T>> for String {
    fn from(grid: Grid<T>) -> Self {
        let mut s = String::new();
        for y in 0..grid.height {
            for x in 0..grid.width {
                let n: String = grid
                    .map
                    .get(&Coord(y, x))
                    .map(|n| n.to_string())
                    .unwrap_or(".".to_string());

                s.push_str(&n);
            }
            s.push('\n');
        }
        s.trim().into()
    }
}

// #[cfg(test)]
// mod tests {
//     use super::*;

//     #[test]
//     fn test() {
//         let input = ".....
// .F-7.
// .|.|.
// .L-J.
// .....";
//         let map = parse_input(input);
//         let grid = Grid {
//             map,
//             width: 5,
//             height: 5,
//         };

//         let grid_str: String = grid.into();

//         println!("{grid_str}");

//         assert_eq!(grid_str, input);
//     }
// }
