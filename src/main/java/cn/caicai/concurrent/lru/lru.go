package lru

type lru struct {

}

type node struct {
	pre *node
	next *node
	value interface{}
}

func NewLru() lru {
	return lru{}
}