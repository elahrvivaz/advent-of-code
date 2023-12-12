package main

import (
	"fmt"
	"math"
	"os"
	"strings"
)

func main() {
	// lines := getInput(sample)
	// lines := getInput(sample2)
	// lines := getInput(sample3)
	lines := getInput(data)

	starMap := [][]int{}
	for _, line := range lines {
		starMap = append(starMap, parseLine(line))
	}

	print(starMap)

	expandRows := map[int]bool{}
	expandCols := map[int]bool{}

	for i := len(starMap) - 1; i >= 0; i-- {
		expand := true
		for j := range starMap[i] {
			if starMap[i][j] != 0 {
				expand = false
				break
			}
		}
		if expand {
			expandRows[i] = true
		}
	}

	for j := len(starMap[0]) - 1; j >= 0; j-- {
		expand := true
		for i := range starMap {
			if starMap[i][j] != 0 {
				expand = false
				break
			}
		}
		if expand {
			expandCols[j] = true
		}
	}

	// for _, e := range expandRows {
	// 	starMap = append(starMap, starMap[len(starMap)-1])
	// 	for i := len(starMap) - 2; i > e; i-- {
	// 		starMap[i] = starMap[i-1]

	// 	}

	// }

	// for _, e := range expandCols {
	// 	for i := range starMap {
	// 		starMap[i] = append(starMap[i], starMap[i][len(starMap[i])-1])
	// 		for j := len(starMap[i]) - 2; j > e; j-- {
	// 			starMap[i][j] = starMap[i][j-1]
	// 		}
	// 	}
	// }

	print(starMap)

	// number the galaxies
	count := 0
	locations := []Loc{}
	for i := range starMap {
		for j := range starMap[i] {
			if starMap[i][j] != 0 {
				count += 1
				starMap[i][j] = count
				locations = append(locations, Loc{i: i, j: j})
			}
		}
	}

	print(starMap)

	mult := 999999
	sum := 0
	for g1, loc1 := range locations {
		for g2, loc2 := range locations {
			if g1 < g2 {
				numExpansions := 0
				for i := min(loc1.i, loc2.i) + 1; i < max(loc1.i, loc2.i); i++ {
					if expandRows[i] {
						numExpansions++
					}
				}
				for j := min(loc1.j, loc2.j) + 1; j < max(loc1.j, loc2.j); j++ {
					if expandCols[j] {
						numExpansions++
					}
				}
				dist := int(math.Abs(float64(loc1.i-loc2.i)) + math.Abs(float64(loc1.j-loc2.j)))
				// fmt.Println("dist ", g1+1, g2+1, dist)
				// fmt.Println("dist ", g1+1, g2+1, dist+(numExpansions*mult))
				sum += dist + (numExpansions * mult)
			}
		}
	}

	fmt.Println(sum)
}

func min(i1 int, i2 int) int {
	return int(math.Min(float64(i1), float64(i2)))
}

func max(i1 int, i2 int) int {
	return int(math.Max(float64(i1), float64(i2)))
}

func parseLine(line string) []int {
	vals := make([]int, len(line))
	for i, char := range line {
		switch char {
		case '.':
			vals[i] = 0
		case '#':
			vals[i] = 1
		default:
			panic(fmt.Sprintf("unexpected label: %v", char))
		}
	}
	return vals
}

func print(starMap [][]int) {
	for i := range starMap {
		for j := range starMap[i] {
			fmt.Print(starMap[i][j])
		}
		fmt.Println()
	}
	fmt.Println()
}

type Loc struct {
	i int
	j int
}

const data string = ""

const sample string = `...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....`

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
