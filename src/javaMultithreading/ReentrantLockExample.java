package javaMultithreading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//Always unlock lock in finally block.
// Can be unlocked in another method, not necessary to unlock in same block of code, unlike synchronized blocks

public class ReentrantLockExample {

  private static int counter = 0;
  private static Lock lock = new ReentrantLock();

  private static void increment() {
    lock.lock();

    try {
      for (int i = 0; i < 10000; i++) {
        counter++;
      }
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        increment();
      }
    });

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        increment();
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

    System.out.println(counter);
  }
}
