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
		folded, foldedGroups := parseLine(line)
		springs := folded
		groups := foldedGroups
		for i := 0; i < 4; i++ {
			springs = append(springs, Unknown)
			springs = append(springs, folded...)
			groups = append(groups, foldedGroups...)
		}
		sum += calc(springs, groups)
	}

	fmt.Println("Result: ", sum)
}

func calc(springs []Spring, groups []int) int {
	fmt.Print(springs, groups, " ")
	c := make(chan int)
	go permutations(springs, groups, 0, c)
	count := <-c
	fmt.Println(count)
	return count
}

func permutations(springs []Spring, groups []int, start int, c chan int) {
	unknown := -1
	damaged := make([]Spring, len(springs))
	operationals := make([]Spring, len(springs))
	for i := 0; i < len(springs); i++ {
		if springs[i] == Unknown && unknown == -1 {
			unknown = i
			damaged[i] = Damaged
			operationals[i] = Operational
		} else {
			damaged[i] = springs[i]
			operationals[i] = springs[i]
		}
	}
	if unknown == -1 {
		if checkGroups(springs, groups) {
			c <- 1
		} else {
			c <- 0
		}
	} else if checkPrefix(springs, groups, unknown) {
		d := make(chan int)
		o := make(chan int)
		go permutations(damaged, groups, unknown+1, d)
		go permutations(operationals, groups, unknown+1, o)
		c1 := <-d
		c2 := <-o
		c <- c1 + c2
	} else {
		c <- 0
	}
}

type Spring string

const (
	Operational Spring = "."
	Damaged     Spring = "#"
	Unknown     Spring = "?"
)

func checkPrefix(spring []Spring, groups []int, to int) bool {
	cur := 0
	group := 0
	for i := 0; i < to; i++ {
		switch spring[i] {
		case Operational:
			if cur != 0 {
				if len(groups) <= group || groups[group] != cur {
					return false
				}
				cur = 0
				group++
			}
		case Damaged:
			cur++
		default:
			print(spring)
			panic("Bad spring: " + spring[i])
		}
	}

	return true
}

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
			print(spring)
			panic("Bad spring: " + c)
		}
	}
	if cur != 0 {
		return len(groups)-1 == i && groups[i] == cur
	} else {
		return len(groups) == i
	}
}

func print(spring []Spring) {
	for _, c := range spring {
		fmt.Print(c)
	}
	fmt.Println()
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
