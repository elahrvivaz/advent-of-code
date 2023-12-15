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

	boxes := make([][]Lens, 256)
	for i := range boxes {
		boxes[i] = make([]Lens, 0)
	}

	for _, line := range lines {
		words := parseLine(line)
		for _, word := range words {
			if word[len(word)-1] == '-' {
				label := word[0 : len(word)-1]
				h := hash(label)
				box := boxes[h]
				for i := range box {
					if box[i].label == label {
						for j := i; j < len(box)-1; j++ {
							box[j] = box[j+1]
						}
						boxes[h] = box[0 : len(box)-1]
					}
					break
				}
			} else {
				i := strings.Index(word, "=")
				if i == -1 {
					panic("invalid word: " + word)
				}
				label := word[0:i]
				focalLength, _ := strconv.Atoi(word[i+1:])
				lens := Lens{label, focalLength}
				h := hash(label)
				box := boxes[h]
				replaced := false
				for i := range box {
					if box[i].label == label {
						box[i] = lens
						replaced = true
						break
					}
				}
				if !replaced {
					boxes[h] = append(box, lens)
				}
			}
			// fmt.Println("After ", word)
			// print(boxes)
			// fmt.Println()
		}
	}

	sum := 0
	for i, box := range boxes {
		for j, lens := range box {
			sum += (1 + i) * (1 + j) * lens.focalLength
		}
	}
	fmt.Println(sum)
}

func hash(val string) int {
	cur := 0
	for _, c := range val {
		cur += int(c)
		cur = cur * 17
		cur = cur % 256
	}
	return cur
}

func print(boxes [][]Lens) {
	for i, box := range boxes {
		if len(box) > 0 {
			fmt.Print("Box ", i, ":")
			printBox(box)

		}
	}
}

func printBox(box []Lens) {
	for _, lens := range box {
		fmt.Print(" [", lens.label, " ", lens.focalLength, "]")
	}
	fmt.Println()
}

type Lens struct {
	label       string
	focalLength int
}

func parseLine(line string) []string {
	return strings.Split(line, ",")
}

const data string = ""

const sample string = `rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7`

const sample2 string = `HASH`

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
