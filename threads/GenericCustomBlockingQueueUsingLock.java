package learn.threads;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GenericCustomBlockingQueueUsingLock<T> {
	private Queue<T> queue = new LinkedList<T>();
	 
    private Lock lock = new ReentrantLock();
    private Condition putCondition = lock.newCondition();
    private Condition takeCondition = lock.newCondition();
 
    private int size;
 
    public GenericCustomBlockingQueueUsingLock(int size) {
        this.size = size;
    }
 
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                takeCondition.await();
            }
            T data = queue.poll();
 
            //If say the queue is full before we take, then chances are threads trying to put would be waiting, so after taking the
            //element we will inform put threads that there is space now for you to put.
            putCondition.signal();
            return data;
        } finally {
            lock.unlock();
        }
    }
 
    public void put(T obj) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == size) {
                putCondition.await();
            }
            System.out.println("Putting data :" + obj);
            queue.add(obj);
 
            //If say the queue is empty before we add the element to the queue, then chances are threads trying to take the element would be waiting,
            //so after adding the element we will inform take threads that there is element now for you to take.
            takeCondition.signal();
        } finally {
            lock.unlock();
        }
    }
 
    public static void main(String[] args) {
        GenericCustomBlockingQueueUsingLock<Integer> queue = new GenericCustomBlockingQueueUsingLock<>(10);
        new Thread(() -> {
            int i = 0;
            while (i < 20) {
                try {
                    System.out.println(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }).start();
 
        new Thread(() -> {
            int i = 0;
            while (i < 20) {
                try {
                    queue.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }).start();
    }
}
