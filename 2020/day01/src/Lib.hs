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
            let ans = [ (i,j,k) | i <- ints, j <- ints, k <- ints, i + j + k == 2020 ]
            let (x, y, z) = head ans
            print x
            print y
            print z
            print (x*y*z)

readInt :: String -> Int
readInt = read
