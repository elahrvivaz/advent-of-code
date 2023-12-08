package main

import (
	"fmt"
	"os"
	"regexp"
	"strings"
)

func main() {
	// lines := getInput(Sample)
	// lines := getInput(Sample2)
	lines := getInput(Data)

	regex := regexp.MustCompile(`([A-Z]{3}) = \(([A-Z]{3}), ([A-Z]{3})\)`)

	nodes := make(map[string]*Node)
	dirs := lines[0]

	for _, line := range lines[2:] {
		matches := regex.FindStringSubmatch(line)
		nodes[matches[1]] = &Node{Name: matches[1], Left: matches[2], Right: matches[3]}
	}

	steps := 0

	cur := nodes["AAA"]
	i := 0
	for cur.Name != "ZZZ" {
		steps += 1
		if dirs[i] == 'L' {
			fmt.Println(cur.Name + "->" + cur.Left)
			cur = nodes[cur.Left]
		} else {
			fmt.Println(cur.Name + "->" + cur.Right)
			cur = nodes[cur.Right]
		}
		if i < len(dirs)-1 {
			i += 1
		} else {
			i = 0
		}
	}

	fmt.Println(steps)
}

type Node struct {
	Name  string
	Left  string
	Right string
}

const sample string = `RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)`

const sample2 string = `LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)`

func getInput(t InputType) []string {
	var input string
	if t == Sample {
		input = sample
	} else if t == Sample2 {
		input = sample2
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
