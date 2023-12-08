package main

import (
	"cmp"
	"fmt"
	"os"
	"slices"
	"strconv"
	"strings"
)

func main() {
	lines := getInput(true)
	// lines := getInput(false)

	hands := []*Hand{}
	for _, line := range lines {
		parts := strings.Split(line, " ")
		bid, _ := strconv.Atoi(parts[1])
		cards := []int{}
		for _, c := range parts[0] {
			switch c {
			case 'A':
				cards = append(cards, 14)
			case 'K':
				cards = append(cards, 13)
			case 'Q':
				cards = append(cards, 12)
			case 'J':
				cards = append(cards, 1)
			case 'T':
				cards = append(cards, 10)
			default:
				cards = append(cards, int(c-'0'))
			}

		}
		hands = append(hands, &Hand{Cards: cards, Type: getType(parts[0]), Bid: bid})
	}

	handComparison := func(a, b *Hand) int {
		if c := cmp.Compare(a.Type, b.Type); c != 0 {
			return c
		}
		i := 0
		for i < 5 {
			if c := cmp.Compare(a.Cards[i], b.Cards[i]); c != 0 {
				return c
			}
			i += 1
		}
		return 0
	}

	slices.SortFunc(hands, handComparison)

	sum := 0

	for i := range hands {
		sum += (i + 1) * hands[i].Bid
		// fmt.Println(hands[i])
	}

	fmt.Println(sum)
}

type Hand struct {
	Cards []int
	Type  Type
	Bid   int
}

type Type int

const (
	FiveOfAKind  Type = 7
	FourOfAKind  Type = 6
	FullHouse    Type = 5
	ThreeOfAKind Type = 4
	TwoPair      Type = 3
	OnePair      Type = 2
	HighCard     Type = 1
)

func getType(cards string) Type {
	counts := make(map[rune]int)
	for _, card := range cards {
		counts[card] = counts[card] + 1
	}
	jokers := counts['J']
	if length := len(counts); length == 5 {
		if jokers == 0 {
			return HighCard
		} else {
			return OnePair
		}
	} else if length == 4 {
		if jokers == 0 {
			return OnePair
		} else {
			return ThreeOfAKind
		}
	} else if length == 3 {
		for _, value := range counts {
			if value == 2 {
				if jokers == 0 {
					return TwoPair
				} else if jokers == 1 {
					return FullHouse
				} else { // jokers == 2
					return FourOfAKind
				}
			} else if value == 3 {
				if jokers == 0 {
					return ThreeOfAKind
				} else { // jokers == 1 || jokers == 3
					return FourOfAKind
				}

			}
		}
		panic("Unexpected counts: " + cards)
	} else if length == 2 {
		for _, value := range counts {
			if value == 2 || value == 3 {
				if jokers == 0 {
					return FullHouse
				} else {
					return FiveOfAKind
				}
			} else if value == 4 {
				if jokers == 0 {
					return FourOfAKind
				} else {
					return FiveOfAKind
				}
			}
		}
		panic("Unexpected counts: " + cards)
	} else { // length == 1
		return FiveOfAKind
	}
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

const sample string = `32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483`
