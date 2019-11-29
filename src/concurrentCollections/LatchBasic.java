package concurrentCollections;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WorkerLatch implements Runnable {
  private int id;
  private CountDownLatch countDownLatch;

  public WorkerLatch(int id, CountDownLatch countDownLatch) {
    this.id = id;
    this.countDownLatch = countDownLatch;
  }

  @Override
  public void run() {
    doWork();
    countDownLatch.countDown();
  }

  private void doWork() {
    System.out.println("I am working: " + this.id);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

public class LatchBasic {

  public static void main(String[] args) {

    ExecutorService service = Executors.newSingleThreadExecutor();
    CountDownLatch countDownLatch = new CountDownLatch(5);

    for (int i = 0; i < 5; i++) {
      service.submit(new WorkerLatch(i + 1, countDownLatch));
    }

    try {
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("All previous threads are closed");
    service.shutdown();
  }
}
