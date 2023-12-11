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

	mappings := [][]*Mapping{}
	for _, line := range lines {
		mappings = append(mappings, parseLine(line))
	}

	pipes := make([][]Pipe, len(mappings))
	for i := range mappings {
		pipes[i] = make([]Pipe, len(mappings[i]))
	}
	for i := range mappings {
		for j := range mappings[i] {
			if m := mappings[i][j]; m != nil {
				// fmt.Println(i, j, mappings[i][j])
				if m.S && i < len(pipes)-1 {
					pipes[i][j].S = &pipes[i+1][j]
				}
				if m.N && i > 0 {
					pipes[i][j].N = &pipes[i-1][j]
				}
				if m.E && j < len(pipes[i])-1 {
					pipes[i][j].E = &pipes[i][j+1]
				}
				if m.W && j > 0 {
					pipes[i][j].W = &pipes[i][j-1]
				}
			}
		}
	}

	var start *Pipe
	var loop *Pipe
findStart:
	for i := range pipes {
		for j := range pipes[i] {
			if m := mappings[i][j]; m != nil && !m.S && !m.N && !m.E && !m.W {
				start = &pipes[i][j]
				if i > 0 && pipes[i-1][j].S != nil {
					loop = &pipes[i-1][j]
				} else if i < len(pipes)-1 && pipes[i+1][j].N != nil {
					loop = &pipes[i+1][j]
				} else if j > 0 && pipes[i][j-1].E != nil {
					loop = &pipes[i][j-1]
				} else if j < len(pipes[i])-1 && pipes[i][j+1].W != nil {
					loop = &pipes[i][j+1]
				} else {
					panic("Not enough pipes")
				}
				break findStart
			}
		}
	}

	if start == nil {
		panic("Didn't find start node")
	}

	steps := 1
	last := start
	for loop != start {
		steps += 1
		if loop.N != nil && loop.N != last {
			last = loop
			loop = last.N
		} else if loop.E != nil && loop.E != last {
			last = loop
			loop = last.E
		} else if loop.S != nil && loop.S != last {
			last = loop
			loop = last.S
		} else if loop.W != nil && loop.W != last {
			last = loop
			loop = last.W
		} else {
			panic("Not enough pipes")
		}
	}

	fmt.Println(steps)
	fmt.Println(steps / 2)
}

func parseLine(line string) []*Mapping {
	pipes := make([]*Mapping, len(line))
	for i, char := range line {
		switch char {
		case '|':
			pipes[i] = &Mapping{N: true, S: true}
		case '-':
			pipes[i] = &Mapping{E: true, W: true}
		case 'L':
			pipes[i] = &Mapping{N: true, E: true}
		case 'J':
			pipes[i] = &Mapping{N: true, W: true}
		case '7':
			pipes[i] = &Mapping{W: true, S: true}
		case 'F':
			pipes[i] = &Mapping{E: true, S: true}
		case 'S':
			pipes[i] = &Mapping{}
		case '.': // is ground; there is no pipe in this tile.
		default:
			panic(fmt.Sprintf("unexpected label: %v", char))
		}
	}
	return pipes
}

type Mapping struct {
	N bool
	S bool
	E bool
	W bool
}

type Pipe struct {
	N *Pipe
	S *Pipe
	E *Pipe
	W *Pipe
}

type Pos struct {
	cur  *Pipe
	last *Pipe
}

const data string = ""

const sample string = `.....
.S-7.
.|.|.
.L-J.
.....`

const sample2 string = `..F7.
.FJ|.
SJ.L7
|F--J
LJ...`

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
