package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

type replace struct {
	m string
	r string
}

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

	regex := regexp.MustCompile("[^0-9]")
	one := replace{"one", "1"}
	two := replace{"two", "2"}
	three := replace{"three", "3"}
	four := replace{"four", "4"}
	five := replace{"five", "5"}
	six := replace{"six", "6"}
	seven := replace{"seven", "7"}
	eight := replace{"eight", "8"}
	nine := replace{"nine", "9"}
	rs := []replace{one, two, three, four, five, six, seven, eight, nine}

	sum := 0
	for _, line := range lines {
		i := 0
		for i < len(line) {
			for _, r := range rs {
				// fmt.Println(line)
				if strings.Index(line[i:], r.m) == 0 {
					line = line[:i] + r.r + line[i+len(r.r):]
					break
				}
			}
			i += 1
		}
		line = regex.ReplaceAllString(line, "")

		// fmt.Println(line)
		start := line[:1]
		end := line[len(line)-1:]
		num, _ := strconv.Atoi(start + end)
		// fmt.Println(num)
		sum += num
		// fmt.Println("")
	}
	fmt.Println(sum)
}

const sample string = `two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen`
