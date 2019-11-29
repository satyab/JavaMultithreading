package javaMultithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ProducerConsumerB {

  private List<Integer> list;
  private int capacity;
  private Random r = new Random();
  private Object lock = new Object();

  public ProducerConsumerB(int capacity) {
    list = new ArrayList<>(capacity);
    this.capacity = capacity;

  }

  public void produce() throws InterruptedException {
    synchronized (lock) {
      while (list.size() == capacity) {
        System.out.println("Waiting for Consumer to consume some items");
        lock.wait();
      }

      int k = r.nextInt(100);
      list.add(k);
      System.out.println("Added " + k + " to the list");
      System.out.println(list);
      lock.notify();
    }
  }

  public void consume() throws InterruptedException {
    synchronized (lock) {
      while (list.size() == 0) {
        System.out.println("Waiting for Producer to produce some items");
        lock.wait();
      }

      int k = list.remove(0);
      System.out.println("Removed " + k + " from the list");
      System.out.println(list);
      lock.notify();
    }
  }

}

public class ProducerConsumerBasic {

  public static void main(String[] args) {
    ProducerConsumerB p = new ProducerConsumerB(10);
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          while (true) {
            p.produce();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          while (true) {
            p.consume();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    t1.start();
    t2.start();
  }
}
