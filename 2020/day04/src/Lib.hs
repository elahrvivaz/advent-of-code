module Lib
    ( someFunc
    ) where

import System.IO
import Control.Monad
import Data.List (isInfixOf)

--byr (Birth Year)
--iyr (Issue Year)
--eyr (Expiration Year)
--hgt (Height)
--hcl (Hair Color)
--ecl (Eye Color)
--pid (Passport ID)
--cid (Country ID)

--ecl:brn pid:760753108 byr:1931
  --hgt:179cm

someFunc :: IO ()
someFunc = do
            contents <- readFile "input"
            let rows = lines contents
            let passports = foldr group [] rows
            print $ head passports
            let ok = filter valid passports
            print $ length ok

group :: String -> [String] -> [String]
group [] [] = []
group line [] = [line]
group [] grouped = "" : grouped 
group line grouped = (head grouped ++ " " ++ line) : tail grouped

valid :: String -> Bool
valid s =
  let byr = isInfixOf "byr:" s
      iyr = isInfixOf "iyr:" s
      eyr = isInfixOf "eyr:" s
      hgt = isInfixOf "hgt:" s
      hcl = isInfixOf "hcl:" s
      ecl = isInfixOf "ecl:" s
      pid = isInfixOf "pid:" s
  in byr && iyr && eyr && hgt && hcl && ecl && pid