package basics;

public class BlockingQueueProblemMonitor {

  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<Integer> queue = new BlockingQueue<Integer>(5);

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

class BlockingQueue<T> {

  T[] queue;
  int head = 0;
  int tail = 0;
  int size = 0;
  int capacity;
  Object lock = new Object();

  public BlockingQueue(int capacity) {
    queue = (T[]) new Object[capacity];
    this.capacity = capacity;
  }

  public void enqueue(T item) throws InterruptedException {

    synchronized (lock) {
      while (size == capacity) {
        lock.wait();
      }

      if (tail == capacity) {
        tail = 0;
      }

      queue[tail] = item;
      size++;
      tail++;

      lock.notifyAll();
    }
  }

  public T deque() throws InterruptedException {

    T item = null;
    synchronized (lock) {
      while (size == 0) {
        lock.wait();
      }

      if (head == capacity) {
        head = 0;
      }

      item = queue[head];
      queue[head] = null;
      head++;
      size--;

      lock.notifyAll();
    }
    return item;
  }
}
