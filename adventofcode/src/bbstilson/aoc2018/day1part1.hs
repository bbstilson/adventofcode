main :: IO ()
main = do
  content <- readFile "input.txt"
  let total = sum $ parseFile content
  putStrLn $ show total

parseFile :: String -> [Int]
parseFile = map parseInt . lines

parseInt :: String -> Int
parseInt (sign:n) = read num :: Int
  where num = if sign == '+' then n else sign : n
