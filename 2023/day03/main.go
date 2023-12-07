package main

import (
	"fmt"
	"os"
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
	var input string
	if dat, err := os.ReadFile("input.txt"); err == nil {
		input = string(dat)
	} else {
		fmt.Println("Can't read input")
		panic(err)
	}

	lines := strings.Split(input, "\n")
	// lines = strings.Split(sample, "\n")

	symbol := regexp.MustCompile(`\*`)

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
			allLines := append(append(previousLine, currentLine...), nextLine...)
			matched := 0
			ratio := 1
			for _, part := range allLines {
				if (pos >= part.start && pos <= part.end) ||
					part.start-pos == 1 {
					matched += 1
					ratio *= part.value
				}
			}
			if matched == 2 {
				sum += ratio
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
