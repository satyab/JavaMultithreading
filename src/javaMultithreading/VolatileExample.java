package javaMultithreading;

public class VolatileExample {

  volatile boolean isTerminated = false;

  public static void main(String[] args) throws InterruptedException {

    VolatileExample v = new VolatileExample();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        while (!v.isTerminated) {
          System.out.println("t1 is running");
          try {
            Thread.sleep(300);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });
    t1.start();
    Thread.sleep(3000);
    v.isTerminated = true;
    System.out.println("Flag is set");
  }
}
