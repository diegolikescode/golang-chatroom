package main

import (
	"fmt"
	"log"
	"math"

	"github.com/diegolikescode/patterns/internals/gof/observer"
	"github.com/google/uuid"
)

type Custom struct {
	id    string
	value int
}

func NewCustom(id string, val int) *Custom {
	return &Custom{id: id, value: val}
}

func main() {
	fmt.Println("STARTS THE OBSERVER")

	eventType := observer.TypeOf[string]()

	pub := observer.NewDefaultPublisher()
	history := []any{}

	sub1 := pub.Subscribe(eventType, func(event any) {
		history = append(history, event)
		fmt.Printf("Sub1 received %v\n", event)
	})

	sub2 := pub.Subscribe(eventType, func(event any) {
		history = append(history, event)
		fmt.Printf("Sub2 received %v\n", event)
	})

	subInt := pub.Subscribe(observer.TypeOf[int](), func(n any) {
		num, ok := n.(int)
		if !ok {
			log.Println("subInt error :: invalid type")
		}

		pow := math.Pow(float64(num), 4)
		fmt.Println("this is my val babyyyy", pow)
	})

	subCustom := pub.Subscribe(observer.TypeOf[*Custom](), func(event any) {
		val, ok := event.(*Custom)
		if !ok {
			log.Println("subCustom error :: invalid type")
		}
		fmt.Printf("MY CUSTOM:: ID == %s | VALUE == %d\n", val.id, val.value)
	})

	pub.Publish("MY_VALUE_PUBLISHED")
	pub.Publish(2)
	pub.Publish(NewCustom(uuid.NewString(), 69420))

	pub.Unsubscribe(eventType, sub1)
	pub.Unsubscribe(eventType, sub2)
	pub.Unsubscribe(observer.TypeOf[int](), subInt)
	pub.Unsubscribe(observer.TypeOf[Custom](), subCustom)
}
