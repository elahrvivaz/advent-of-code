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
		terrain = append(terrain, parseLine(line))
	}
	print(terrain)
	tiltNorth(terrain)
	print(terrain)

	for i := range terrain {
		weight := len(terrain) - i
		for _, cell := range terrain[i] {
			if cell == RoundRock {
				sum += weight
			}
		}
	}

	fmt.Println(sum)
}

func tiltNorth(terrain [][]Terrain) {
	for j := range terrain[0] {
		to := 0
		for i := range terrain {
			switch terrain[i][j] {
			case RoundRock:
				if to != i {
					terrain[to][j] = RoundRock
					terrain[i][j] = Ground
				}
				to++
			case CubeRock:
				to = i + 1
			case Ground: // no-op
			}
		}
	}
}

func mirror(terrain [][]Terrain, ignore int) int {
	for i := 0; i < len(terrain)-1; i++ {
		if compareRows(terrain, i, i+1) {
			// fmt.Println("found horizontal match at ", i)
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
				result := (i + 1) * 100
				if result != ignore {
					// fmt.Println("horizontal match at ", i)
					return result
				}
				// fmt.Println("ignoring horizontal match at ", i)
			}
		}
	}
	for i := 0; i < len(terrain[0])-1; i++ {
		if compareCols(terrain, i, i+1) {
			// fmt.Println("found vertical match at ", i)
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
				result := i + 1
				if result != ignore {
					// fmt.Println("vertical match at ", i)
					return result
				}
				// fmt.Println("ignoring vertical match at ", i)
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

func print(terrain [][]Terrain) {
	for i := range terrain {
		for j := range terrain[i] {
			fmt.Print(terrain[i][j])
		}
		fmt.Println()
	}
	fmt.Println()
}

type Terrain string

const (
	Ground    Terrain = "."
	CubeRock  Terrain = "#"
	RoundRock Terrain = "O"
)

func parseLine(line string) []Terrain {
	vals := make([]Terrain, len(line))
	for i, char := range line {
		vals[i] = Terrain(char)
	}
	return vals
}

const data string = ""

const sample string = `O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....`

const sample2 string = `...#..#
...#..#
##..##.
.#.##.#
..#..##
..#.##.
#.#.###
##.##..
##.##..
#.#.###
..#.##.
..#..##
.#.##.#
##..##.
...#..#`

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
