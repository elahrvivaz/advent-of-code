package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	// lines := getInput(sample)
	// lines := getInput(sample2)
	// lines := getInput(sample3)
	lines := getInput(data)

	sum := 0
	for _, line := range lines {
		springs, groups := parseLine(line)
		count := 0
		for _, attempt := range permutations(springs) {
			if checkGroups(attempt, groups) {
				count += 1
			}
		}
		fmt.Println(springs, groups, count)
		sum += count
	}

	fmt.Println(sum)
}

func permutations(springs []Spring) [][]Spring {
	firstUnknown := -1
	damaged := make([]Spring, len(springs))
	operationals := make([]Spring, len(springs))
	for i, s := range springs {
		if s == Unknown && firstUnknown == -1 {
			firstUnknown = i
			damaged[i] = Damaged
			operationals[i] = Operational
		} else {
			damaged[i] = springs[i]
			operationals[i] = springs[i]
		}
	}
	if firstUnknown == -1 {
		return [][]Spring{springs}
	} else {
		return append(permutations(damaged), permutations(operationals)...)
	}
}

type Spring string

const (
	Operational Spring = "."
	Damaged     Spring = "#"
	Unknown     Spring = "?"
)

func checkGroups(spring []Spring, groups []int) bool {
	cur := 0
	i := 0
	for _, c := range spring {
		switch c {
		case Operational:
			if cur != 0 {
				if len(groups) <= i || groups[i] != cur {
					return false
				}
				i++
				cur = 0
			}
		case Damaged:
			cur++
		default:
			panic("Bad spring")
		}
	}
	if cur != 0 {
		return len(groups)-1 == i && groups[i] == cur
	} else {
		return len(groups) == i
	}
}

func parseLine(line string) ([]Spring, []int) {
	vals := make([]Spring, 0)
	groups := []int{}
loop:
	for i, char := range line {
		switch char {
		case '.':
			vals = append(vals, Operational)
		case '#':
			vals = append(vals, Damaged)
		case '?':
			vals = append(vals, Unknown)
		case ' ':
			strings := strings.Split(line[i+1:], ",")
			groups = make([]int, len(strings))
			for i, c := range strings {
				p, _ := strconv.Atoi(c)
				groups[i] = p
			}
			break loop
		default:
			panic(fmt.Sprintf("unexpected label: %v", char))
		}
	}

	return vals, groups
}

const data string = ""

const sample string = `???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1`

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
