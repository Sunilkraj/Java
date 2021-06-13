package learn.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 
public class CustomBlockingQueueUsingLock {
    private Lock lock = new ReentrantLock();
    private Condition putCondition = lock.newCondition();
    private Condition takeCondition = lock.newCondition();
 
    private Object[] queue;
    private int queueSize;
 
    private int putIndex;
    private int takeIndex;
    private int count;
 
    public CustomBlockingQueueUsingLock(int queueSize) {
        this.queueSize = queueSize;
        queue = new Object[queueSize];
    }
 
    public void put(Object data) {
        lock.lock();
        try{
            while (count >= queueSize) {
                try {
                    putCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Queuing value :" + data);
            queue[putIndex] = data;
            count++;
 
            if (++putIndex >= queueSize) {
                putIndex = 0;
            }
            takeCondition.signalAll();
        }  finally {
            lock.unlock();
        }
    }
 
    public Object take() {
        lock.lock();
        try {
            while (count == 0) {
                try {
                    takeCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Object data = queue[takeIndex];
            count--;
 
            if (++takeIndex >= queueSize) {
                takeIndex = 0;
            }
            putCondition.signalAll();
            return data;
        } finally {
            lock.unlock();
        }
    }
 
    public static void main(String[] args) {
        CustomBlockingQueueUsingLock customBlockingQueue = new CustomBlockingQueueUsingLock(5);
 
        new Thread(() -> {
            int i = 0;
            while (i < 10) {
                System.out.println("data :" + customBlockingQueue.take());
                i++;
            }
        }, "Consumer Thread").start();
 
        new Thread(() -> {
            int i = 0;
            while (i < 10) {
                customBlockingQueue.put(i);
                i++;
            }
        }, "Producer Thread").start();
    }
}