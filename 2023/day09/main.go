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
		stack := []Hist{}
		cur := parseLine(line)
		done := false
		for !done {
			stack = append(stack, cur)
			cur, done = calcNext(cur)
		}
		stack = append(stack, cur)
		for i := len(stack) - 1; i > 0; i -= 1 {
			stack[i-1] = append([]int{stack[i-1][0] - stack[i][0]}, stack[i-1]...)
			// fmt.Println(stack[i-1])
		}
		sum += stack[0][0]
		// fmt.Println(stack[0].last())
	}

	fmt.Println(sum)
}

func parseLine(line string) Hist {
	strings := strings.Split(line, " ")
	seq := make([]int, len(strings))
	for i := 0; i < len(strings); i += 1 {
		s, _ := strconv.Atoi(strings[i])
		seq[i] = s
	}
	return seq
}

func calcNext(seq Hist) (Hist, bool) {
	done := true
	result := make([]int, len(seq)-1)
	for i := 0; i < len(result); i += 1 {
		result[i] = seq[i+1] - seq[i]
		if result[i] != 0 {
			done = false
		}
	}
	// fmt.Println(seq, result, done)
	return result, done
}

type Hist []int

const data string = ""

const sample string = `0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45`

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
