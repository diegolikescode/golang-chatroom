package observer

import (
	"math/rand"
	"testing"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
)

type Custom struct {
	id    string
	value int
}

func NewCustom(id string, value int) *Custom {
	return &Custom{id: id, value: value}
}

func TestPubSubCustomType(t *testing.T) {
	pub := NewDefaultPublisher()

	alright := false
	subId := uuid.NewString()
	subValue := rand.Intn(69420)

	sub := pub.Subscribe(TypeOf[*Custom](), func(event any) {
		val, ok := event.(*Custom)
		assert.True(t, ok)

		assert.EqualValues(t, val.id, subId)
		assert.EqualValues(t, val.value, subValue)

		alright = true
	})

	pub.Publish(NewCustom(subId, subValue))
	assert.True(t, alright)
	assert.NotNil(t, pub.subscribers[TypeOf[*Custom]()])

	assert.EqualValues(t, len(pub.subscribers[TypeOf[*Custom]()]), 1)

	pub.Unsubscribe(TypeOf[*Custom](), sub)
	assert.EqualValues(t, len(pub.subscribers[TypeOf[*Custom]()]), 0)
}
