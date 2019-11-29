package javaMultithreading;

class Processor {

  public void produce() throws InterruptedException {
    synchronized (this) {
      System.out.println("We are in producer method");
      wait(1000);
      System.out.println("Again Producer");
    }
  }

  public void consume() throws InterruptedException {
    Thread.sleep(1000);
    synchronized (this) {
      System.out.println("We are in consumer method");
      notify();
      Thread.sleep(3000);
      System.out.println("Waited long enough");
    }
  }

}

public class WaitAndNotifyExample {

  public static void main(String[] args) {
    Processor p = new Processor();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          p.produce();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          p.consume();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    t1.start();
    t2.start();
  }
}
