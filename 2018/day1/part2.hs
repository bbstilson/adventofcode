import qualified Data.Set as Set

main' :: IO ()
main' = do
  content <- readFile "input.txt"
  let xs = parseFile content
  let calced = reverse $ foldl calcFreqs [0] xs
  let (seen, dupes) = findDupe calced

  putStrLn $ show seen
  putStrLn $ show dupes

parseFile :: String -> [Int]
parseFile = map parseInt . lines

parseInt :: String -> Int
parseInt (sign:n) = read num :: Int
  where num = if sign == '+' then n else sign : n

calcFreqs :: [Int] -> Int -> [Int]
calcFreqs calced n = nextVal : calced
  where
    nextVal = head calced + n

findDupe :: [Int] -> ([Int], [Int])
findDupe xs = findDupe' 0 xs

findDupe' :: Int -> [Int] -> Int
findDupe' initVal xs =
  case foundVal of
    Just found -> found
    Nothing -> findDupe' lastVal xs
  where
    (seen, lastVal, foundVal) = foldl (\(seen, lastVal, found) x ->
      case found of
        Just val -> (seen, lastVal, found)
        Nothing -> updateState seen x
    ) (Set.singleton, initVal, Nothing) xs

updateState :: Set.Set -> Int -> (Set.Set, Int, Maybe Int)
updateState seen x =
  if Set.member x seen then
    (seen, lastVal, Just x)
  else
    (Set.insert x seen, x, Nothing)
