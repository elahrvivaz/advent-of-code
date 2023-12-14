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

	states := [][][]Terrain{}
	iterations := 1000000000
loop:
	for i := 0; i < iterations; i++ {
		spinCycle(terrain)
		for j := range states {
			if compare(terrain, states[j]) {
				loop := i - j
				fmt.Println("found loop of length ", loop, " from ", j, " to ", i)
				k := i + 1
				for k+loop < iterations {
					k += loop
				}
				fmt.Println("skipping to ", k)
				for k < iterations {
					spinCycle(terrain)
					k++
				}
				break loop

			}
		}
		states = append(states, copy(terrain))
	}

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

func tiltSouth(terrain [][]Terrain) {
	for j := len(terrain[0]) - 1; j >= 0; j-- {
		to := len(terrain) - 1
		for i := len(terrain) - 1; i >= 0; i-- {
			switch terrain[i][j] {
			case RoundRock:
				if to != i {
					terrain[to][j] = RoundRock
					terrain[i][j] = Ground
				}
				to--
			case CubeRock:
				to = i - 1
			case Ground: // no-op
			}
		}
	}
}

func tiltEast(terrain [][]Terrain) {
	for i := range terrain {
		to := len(terrain[i]) - 1
		for j := len(terrain[i]) - 1; j >= 0; j-- {
			switch terrain[i][j] {
			case RoundRock:
				if to != j {
					terrain[i][to] = RoundRock
					terrain[i][j] = Ground
				}
				to--
			case CubeRock:
				to = j - 1
			case Ground: // no-op
			}
		}
	}
}

func tiltWest(terrain [][]Terrain) {
	for i := range terrain {
		to := 0
		for j := range terrain[i] {
			switch terrain[i][j] {
			case RoundRock:
				if to != j {
					terrain[i][to] = RoundRock
					terrain[i][j] = Ground
				}
				to++
			case CubeRock:
				to = j + 1
			case Ground: // no-op
			}
		}
	}
}

func spinCycle(terrain [][]Terrain) {
	tiltNorth(terrain)
	tiltWest(terrain)
	tiltSouth(terrain)
	tiltEast(terrain)
}

func compare(t1 [][]Terrain, t2 [][]Terrain) bool {
	for i := range t1 {
		for j := range t1[i] {
			if t1[i][j] != t2[i][j] {
				return false
			}
		}
	}
	return true
}

func copy(terrain [][]Terrain) [][]Terrain {
	res := make([][]Terrain, len(terrain))
	for i := range terrain {
		res[i] = make([]Terrain, len(terrain[i]))
		for j := range terrain[i] {
			res[i][j] = terrain[i][j]
		}
	}
	return res
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
