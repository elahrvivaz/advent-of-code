module Lib
    ( someFunc
    ) where

import System.IO
import Control.Monad
import Text.Regex.Base
import Text.Regex.Posix

--1-3 a: abcde
--1-3 b: cdefg
--2-9 c: ccccccccc

someFunc :: IO ()
someFunc = do
            contents <- readFile "input"
            let rows = lines contents
            let codes = map readCode rows
            let c = length $ filter ok codes 
            print c
--            print head codes
--            let ans = [ (i,j,k) | i <- ints, j <- ints, k <- ints, i + j + k == 2020 ]
--            let (x, y, z) = head ans
--            print x
--            print y
--            print z
--            print (x*y*z)

ok :: (Int, Int, Char, String) -> Bool
ok (minInt, maxInt, char, pwd) =
  let match1 = pwd!!(minInt - 1) == char
      match2 = pwd!!(maxInt - 1) == char
  in (match1 || match2) && not (match1 && match2)

readCode :: String -> (Int, Int, Char, String)
readCode s =
  let matches = s =~ "([0-9]+)\\-([0-9]+) (\\w)\\: (\\w+)" :: [[String]]
      m = head matches
      minInt = read $ m!!1 :: Int
      maxInt = read $ m!!2 :: Int
      char = head $ m!!3
  in (minInt, maxInt, char, m!!4)
