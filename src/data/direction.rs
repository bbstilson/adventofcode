use crate::data::coord::Coord;

#[derive(Debug, PartialEq, Eq)]
pub enum Direction {
    L,
    R,
    U,
    D,
}

impl Direction {
    pub fn to_coord(&self) -> Coord {
        match self {
            Self::U => Coord(-1, 0),
            Self::D => Coord(1, 0),
            Self::L => Coord(0, -1),
            Self::R => Coord(0, 1),
        }
    }

    pub fn all() -> [Self; 4] {
        [Direction::U, Direction::D, Direction::L, Direction::R]
    }
}
