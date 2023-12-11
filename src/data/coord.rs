#[derive(Debug, Hash, PartialEq, Eq, Clone, Copy)]
pub struct Coord(pub i64, pub i64);

impl Coord {
    pub fn is_neighbor(&self, other: &Coord) -> bool {
        (self.0 == other.0 && self.1 + 1 == other.1)  || // right
        (self.0 == other.0 && self.1- 1 == other.1) || // left
        (self.0 + 1 == other.0 && self.1 == other.1) || // up
        (self.0 - 1 == other.0 && self.1 == other.1) // down
    }
}
