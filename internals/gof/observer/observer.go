package observer

import (
	"fmt"
	"math/rand"
	"time"
)

type Observer interface {
	update(string)
	getId() string
}

type Customer struct {
	id string
}

func NewCustomer(id string) *Customer {
	return &Customer{id: id}
}

func (c *Customer) update(itemName string) {
	r := rand.Intn(3)
	time.Sleep(time.Second * time.Duration(r))
	fmt.Printf("Hey hey customer (%s)! The item %s is now available! (waited %d)\n", c.id, itemName, r)
}

func (c *Customer) getId() string {
	return c.id
}

type Salesman struct {
	id string
}

func NewSalesman(id string) *Salesman {
	return &Salesman{id: id}
}

func (s *Salesman) update(itemName string) {
	r := rand.Intn(6)
	time.Sleep(time.Second * time.Duration(r))
	fmt.Printf("Hey hey SALESMAN (%s)! The item %s is now available FOR YOU TO SELL, GO GET THEM TIGER!! (waited %d)\n", s.id, itemName, r)
}

func (s *Salesman) getId() string {
	return s.id
}

// functional observer
type FunctionalObserver interface {
	execute(func(...any)) error
}

type Executioner struct {
	id string
}

func NewExecutioner(id string) *Executioner {
	return &Executioner{id: id}
}

func (e *Executioner) execute(victim string) error {
	fmt.Printf("Hey there %s, The head of the people in %s is going to start rolling\n", e.id, victim)
	return nil
}
