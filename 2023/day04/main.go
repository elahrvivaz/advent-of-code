package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	var input string
	if dat, err := os.ReadFile("input.txt"); err == nil {
		input = string(dat)
	} else {
		fmt.Println("Can't read input")
		panic(err)
	}

	lines := strings.Split(input, "\n")
	// lines = strings.Split(sample, "\n")

	sum := 0

	stack := []int{}
	for i := range lines {
		stack = append(stack, i)
	}
	for len(stack) > 0 {
		sum += 1
		i := stack[0]
		stack = stack[1:]
		line := lines[i][strings.Index(lines[i], ":")+1:]
		parts := strings.Split(line, "|")
		left := strings.Trim(parts[0], " ")
		right := strings.Trim(parts[1], " ")
		winners := strings.Split(left, " ")
		for j := range winners {
			winners[j] = strings.Trim(winners[j], " ")
		}
		mine := strings.Split(right, " ")
		count := 0
		for _, num := range mine {
			num := strings.Trim(num, " ")
			if num != "" {
				for _, winner := range winners {
					if num == winner {
						count += 1
						stack = append(stack, i+count)
						break
					}
				}
			}
		}
	}

	fmt.Println(sum)
}

const sample string = `Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11`
