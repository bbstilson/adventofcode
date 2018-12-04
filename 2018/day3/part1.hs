import qualified Data.List as L
import qualified Data.Map as M
import Data.Function (on)

type Coord = (Int, Int)

data Rect = Rect { pos :: Coord
                 , dim :: Coord
                 } deriving (Show)

data Order = Order { oid   :: Int
                   , claim :: Rect
                   } deriving (Show)

main :: IO ()
main = do
  content <- readFile "input.txt"
  let orders = map parseOrder . lines $ content
  let orderMap = freqMap . concatMap produceClaims $ orders
  let sharedTiles = length . filter (>= 2) . M.elems $ orderMap

  putStrLn . show $ sharedTiles

parseOrder :: String -> Order
parseOrder order =
  let
    (oid : _at : pos : dim) = words order
    orderId = parseInt . tail $ oid
    position = parsePosition . init $ pos
    dimension = parseDimension . head $ dim
  in
    Order { oid = orderId, claim = Rect { pos=position, dim=dimension } }

parseInt :: String -> Int
parseInt n = read n :: Int

parsePosition :: String -> Coord
parsePosition str = (parseInt left, parseInt top)
  where
    (left : _ : top : _) = splitOnChar ',' str

parseDimension :: String -> Coord
parseDimension str = (parseInt width, parseInt height)
  where
    (width : _ : height : _) = splitOnChar 'x' str

splitOnChar :: Char -> String -> [String]
splitOnChar c str = L.groupBy ((==) `on` (== c)) str

freqMap :: Ord(a) => [a] -> M.Map a Int
freqMap = M.fromListWith (+) . map (\x -> (x,1))

produceClaims :: Order -> [Coord]
produceClaims order =
  let
    c = claim order
    (left, top) = pos c
    (width, height) = dim c
    bottom = top + height - 1
    right = left + width - 1
  in
    [ (row, col) | row <- [top..bottom], col <- [left..right] ]
