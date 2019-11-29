package javaMultithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Worker {

  private Lock lock = new ReentrantLock();
  private Condition condition = lock.newCondition();

  private int capacity;
  private List<Integer> list;
  private Random random;

  public Worker(int capacity) {
    this.capacity = capacity;
    list = new ArrayList<Integer>(capacity);
    random = new Random();
  }

  public void producer() throws InterruptedException {
    lock.lock();
    try {
      while (list.size() == capacity) {
        System.out.println("List if Full...Waiting for Consumer to consume items");
        condition.await();
      }

      int k = random.nextInt(100);
      list.add(k);
      System.out.println("Added " + k + " to the list");
      System.out.println(list);
      condition.signal();
    } finally {
      lock.unlock();
    }
  }

  public void consumer() throws InterruptedException {
    lock.lock();
    try {
      while (list.size() == 0) {
        System.out.println("List if Empty...Waiting for Producer to produce items");
        condition.await();
      }

      int k = list.remove(0);
      System.out.println("Removed " + k + " from the list");
      System.out.println(list);
      condition.signal();
    } finally {
      lock.unlock();
    }
  }
}

public class ProducerConsumerLock {

  public static void main(String[] args) {

    Worker worker = new Worker(50);

    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            worker.producer();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            worker.consumer();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

      }
    });

    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
