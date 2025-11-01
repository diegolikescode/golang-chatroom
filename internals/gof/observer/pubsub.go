package observer

import (
	"fmt"
	"reflect"
	"slices"
	"sync"
)

type Publisher interface {
	Publish(obj any) int
	Subscribe(eventType reflect.Type, sub Subscriber) Subscriber
	Unsubscribe(eventType reflect.Type, sub Subscriber)
}

type Subscriber interface {
	Execute(event any)
	GetID() string
}

type DefaultSubscriber struct {
	id       string
	consumer func(any)
}

func NewDefaultSubscriber(consumer func(any)) *DefaultSubscriber {
	return &DefaultSubscriber{id: fmt.Sprintf("%p", consumer), consumer: consumer}
}

func (s *DefaultSubscriber) Execute(event any) {
	s.consumer(event)
}

func (s *DefaultSubscriber) GetID() string {
	return s.id
}

type DefaultPublisher struct {
	mu          sync.RWMutex
	subscribers map[reflect.Type][]Subscriber
}

func NewDefaultPublisher() *DefaultPublisher {
	return &DefaultPublisher{subscribers: make(map[reflect.Type][]Subscriber)}
}

func (p *DefaultPublisher) Publish(object any) int {
	p.mu.Lock()
	defer p.mu.Unlock()

	eventType := reflect.TypeOf(object)
	if subs, exists := p.subscribers[eventType]; exists {
		for _, s := range subs {
			s.Execute(object)
		}
		return len(p.subscribers[eventType])
	}

	return 0
}

func (p *DefaultPublisher) Subscribe(eventType reflect.Type, consumer func(any)) Subscriber {
	p.mu.Lock()
	defer p.mu.Unlock()

	sub := NewDefaultSubscriber(consumer)
	p.subscribers[eventType] = append(p.subscribers[eventType], sub)

	return sub
}

func (p *DefaultPublisher) Unsubscribe(eventType reflect.Type, sub Subscriber) {
	p.mu.Lock()
	defer p.mu.Unlock()

	subId := sub.GetID()
	p.subscribers[eventType] = slices.DeleteFunc(p.subscribers[eventType], func(s Subscriber) bool {
		return s.GetID() == subId
	})
}

func TypeOf[T any]() reflect.Type {
	return reflect.TypeOf((*T)(nil)).Elem()
}
