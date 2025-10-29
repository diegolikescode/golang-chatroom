package strategy

import (
	"crypto/rand"
	"encoding/base64"

	"github.com/google/uuid"
)

var DEFAULT_BYTESIZE = 16

type IdGenerator struct {
	strategy Strategy
}

func (ig *IdGenerator) ExecuteStrategy() (string, error) {
	return ig.strategy.execute()
}

func NewIdGenerator(s Strategy) *IdGenerator {
	return &IdGenerator{
		strategy: s,
	}
}

type Strategy interface {
	execute() (string, error)
}

type uuidStrategy struct{}

func (s *uuidStrategy) execute() (string, error) {
	return uuid.NewString(), nil
}

func NewUuidStrategy() *uuidStrategy {
	return &uuidStrategy{}
}

type randomGenStrategy struct{}

func (s *randomGenStrategy) execute() (string, error) {
	bytes := make([]byte, DEFAULT_BYTESIZE)
	_, err := rand.Read(bytes)
	if err != nil {
		return "", err
	}
	val := base64.StdEncoding.EncodeToString(bytes)
	return val, nil
}

func NewRandom() *randomGenStrategy {
	return &randomGenStrategy{}
}
