import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueProblemMutex {

  public static void main(String[] args) throws InterruptedException {
    BlockingQueueBusyWait<Integer> queue = new BlockingQueueBusyWait<Integer>(5);

    Thread t1 = new Thread(() -> {
      for (int i = 0; i < 50; i++) {
        try {
          queue.enqueue(i);
          System.out.println("Thread 1 Enqueue operation : " + i);
        } catch (InterruptedException e) {

        }
      }
    });

    Thread t2 = new Thread(() -> {
      for (int i = 0; i < 25; i++) {
        try {
          int k = queue.deque();
          System.out.println("Thread 2 Dequeue operation : " + k);
        } catch (InterruptedException e) {

        }
      }
    });

    Thread t3 = new Thread(() -> {
      for (int i = 0; i < 25; i++) {
        try {
          int k = queue.deque();
          System.out.println("Thread 3 Dequeue operation : " + k);
        } catch (InterruptedException e) {

        }
      }
    });

    t1.start();
    Thread.sleep(4000);
    t2.start();
    t3.start();

    t1.join();
    t2.join();
    t3.join();
  }
}

class BlockingQueueBusyWait<T> {

  T[] queue;
  int head = 0;
  int tail = 0;
  int size = 0;
  int capacity;
  Lock lock = new ReentrantLock();

  public BlockingQueueBusyWait(int capacity) {
    queue = (T[]) new Object[capacity];
    this.capacity = capacity;
  }

  public void enqueue(T item) throws InterruptedException {

    lock.lock();
    while (size == capacity) {
      lock.unlock();
      lock.lock();
    }

    if (tail == capacity) {
      tail = 0;
    }

    queue[tail] = item;
    size++;
    tail++;

    lock.unlock();

  }

  public T deque() throws InterruptedException {

    T item = null;
    lock.lock();
    while (size == 0) {
      lock.unlock();
      lock.lock();
    }

    if (head == capacity) {
      head = 0;
    }

    item = queue[head];
    queue[head] = null;
    head++;
    size--;

    lock.unlock();

    return item;
  }
}
