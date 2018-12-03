import qualified Data.List as L
import qualified Data.Maybe as M

{-
Though this code produces the correct solution, there's a bug in here somewhere.
The goal is to find two ids that are only different by one character. The bug is
that we find A -> B as well as B -> A. Given this is day 2 advent challenge, and
day 2 of learning me a haskell for great good, I'm gonna leave it as is and not
sweat it.
-}

main :: IO ()
main = do
  content <- readFile "input.txt"
  let boxIds = lines content
  let matched = filter (\(_, matches) -> length matches > 0) $ map (\id -> (id, findMatch id boxIds)) boxIds
  let letters = map (\(id, matches) -> (id, head matches)) matched
  let finalLetters = map (\(id, oId) -> [x | x <- id, x `elem` oId]) letters
  let finalForReal = finalLetters

  putStrLn $ show finalForReal

findMatch :: String -> [String] -> [String]
findMatch boxId boxIds = M.catMaybes $ map (\otherBoxId ->
    if otherBoxId /= boxId && hasSmallDiff boxId otherBoxId then
      Just otherBoxId
    else
      Nothing
  ) boxIds

hasSmallDiff :: String -> String -> Bool
hasSmallDiff bId oId =
  (<2) $
  length $
  filter (==False) $
  concat $
  L.group $
  L.sort $
  map (\(x,y) -> x == y) $
  zip bId oId
