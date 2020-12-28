module Lib
    ( someFunc
    ) where

import System.IO  
import Control.Monad


someFunc :: IO ()
someFunc = do
            contents <- readFile "input"
            let codes = lines contents
            let ints = map readInt codes
            let ans = [ (i,j) | i <- ints, j <- ints, i + j == 2020 ]
            let (x, y) = head ans
            print x
            print y
            print (x*y)

readInt :: String -> Int
readInt = read
