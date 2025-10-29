package main

import (
	"fmt"

	"github.com/diegolikescode/patterns/internals/gof/strategy"
)

func main() {
	fmt.Println("Starting STRATEGY")

	uidGen := strategy.NewIdGenerator(strategy.NewUuidStrategy())
	uid, err := uidGen.ExecuteStrategy()
	if err != nil {
		panic(err)
	}
	fmt.Println("UUID_STRATEGY GENERATED", uid)

	randGen := strategy.NewIdGenerator(strategy.NewUuidStrategy())
	randId, err := randGen.ExecuteStrategy()
	if err != nil {
		panic(err)
	}
	fmt.Println("RANDOM_STRATEGY GENERATED", randId)
}
