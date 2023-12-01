use std::sync::Arc;

use anyhow::Result;

pub struct Api;

impl Api {
    pub fn get_input(year: u32, day: u32) -> Result<String> {
        let url = format!("https://adventofcode.com/{year}/day/{day}/input");

        let token = std::env::var("AOC_SESSION_TOKEN")?;
        let cookies = reqwest::cookie::Jar::default();
        cookies.add_cookie_str(
            &format!("session={token}"),
            &url.parse::<reqwest::Url>().unwrap(),
        );

        let client = reqwest::blocking::Client::builder()
            .cookie_provider(Arc::new(cookies))
            .user_agent("advent-of-code-data bbstilson rust-v1")
            .build()?;

        let response = client.get(url).send()?;
        Ok(String::from_utf8(response.bytes()?.to_vec())?)
    }
}
