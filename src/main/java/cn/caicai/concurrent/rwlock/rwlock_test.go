package rwlock

import (
	"fmt"
	"sync"
	"testing"
)

func TestRWLock(t *testing.T) {
	rwLock := RWLock{
		S:     sync.Mutex{},
		count: 0,
	}
	text := "bar"
	var wg sync.WaitGroup
	wg.Add(5)
	go func() {
		defer wg.Done()
		rwLock.RLock()
		fmt.Printf("reader1:%s\n", text)
		rwLock.RUnlock()
	}()
	go func() {
		defer wg.Done()
		rwLock.RLock()
		fmt.Printf("reader2:%s\n", text)
		rwLock.RUnlock()
	}()
	go func() {
		defer wg.Done()
		rwLock.RLock()
		fmt.Printf("reader3:%s\n", text)
		rwLock.RUnlock()
	}()

	go func() {
		defer wg.Done()
		rwLock.WLock()
		text = text + "foo"
		fmt.Printf("writer1:%s\n", text)
		rwLock.WUnlock()
	}()
	go func() {
		defer wg.Done()
		rwLock.WLock()
		text = text + "foo"
		fmt.Printf("writer2:%s\n", text)
		rwLock.WUnlock()
	}()

	wg.Wait()
}
