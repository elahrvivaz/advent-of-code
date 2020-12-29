module Lib
    ( someFunc
    ) where

import System.IO
import Control.Monad

import Text.Regex.Base
import Text.Regex.Posix
import Data.List (isInfixOf)

--byr (Birth Year)
--iyr (Issue Year)
--eyr (Expiration Year)
--hgt (Height)
--hcl (Hair Color)
--ecl (Eye Color)
--pid (Passport ID)
--cid (Country ID)

someFunc :: IO ()
someFunc = do
            contents <- readFile "input"
            let rows = lines contents
--            mapM_ print rows
            let passports = foldr group [] rows
--            mapM_ print passports
--            print $ head passports
            let ok = filter valid passports
            mapM_ print ok
            print $ length ok
--            let h = head ok
--            print h
--            let matches = h =~ "hgt:([0-9]{2})in" :: [[String]]
--            print matches
--            let matches2 = h =~ "hgt:([0-9]{3})cm" :: [[String]]
--            print matches2

group :: String -> [String] -> [String]
group "" [] = []
group line [] = [line]
group "" grouped = "" : grouped 
group line grouped = (head grouped ++ " " ++ line) : tail grouped

valid :: String -> Bool
valid s = validByr s && validIyr s && validEyr s && validHgt s && validHcl s && validEcl s && validPid s

--byr (Birth Year) - four digits; at least 1920 and at most 2002.
validByr :: String -> Bool
validByr s =
  let matches = s =~ "byr:([0-9]{4})" :: [[String]]
  in not (null matches) &&
    let m = head matches
        i = read $ m!!1 :: Int
    in i >= 1920 && i <= 2002

--iyr (Issue Year) - four digits; at least 2010 and at most 2020.
validIyr :: String -> Bool
validIyr s =
  let matches = s =~ "iyr:([0-9]{4})" :: [[String]]
  in not (null matches) &&
    let m = head matches
        i = read $ m!!1 :: Int
    in i >= 2010 && i <= 2020

--eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
validEyr :: String -> Bool
validEyr s =
  let matches = s =~ "eyr:([0-9]{4})" :: [[String]]
  in not (null matches) &&
    let m = head matches
        i = read $ m!!1 :: Int
    in i >= 2020 && i <= 2030

--hgt (Height) - a number followed by either cm or in:
--If cm, the number must be at least 150 and at most 193.
--If in, the number must be at least 59 and at most 76.
validHgt :: String -> Bool
validHgt s = validCm s || validIn s

validCm :: String -> Bool
validCm s =
  let matches = s =~ "hgt:([0-9]{3})cm" :: [[String]]
  in not (null matches) &&
      let m = head matches
          i = read $ m!!1 :: Int
      in i >= 150 && i <= 193

validIn :: String -> Bool
validIn s =
  let matches = s =~ "hgt:([0-9]{2})in" :: [[String]]
  in not (null matches) &&
      let m = head matches
          i = read $ m!!1 :: Int
      in i >= 59 && i <= 76

--hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
validHcl :: String -> Bool
validHcl s =
  let matches = s =~ "hcl:#([0-9a-f]{6})" :: [[String]]
  in not (null matches)

--ecl (Eye Color) - exactly one of: .
validEcl :: String -> Bool
validEcl s =
  let matches = s =~ "ecl:(amb|blu|brn|gry|grn|hzl|oth)" :: [[String]]
  in not (null matches)

--pid (Passport ID) - a nine-digit number, including leading zeroes.
validPid :: String -> Bool
validPid s =
  let matches = s =~ "pid:([0-9]+)($|\\s)" :: [[String]]
  in not (null matches) &&
    let m = head matches
        i = m!!1
    in length i == 9
