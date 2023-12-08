package main

import (
	"fmt"
	"os"
	"regexp"
	"strings"
)

func main() {
	// lines := getInput(sample)
	// lines := getInput(sample2)
	lines := getInput(sample3)
	// lines := getInput(data)

	regex := regexp.MustCompile(`([1-9A-Z]{3}) = \(([1-9A-Z]{3}), ([1-9A-Z]{3})\)`)

	nodeMap := make(map[string]*Node)
	dirs := lines[0]
	cur := []*Node{}

	for _, line := range lines[2:] {
		matches := regex.FindStringSubmatch(line)
		node := &Node{Name: matches[1], LeftName: matches[2], RightName: matches[3], Terminal: matches[1][2] == 'Z'}
		nodeMap[node.Name] = node
		if node.Name[2] == 'A' {
			cur = append(cur, node)
		}
	}

	for _, n := range nodeMap {
		n.Left = nodeMap[n.LeftName]
		n.Right = nodeMap[n.RightName]
	}

	steps := 0
	complete := []int{}

	for i := 0; len(complete) < len(cur); {
		steps += 1
		if dirs[i] == 'L' {
			for j := range cur {
				// fmt.Println(cur[j].Name + "->" + cur[j].Left)
				cur[j] = cur[j].Left
				if cur[j].Terminal {
					complete = append(complete, steps)
				}
			}
		} else {
			for j := range cur {
				// fmt.Println(cur[j].Name + "->" + cur[j].Right)
				cur[j] = cur[j].Right
				if cur[j].Terminal {
					complete = append(complete, steps)
				}
			}
		}

		// fmt.Println()
		if i < len(dirs)-1 {
			i += 1
		} else {
			i = 0
		}
	}

	// find LCM of the steps for each node
	multiples := []int{}
	multiples = append(multiples, complete...)

	for i := next(multiples); i != -1; i = next(multiples) {
		multiples[i] += complete[i]
		// fmt.Println(multiples)
	}

	fmt.Println(multiples[0])
}

func next(mult []int) int {
	done := true
	min := 0
	for i := range mult[1:] {
		if mult[i+1] < mult[min] {
			min = i + 1
			done = false
		} else if mult[i+1] != mult[min] {
			done = false
		}
	}
	if done {
		return -1
	}
	return min
}

type Node struct {
	Name      string
	LeftName  string
	RightName string
	Left      *Node
	Right     *Node
	Terminal  bool
}

const data string = ""

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

const sample3 string = `LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)`

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
