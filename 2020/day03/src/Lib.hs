module Lib
    ( someFunc
    ) where

import System.IO
import Control.Monad

--1-3 a: abcde
--1-3 b: cdefg
--2-9 c: ccccccccc

someFunc :: IO ()
someFunc = do
            contents <- readFile "input"
            let rows = lines contents
            let len = length $ head rows
            let ys = takeWhile (< length rows) [0,1..]
            let xs = [0,3..]
            let pairs = zip ys xs
            let hits = filter (tree rows len) pairs 
            print $ length hits
--            print head codes
--            let ans = [ (i,j,k) | i <- ints, j <- ints, k <- ints, i + j + k == 2020 ]
--            let (x, y, z) = head ans
--            print x
--            print y
--            print z
--            print (x*y*z)

tree :: [String] -> Int -> (Int, Int) -> Bool
tree ground len (y, x) = (ground!!y)!!mod x len == '#'
