package main

import (
	"fmt"
	"math"
	"os"
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

	sum := 0
	for _, line := range lines {
		line = line[5:] // strip off "game"
		game, _ := strconv.Atoi(line[0:strings.Index(line, ":")])
		rounds := strings.Split(line[strings.Index(line, ":")+1:], ";")
		red := 0.0
		blue := 0.0
		green := 0.0
		for _, round := range rounds {
			colors := strings.Split(round, ",")
			for _, color := range colors {
				if i := strings.Index(color, " red"); i != -1 {
					num, _ := strconv.Atoi(color[1:i])
					red = math.Max(red, float64(num))
				} else if i := strings.Index(color, " blue"); i != -1 {
					num, _ := strconv.Atoi(color[1:i])
					blue = math.Max(blue, float64(num))
				} else if i := strings.Index(color, " green"); i != -1 {
					num, _ := strconv.Atoi(color[1:i])
					green = math.Max(green, float64(num))
				} else {
					panic("no color in " + color)
				}
			}

		}
		power := red * blue * green
		fmt.Println(fmt.Sprintf("game %v: power %v", game, power))
		sum += int(power)
	}
	fmt.Println(sum)
}

const sample string = `Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green`
