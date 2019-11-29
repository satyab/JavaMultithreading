public class BarrierProblem {

  public static void main(String[] args) throws Exception {
    Barrier.runTest();
  }
}

class Barrier {
  int released = 0;
  int count = 0;
  int totalThreads;

  public Barrier(int totalThreads) {
    this.totalThreads = totalThreads;
  }

  public static void runTest() throws InterruptedException {
    final Barrier barrier = new Barrier(3);

    Thread p1 = new Thread(new Runnable() {
      public void run() {
        try {
          System.out.println("Thread 1");
          barrier.await();
          System.out.println("Thread 1");
          barrier.await();
          System.out.println("Thread 1");
          barrier.await();
        } catch (InterruptedException ie) {
        }
      }
    });

    Thread p2 = new Thread(new Runnable() {
      public void run() {
        try {
          Thread.sleep(500);
          System.out.println("Thread 2");
          barrier.await();
          Thread.sleep(500);
          System.out.println("Thread 2");
          barrier.await();
          Thread.sleep(500);
          System.out.println("Thread 2");
          barrier.await();
        } catch (InterruptedException ie) {
        }
      }
    });

    Thread p3 = new Thread(new Runnable() {
      public void run() {
        try {
          Thread.sleep(1500);
          System.out.println("Thread 3");
          barrier.await();
          Thread.sleep(1500);
          System.out.println("Thread 3");
          barrier.await();
          Thread.sleep(1500);
          System.out.println("Thread 3");
          barrier.await();
        } catch (InterruptedException ie) {
        }
      }
    });

    p1.start();
    p2.start();
    p3.start();

    p1.join();
    p2.join();
    p3.join();
  }

  public synchronized void await() throws InterruptedException {

    while (count == totalThreads) {
      wait();
    }
    count++;

    if (count == totalThreads) {
      notifyAll();
      released = totalThreads;
    } else {
      while (count < totalThreads) {
        wait();
      }
    }
    released--;
    if (released == 0) {
      count = 0;
      notifyAll();
    }
  }
}