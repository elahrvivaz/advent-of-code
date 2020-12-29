module Lib
    ( someFunc
    ) where

import System.IO
import Control.Monad

--Right 1, down 1.
--Right 3, down 1. (This is the slope you already checked.)
--Right 5, down 1.
--Right 7, down 1.
--Right 1, down 2.

someFunc :: IO ()
someFunc = do
            contents <- readFile "input"
            let rows = lines contents
            let slopes = [(1,1),(1,3),(1,5),(1,7),(2,1)] :: [(Int, Int)]
            let coords = map (pairs $ length rows) slopes
--            print coords
            let counts = map (trees rows) coords
            print counts
            let mult = product counts
            print mult
--            print head codes
--            let ans = [ (i,j,k) | i <- ints, j <- ints, k <- ints, i + j + k == 2020 ]
--            let (x, y, z) = head ans
--            print x
--            print y
--            print z
--            print (x*y*z)

pairs :: Int -> (Int, Int) -> [(Int, Int)]
pairs len xy =
  let ys = takeWhile (< len) [0,fst xy..]
      xs = [0,snd xy..]
  in zip ys xs

trees :: [String] -> [(Int,Int)] -> Int
trees ground yxs =
  let len = length $ head ground
      hits = filter (tree ground len) yxs
  in length hits
 
tree :: [String] -> Int -> (Int, Int) -> Bool
tree ground len (y, x) = (ground!!y)!!mod x len == '#'
