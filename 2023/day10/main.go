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
				if m.W && j > 0 {
					pipes[i][j].W = &pipes[i][j-1]
				}
				if m.E && j < len(pipes[i])-1 {
					pipes[i][j].E = &pipes[i][j+1]
				}
				if m.S && i < len(pipes)-1 {
					pipes[i][j].S = &pipes[i+1][j]
				}
				if m.N && i > 0 {
					pipes[i][j].N = &pipes[i-1][j]
				}
			}
		}
	}

	// fmt.Println(sample)
	// fmt.Println()

	var start *Pipe
	var loop *Pipe
findStart:
	for i := range pipes {
		for j := range pipes[i] {
			if m := mappings[i][j]; m != nil && !m.S && !m.N && !m.E && !m.W {
				start = &pipes[i][j]
				if i > 0 && pipes[i-1][j].S != nil {
					loop = &pipes[i-1][j]
					start.N = loop
				}
				if i < len(pipes)-1 && pipes[i+1][j].N != nil {
					loop = &pipes[i+1][j]
					start.S = loop
				}
				if j > 0 && pipes[i][j-1].E != nil {
					loop = &pipes[i][j-1]
					start.W = loop
				}
				if j < len(pipes[i])-1 && pipes[i][j+1].W != nil {
					loop = &pipes[i][j+1]
					start.E = loop
				}
				if loop == nil {
					panic("Not enough pipes")
				}
				break findStart
			}
		}
	}

	if start == nil {
		panic("Didn't find start node")
	}
	start.loop = true
	loop.loop = true

	last := start
	for loop != start {
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
		loop.loop = true
	}

	// for i := range pipes {
	// 	for j := range pipes[i] {
	// 		fmt.Print(pipes[i][j])
	// 	}
	// 	fmt.Println()
	// }
	// fmt.Println()

	for i := range pipes {
		interior := false
		var cross *Pipe
		for j := range pipes[i] {
			if p := pipes[i][j]; p.loop {
				if p.N != nil && p.S != nil {
					interior = !interior
				} else if p.E == nil || p.W == nil {
					if cross == nil {
						cross = &p
					} else {
						if (cross.N == nil && p.S == nil) || (cross.S == nil && p.N == nil) {
							interior = !interior
						}
						cross = nil
					}
				}
			} else {
				pipes[i][j].ground.interior = interior
			}

		}
		// fmt.Println()
	}
	fmt.Println()

	for i := range pipes {
		for j := range pipes[i] {
			fmt.Print(&pipes[i][j])
		}
		fmt.Println()
	}
	fmt.Println()

	sum := 0
	for i := range pipes {
		for j := range pipes[i] {
			if pipes[i][j].ground.interior {
				sum += 1
			}
		}
	}

	fmt.Println(sum)
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
	N      *Pipe
	S      *Pipe
	E      *Pipe
	W      *Pipe
	loop   bool
	ground Ground
}

type Ground struct {
	interior bool
}

func (p *Pipe) String() string {
	// if p == nil {
	// 	return "."
	// }
	if !p.loop {
		if p.ground.interior {
			return "I"
		} else {
			return "O"
		}
	}
	if p.N != nil {
		if p.S != nil {
			return "|"
		} else if p.E != nil {
			return "L"
		} else if p.W != nil {
			return "J"
		}
	} else if p.E != nil {
		if p.S != nil {
			return "F"
		} else if p.N != nil {
			return "L"
		} else if p.W != nil {
			return "-"
		}
	} else if p.S != nil {
		if p.E != nil {
			return "F"
		} else if p.N != nil {
			return "|"
		} else if p.W != nil {
			return "7"
		}
	} else if p.W != nil {
		if p.S != nil {
			return "7"
		} else if p.N != nil {
			return "J"
		} else if p.E != nil {
			return "-"
		}
	}
	return "&"
}

func (p *Mapping) String() string {
	if p == nil {
		return "."
	}
	if p.N {
		if p.S {
			return "|"
		} else if p.E {
			return "L"
		} else if p.W {
			return "J"
		}
	} else if p.E {
		if p.S {
			return "F"
		} else if p.N {
			return "L"
		} else if p.W {
			return "-"
		}
	} else if p.S {
		if p.E {
			return "F"
		} else if p.N {
			return "|"
		} else if p.W {
			return "7"
		}
	} else if p.W {
		if p.S {
			return "7"
		} else if p.N {
			return "J"
		} else if p.E {
			return "-"
		}
	}
	return "&"
}

const data string = ""

const sample string = `...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........`

const sample2 string = `.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...`

const sample3 string = `FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L`

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
