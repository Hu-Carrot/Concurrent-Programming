package rwlock

import (
	"sync"
	"sync/atomic"
)

type RWLock struct {
	S     sync.Mutex
	count uint32
}

func (rw *RWLock) RLock() {
	if atomic.CompareAndSwapUint32(&rw.count, 0, 0) {
		rw.S.Lock()
	}
	atomic.AddUint32(&rw.count, 1)
}

func (rw *RWLock) RUnlock() {
	atomic.AddUint32(&rw.count, ^uint32(0))
	if atomic.CompareAndSwapUint32(&rw.count, 0, 0) {
		rw.S.Unlock()
	}
}

func (rw *RWLock) WLock() {
	rw.S.Lock()
}

func (rw *RWLock) WUnlock() {
	rw.S.Unlock()
}
