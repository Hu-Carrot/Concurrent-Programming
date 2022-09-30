package queue

import (
	"fmt"
	"testing"
)


func TestNewLKQueue(t *testing.T) {

	q := NewLKQueue()

	go func() {
		for i := 0; i < 10 ; i++ {
			q.Enqueue(i)
			fmt.Println("1 enQueue:",i)
		}
	}()

	go func() {
		for i := 10; i < 20 ; i++ {
			q.Enqueue(i)
			fmt.Println("2 enQueue:",i)
		}
	}()

	go func() {
		for  {
			if !q.Empty() {
				fmt.Println(q.Dequeue())
			}
		}
	}()

	for  {
		fmt.Print()
	}
}