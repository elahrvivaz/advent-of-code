package main

import (
	"fmt"
	"strings"
)

func main() {
	// lines := strings.Split(sample, "\n")
	lines := strings.Split(data, "\n")
	sum := 0

	for _, line := range lines {
		fmt.Println(line)
		line = line[strings.Index(line, ":")+1:]
		fmt.Println(line)
		parts := strings.Split(line, "|")
		left := strings.Trim(parts[0], " ")
		right := strings.Trim(parts[1], " ")
		winners := strings.Split(left, " ")
		for i := range winners {
			winners[i] = strings.Trim(winners[i], " ")
		}
		mine := strings.Split(right, " ")
		count := 0
		for _, num := range mine {
			num := strings.Trim(num, " ")
			if num != "" {
				for _, winner := range winners {
					if num == winner {
						if count == 0 {
							count = 1
						} else {
							count *= 2
						}
						break
					}
				}
			}
		}
		sum += count
	}
	fmt.Println(sum)
}

const sample string = `Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11`
const data string = ``
