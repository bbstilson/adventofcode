import qualified Data.List as L

main :: IO ()
main = do
  content <- readFile "input.txt"
  let checkSum = process content
  putStrLn $ show checkSum

process =
  product .
  map length .
  sortGroup .
  concat .
  map getOneOfEachLen .
  map (filter isMinLen) .
  map sortGroup .
  lines

isMinLen :: String -> Bool
isMinLen text = length text >= 2

getOneOfEachLen :: [String] -> [Int]
getOneOfEachLen = map head . sortGroup . map length

sortGroup :: (Ord a) => [a] -> [[a]]
sortGroup = L.group . L.sort
