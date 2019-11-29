package javaMultithreading;

public class SynchronizedBlock {
  private static int counter1 = 0;
  private static int counter2 = 0;

  private static Object lock1 = new Object();
  private static Object lock2 = new Object();

  public static void add() {
    synchronized (lock1) {
      ++counter1;
    }
  }

  public static synchronized void addAgain() {
    synchronized (lock2) {
      ++counter2;
    }
  }

  public static void compute() {
    for (int i = 0; i < 100; i++) {
      add();
      addAgain();
    }
  }

  public static void process() {
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        compute();
      }
    });

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        compute();
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

  public static void main(String[] args) {
    process();
    System.out.println(counter1 + " " + counter2);
  }

}
