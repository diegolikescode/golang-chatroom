package observer

import (
	"errors"
	"fmt"
	"slices"
	"sync"
)

type Subject interface {
	Register(observer Observer)
	Deregister(observer Observer)
	notifyAll()
}

type Item struct {
	observers []Observer
	name      string
	inStock   bool
}

func NewItem(name string) *Item {
	return &Item{name: name}
}

func (i *Item) UpdateAvailability() {
	i.inStock = !i.inStock
	if i.inStock {
		fmt.Printf("The item %s is now in stock!!\n", i.name)
	} else {
		fmt.Printf("The item %s is now OUT_OF_STOCK!!\n", i.name)
	}

	i.notifyAll()
}

func (i *Item) Register(obs Observer) {
	i.observers = append(i.observers, obs)
}

func (i *Item) Deregister(obs Observer) error {
	idx := slices.IndexFunc(i.observers, func(e Observer) bool {
		return e.getId() == obs.getId()
	})

	if idx == -1 {
		return errors.New("NOT_FOUND")
	}

	return nil
}

func (i *Item) notifyAll() {
	var wg sync.WaitGroup

	for _, obs := range i.observers {
		wg.Go(func() { obs.update(i.name) })
	}

	wg.Wait()
}

// now a version with custom function in it
type FunctionalSubject interface {
	Register(observer FunctionalObserver)
	Deregister(observer FunctionalObserver)
	notifyAll()
}

type HeadList struct {
	executioners     []Executioner
	executionAddress string
	thereArePeasants bool
}

func NewHeadList() *HeadList {
	return &HeadList{thereArePeasants: false, executionAddress: ""}
}

func (hl *HeadList) Register(obs Executioner) {
	hl.executioners = append(hl.executioners, obs)
}

func (hl *HeadList) Deregister(obs Executioner) {
	slices.DeleteFunc(hl.executioners, func(e Executioner) bool {
		return obs.id == e.id
	})
}

func (hl *HeadList) UpdateExecutionAddress(newAddress string) {
	hl.executionAddress = newAddress
	hl.thereArePeasants = true

	hl.NotifyAll()
}

func (hl *HeadList) NotifyAll() {
	for _, e := range hl.executioners {
		e.execute(hl.executionAddress)
	}
}
