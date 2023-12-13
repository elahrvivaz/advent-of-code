package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	// lines := getInput(sample)
	// lines := getInput(sample2)
	// lines := getInput(sample3)
	lines := getInput(data)

	sum := 0
	terrain := [][]Terrain{}
	for _, line := range lines {
		if line == "" {
			sum += mirror(terrain)
			terrain = [][]Terrain{}
		} else {
			terrain = append(terrain, parseLine(line))
		}
	}
	if len(terrain) > 0 {
		sum += mirror(terrain)
	}

	fmt.Println(sum)
}

func mirror(terrain [][]Terrain) int {
	for i := 0; i < len(terrain)-1; i++ {
		if compareRows(terrain, i, i+1) {
			// fmt.Println("found match at ", i)
			left := i - 1
			right := i + 2
			matched := true
			for matched && left >= 0 && right < len(terrain) {
				matched = compareRows(terrain, left, right)
				// fmt.Println("comparing ", left, " ", right, " -> ", matched)
				left--
				right++
			}
			if matched {
				// fmt.Println("horizontal match at ", i)
				return (i + 1) * 100
			}
		}
	}
	for i := 0; i < len(terrain[0])-1; i++ {
		if compareCols(terrain, i, i+1) {
			// fmt.Println("found match at ", i)
			left := i - 1
			right := i + 2
			matched := true
			for matched && left >= 0 && right < len(terrain[0]) {
				matched = compareCols(terrain, left, right)
				// fmt.Println("comparing ", left, " ", right, " -> ", matched)
				left--
				right++
			}
			if matched {
				// fmt.Println("horizontal match at ", i)
				return i + 1
			}
		}
	}
	return 0
}

func compareRows(terrain [][]Terrain, r1 int, r2 int) bool {
	for i := range terrain[r1] {
		if terrain[r1][i] != terrain[r2][i] {
			return false
		}
	}
	return true
}

func compareCols(terrain [][]Terrain, c1 int, c2 int) bool {
	for i := range terrain {
		if terrain[i][c1] != terrain[i][c2] {
			return false
		}
	}
	return true
}

type Terrain string

const (
	Ash   Terrain = "."
	Rocks Terrain = "#"
)

func parseLine(line string) []Terrain {
	vals := make([]Terrain, len(line))
	for i, char := range line {
		switch char {
		case '.':
			vals[i] = Ash
		case '#':
			vals[i] = Rocks
		default:
			panic(fmt.Sprintf("unexpected label: %v", char))
		}
	}

	return vals
}

const data string = ""

const sample string = `#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#`

const sample2 string = ``

const sample3 string = ``

func getInput(t string) []string {
	var input string
	if t != "" {
		input = t
	} else {
		if dat, err := os.ReadFile("input.txt"); err == nil {
			input = string(dat)
		} else {
			fmt.Println("Can't read input")
			panic(err)
		}
	}
	return strings.Split(input, "\n")
}

type InputType int

const (
	Sample  InputType = 0
	Sample2 InputType = 1
	Data    InputType = 2
)
