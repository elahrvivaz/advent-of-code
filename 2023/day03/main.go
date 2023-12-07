package main

import (
	"fmt"
	"regexp"
	"strconv"
	"strings"
)

type PartNumber struct {
	start   int
	end     int
	value   int
	matched bool
}

func main() {
	symbol := regexp.MustCompile("[^0-9.]")

	lines := strings.Split(sample, "\n")
	// lines := strings.Split(data, "\n")
	sum := 0
	var previousLine []PartNumber
	var currentLine []PartNumber
	var nextLine []PartNumber

	for i := range lines {
		if i == 0 {
			previousLine = []PartNumber{}
			currentLine = findNumbers(lines[i])
			nextLine = findNumbers(lines[i+1])
		} else if i == len(lines)-1 {
			previousLine = currentLine
			currentLine = nextLine
			nextLine = []PartNumber{}
		} else {
			previousLine = currentLine
			currentLine = nextLine
			nextLine = findNumbers(lines[i+1])
		}
		matches := symbol.FindAllStringIndex(lines[i], -1)
		for _, match := range matches {
			pos := match[0]
			if match[1]-pos != 1 {
				panic("found multi-word match " + lines[i])
			}
			for _, line := range [][]PartNumber{previousLine, currentLine, nextLine} {
				for j := range line {
					if (pos >= line[j].start && pos <= line[j].end) ||
						line[j].start-pos == 1 {
						if !line[j].matched {
							sum += line[j].value
							line[j].matched = true
						}
					}
				}
			}
		}

	}
	fmt.Println(sum)
}

func findNumbers(line string) []PartNumber {
	result := []PartNumber{}
	regex := regexp.MustCompile("[0-9]+")
	matches := regex.FindAllStringIndex(line, -1)
	for _, match := range matches {
		start := match[0]
		end := match[1]
		value, _ := strconv.Atoi(line[start:end])
		result = append(result, PartNumber{start, end, value, false})
	}
	return result
}

const sample string = `467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..`
const data string = ``
