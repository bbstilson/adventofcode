use crate::adventofcode::AdventOfCode;

pub struct Day;

impl AdventOfCode for Day {
    fn solve() -> anyhow::Result<()> {
        let input = Day::input_raw(2024, 4).unwrap();
        let ws = Wordsearch::new(&input);
        let a = ws.search("XMAS");
        println!("{}", a.len());

        part2(&ws);

        Ok(())
    }
}

fn part2(ws: &Wordsearch) {
    // find all the 'A's. Check the corners for the right letters.
    let mut a_coords = vec![];
    for y in 0..ws.height {
        for x in 0..ws.width {
            if ws.board[y][x] == 'A' {
                a_coords.push((x, y));
            }
        }
    }

    let mut found = 0;
    for (x, y) in a_coords {
        // check bottom right for M
        let bottom_right_m = ws
            .move_coord(x, y, 1, 1)
            .filter(|(x, y)| ws.board[*y][*x] == 'M')
            .is_some();
        // check bottom left for M
        let bottom_left_m = ws
            .move_coord(x, y, -1, 1)
            .filter(|(x, y)| ws.board[*y][*x] == 'M')
            .is_some();
        // check top right for M
        let top_right_m = ws
            .move_coord(x, y, 1, -1)
            .filter(|(x, y)| ws.board[*y][*x] == 'M')
            .is_some();
        // check top left for M
        let top_left_m = ws
            .move_coord(x, y, -1, -1)
            .filter(|(x, y)| ws.board[*y][*x] == 'M')
            .is_some();
        // check bottom right for S
        let bottom_right_s = ws
            .move_coord(x, y, 1, 1)
            .filter(|(x, y)| ws.board[*y][*x] == 'S')
            .is_some();
        // check bottom left for S
        let bottom_left_s = ws
            .move_coord(x, y, -1, 1)
            .filter(|(x, y)| ws.board[*y][*x] == 'S')
            .is_some();
        // check top right for S
        let top_right_s = ws
            .move_coord(x, y, 1, -1)
            .filter(|(x, y)| ws.board[*y][*x] == 'S')
            .is_some();
        // check top left for S
        let top_left_s = ws
            .move_coord(x, y, -1, -1)
            .filter(|(x, y)| ws.board[*y][*x] == 'S')
            .is_some();

        // S . .
        // . A .
        // . . M
        let a = bottom_right_m && top_left_s;
        // . . S
        // . A .
        // M . .
        let b = bottom_left_m && top_right_s;
        // M . .
        // . A .
        // . . S
        let c = top_left_m && bottom_right_s;
        // . . M
        // . A .
        // S . .
        let d = top_right_m && bottom_left_s;

        found += vec![a && b, a && d, c && b, c && d]
            .into_iter()
            .filter(|t| *t)
            .count();
    }

    println!("{found}");
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
enum Dir {
    Up,
    UpRight,
    Right,
    DownRight,
    Down,
    DownLeft,
    Left,
    UpLeft,
}

const DIRS: [Dir; 8] = [
    Dir::Up,
    Dir::UpRight,
    Dir::Right,
    Dir::DownRight,
    Dir::Down,
    Dir::DownLeft,
    Dir::Left,
    Dir::UpLeft,
];

fn parse_input(input: &str) -> Vec<Vec<char>> {
    input
        .split('\n')
        .map(str::trim)
        .filter(|row| !row.is_empty())
        .map(|row| row.chars().collect::<Vec<char>>())
        .collect()
}

struct Wordsearch {
    width: usize,
    height: usize,
    board: Vec<Vec<char>>,
}

impl Wordsearch {
    pub fn new(input: &str) -> Self {
        let board = parse_input(input);
        let height = board.len();
        let width = board[0].len();
        Self {
            width,
            height,
            board,
        }
    }

    pub fn search(&self, word: &str) -> Vec<((usize, usize), Dir)> {
        let mut chars = word.chars();
        let start = chars.next().unwrap();
        let rest = chars.collect::<Vec<_>>();

        let mut found = vec![];
        for y in 0..self.height {
            for x in 0..self.width {
                if self.board[y][x] == start {
                    for dir in DIRS {
                        let search = Search {
                            dir,
                            start: (x, y),
                            ws: self,
                        };

                        found.append(&mut search.search(x, y, &rest));
                    }
                }
            }
        }
        found
    }

    fn move_coord(&self, x: usize, y: usize, dx: isize, dy: isize) -> Option<(usize, usize)> {
        x.checked_add_signed(dx)
            .and_then(|x| y.checked_add_signed(dy).map(|y| (x, y)))
            .filter(|(x, y)| *x < self.width && *y < self.height)
    }

    fn dir_to_coord(&self, x: usize, y: usize, dir: Dir) -> Option<(usize, usize)> {
        match dir {
            Dir::Up => self.move_coord(x, y, 0, -1),
            Dir::UpRight => self.move_coord(x, y, 1, -1),
            Dir::Right => self.move_coord(x, y, 1, 0),
            Dir::DownRight => self.move_coord(x, y, 1, 1),
            Dir::Down => self.move_coord(x, y, 0, 1),
            Dir::DownLeft => self.move_coord(x, y, -1, 1),
            Dir::Left => self.move_coord(x, y, -1, 0),
            Dir::UpLeft => self.move_coord(x, y, -1, -1),
        }
    }
}

struct Search<'a> {
    start: (usize, usize),
    dir: Dir,
    ws: &'a Wordsearch,
}

impl Search<'_> {
    pub fn search(&self, x: usize, y: usize, word: &[char]) -> Vec<((usize, usize), Dir)> {
        if word.is_empty() {
            vec![(self.start, self.dir)]
        } else {
            let mut out = vec![];
            if let Some((x, y)) = self.ws.dir_to_coord(x, y, self.dir) {
                if word[0] == self.ws.board[y][x] {
                    out.append(&mut self.search(x, y, &word[1..]));
                }
            }
            out
        }
    }
}

#[test]
fn test_wordsearch() {
    let test_input = "MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX";
    let ws = Wordsearch::new(test_input);
    assert_eq!(ws.search("XMAS").len(), 18);

    part2(&ws);
}
