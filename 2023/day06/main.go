package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {
	// lines := getInput(true)
	lines := getInput(false)

	regex := regexp.MustCompile(`\s+`)
	times := strings.Split(regex.ReplaceAllString(lines[0], ""), ":")
	distances := strings.Split(regex.ReplaceAllString(lines[1], ""), ":")
	races := []Race{}

	for i := range times {
		if i != 0 {
			t, _ := strconv.Atoi(times[i])
			d, _ := strconv.Atoi(distances[i])
			races = append(races, Race{time: t, distance: d})
		}
	}

	sum := 1

	for _, race := range races {
		sum *= race.wins()
	}

	fmt.Println(sum)
}

type Race struct {
	time     int
	distance int
}

func (r Race) wins() int {
	fmt.Printf("race time %v distance %v\n", r.time, r.distance)
	count := 0
	hold := r.time / 2
	dist := hold * (r.time - hold)
	for dist > r.distance && hold > 0 {
		// fmt.Printf("hold %v distance %v\n", hold, dist)
		count += 1
		hold -= 1
		dist = hold * (r.time - hold)
	}

	if r.time%2 == 0 {
		count = ((count - 1) * 2) + 1
	} else {
		count *= 2
	}
	fmt.Printf("count %v\n", count)
	fmt.Println()
	return count
}

func getInput(test bool) []string {
	var input string
	if test {
		input = sample
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

const sample string = `Time:      7  15   30
Distance:  9  40  200`
