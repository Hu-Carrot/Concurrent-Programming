package queue

import (
	"sync/atomic"
	"unsafe"
)

/*
LinkedQueue
参考:
	https://www.cs.rochester.edu/~scott/papers/1996_PODC_queues.pdf
	https://www.sobyte.net/post/2021-07/implementing-lock-free-queues-with-go/

队列: FIFO 在两端操作的线性表
>使用单链表实现
*/

type LKQueue struct {
	head unsafe.Pointer
	tail unsafe.Pointer
	len  uint64
}

type node struct {
	value interface{}
	next  unsafe.Pointer
}

/*
初始化队列, head 和 tail 都指向空节点
*/
func NewLKQueue() *LKQueue {
	n := unsafe.Pointer(&node{})
	return &LKQueue{head: n, tail: n}
}

/*
TODO 注意:这里cas比较是指针存在ABA问题
	 会导致 head | tail 指向错误(这里不返回节点，所以没这个问题)
	 但依旧会存在 记录丢失的问题
*/

func cas(p *unsafe.Pointer, old, new *node) (ok bool) {
	return atomic.CompareAndSwapPointer(p, unsafe.Pointer(old), unsafe.Pointer(new))
}

/*
入队
	 tail.next = newNode
	 tail = newNode
*/
func (q *LKQueue) Enqueue(v interface{}) {
	n := &node{value: v}
	for {
		tail := (*node)(atomic.LoadPointer(&q.tail))
		next := (*node)(atomic.LoadPointer(&tail.next))
		if tail == (*node)(atomic.LoadPointer(&q.tail)) {
			//如果 next == nil
			if next == nil {
				//尾插法
				if cas(&tail.next, next, n) {
					cas(&q.tail, tail, n)
					atomic.AddUint64(&q.len, 1)
					return
				}
			} else {
				//next不为nil，说明别的协程有添加数据，遍历tail
				cas(&q.tail, tail, next)
			}
		}
	}
}

/*
出队
	 next = head.next
	 head = next
*/
func (q *LKQueue) Dequeue() interface{} {
	for {
		head := (*node)(atomic.LoadPointer(&q.head))
		tail := (*node)(atomic.LoadPointer(&q.tail))
		next := (*node)(atomic.LoadPointer(&head.next))
		if head == (*node)(atomic.LoadPointer(&q.head)) {
			if head == tail {
				if next == nil {
					return nil
				}
				cas(&q.tail, tail, next)
			} else {
				v := next.value
				if cas(&q.head, head, next) {
					atomic.AddUint64(&q.len, ^uint64(0))
					return v
				}
			}
		}
	}
}

func (q *LKQueue)Length() uint64 {
	return atomic.LoadUint64(&q.len)
}

func (q *LKQueue)Empty() bool {
	return atomic.CompareAndSwapUint64(&q.len,0,0)
}