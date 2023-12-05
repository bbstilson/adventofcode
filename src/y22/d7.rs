use std::collections::HashMap;

use anyhow::Result;

use crate::adventofcode::AdventOfCode;
pub struct Day7;

#[derive(Debug)]
struct FileDir {
    size: u32,
    children: HashMap<String, FileDir>,
}

impl FileDir {
    pub fn new() -> FileDir {
        FileDir {
            size: 0,
            children: HashMap::new(),
        }
    }

    pub fn mk_at_path(&mut self, path: &Vec<String>, name: String, size: u32) {
        let mut fd = self.children.get_mut(&path[0]).unwrap();
        for p in &path[1..] {
            fd = fd.children.get_mut(p).unwrap();
        }
        fd.children.insert(
            name,
            FileDir {
                size,
                children: HashMap::new(),
            },
        );
    }
}

impl AdventOfCode for Day7 {
    fn solve() -> Result<()> {
        let input = Day7::input_lines(2022, 7)?;

        let mut os = FileDir {
            size: 0,
            children: HashMap::from_iter([("/".to_string(), FileDir::new())]),
        };
        let mut path: Vec<String> = vec![];

        for line in input.iter().skip(0) {
            if line.starts_with("$ cd") {
                let change = line.strip_prefix("$ cd ").unwrap().to_string();
                if change == ".." {
                    path.pop();
                } else {
                    path.push(change);
                }
            } else if line.starts_with("dir") {
                let dir_name = line.strip_prefix("dir ").unwrap().to_string();
                os.mk_at_path(&path, dir_name, 0);
            } else if line.starts_with("$ ls") {
                continue;
            } else {
                // it's a file
                let split = line.split(" ").collect::<Vec<_>>();
                let size = split[0].parse::<u32>()?;
                let file_name = split[1].to_string();
                os.mk_at_path(&path, file_name, size);
            }
        }

        fn get_dir_size(fd: &FileDir) -> u32 {
            fd.size
                + fd.children
                    .values()
                    .fold(0, |acc, child| acc + get_dir_size(child))
        }

        // sum all directories whos total size is <= 100_000
        fn part_1_helper(fd: &FileDir) -> u32 {
            fd.children.values().fold(0, |acc, child| {
                let dir_size = part_1_helper(child);
                if dir_size <= 100000 {
                    acc + dir_size
                } else {
                    acc
                }
            }) + fd.size
        }

        let part_1 = part_1_helper(&os);
        println!("part 1: {} -> {}", 1501149, part_1);

        let available = 70_000_000;
        let required = 30_000_000;
        let used_space = get_dir_size(&os);
        let unused = available - used_space;
        let space_to_delete = required - unused;

        fn part_2_helper(fd: &FileDir, to_delete: u32) -> u32 {
            let mut children = fd
                .children
                .values()
                .map(|child| part_2_helper(child, to_delete))
                .collect::<Vec<u32>>();

            children.sort();

            if let Some(child) = children.iter().find(|size| **size > to_delete) {
                *child
            } else {
                fd.size + children.iter().sum::<u32>()
            }
        }

        let part_2 = part_2_helper(&os, space_to_delete);
        println!("part 2: {} -> {}", 10096985, part_2);

        Ok(())
    }
}
