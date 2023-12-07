package main

import (
	"fmt"
	"math"
	"os"
	"strconv"
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

	var lines []string
	lines = strings.Split(input, "\n")
	// lines = strings.Split(sample, "\n")

	seeds := []int64{}
	seedStrings := strings.Split(strings.TrimPrefix(lines[0], "seeds: "), " ")
	for _, i := range seedStrings {
		i64, _ := strconv.ParseInt(i, 10, 64)
		seeds = append(seeds, i64)
	}
	mappings := []*Mapping{}
	var mapping *Mapping

	for _, line := range lines[1:] {
		if line == "" {
			continue
		}
		if strings.HasSuffix(line, " map:") {
			fromTo := strings.Split(strings.TrimSuffix(line, " map:"), "-")
			mapping = &Mapping{source: fromTo[0], destination: fromTo[2], ranges: []Range{}}
			mappings = append(mappings, mapping)
		} else {
			nums := strings.Split(line, " ")
			dest, _ := strconv.ParseInt(nums[0], 10, 64)
			src, _ := strconv.ParseInt(nums[1], 10, 64)
			length, _ := strconv.ParseInt(nums[2], 10, 64)
			mapping.ranges = append(mapping.ranges, Range{destinationStart: dest, sourceStart: src, length: length})
		}
	}

	min := int64(math.MaxInt64)
	var i = 0
	for i < len(seeds) {
		start := seeds[i]
		end := start + seeds[i+1]
		i += 2
		for start < end {
			loc := start
			start += 1
			for _, mapping := range mappings {
				loc = mapping.mapTo(loc)
			}
			if loc < min {
				min = loc
			}
		}
	}

	fmt.Println(min)
}

type Mapping struct {
	source      string
	destination string
	ranges      []Range
}

type Range struct {
	destinationStart int64
	sourceStart      int64
	length           int64
}

func (m Mapping) mapTo(in int64) int64 {
	for _, r := range m.ranges {
		if out, match := r.mapTo(in); match {
			return out
		}
	}
	return in
}

func (r Range) mapTo(in int64) (int64, bool) {
	if in >= r.sourceStart && in < r.sourceStart+r.length {
		return (in - r.sourceStart) + r.destinationStart, true
	} else {
		return -1, false
	}
}

const sample string = `seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4`

