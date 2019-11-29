package javaMultithreading;

class RunnerThread1 extends Thread {

  @Override
  public void run() {
    for (int i = 0; i < 10; i++) {
      System.out.println("Runner 1: " + i);
    }
  }
}

class RunnerThread2 extends Thread {
  @Override
  public void run() {
    for (int i = 0; i < 500; i++) {
      System.out.println("Runner 2: " + i);
    }
  }
}

public class JavaThreads {

  public static void main(String[] args) {
    Thread t1 = new RunnerThread1();
    Thread t2 = new RunnerThread2();

    t1.start();
    t2.start();

    try {
      t1.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Tasks Finished");
  }
}
