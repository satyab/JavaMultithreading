package concurrentCollections;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WorkerCyclicBarrier implements Runnable {

  private int id;
  private CyclicBarrier cyclicBarrier;
  private Random random;

  public WorkerCyclicBarrier(int id, CyclicBarrier cyclicBarrier) {
    this.id = id;
    this.cyclicBarrier = cyclicBarrier;
    random = new Random();
  }

  @Override
  public void run() {
    doWork();
  }

  private void doWork() {
    System.out.println("Thread " + id + " started working");
    try {
      Thread.sleep(random.nextInt(3000));
      System.out.println("Thread " + id + " finished working");
      cyclicBarrier.await();
      System.out.println("Moving on to next task " + id);
    } catch (InterruptedException | BrokenBarrierException e) {
      e.printStackTrace();
    }
  }
}

public class CyclicBarrierBasic {

  public static void main(String[] args) {
    ExecutorService service = Executors.newFixedThreadPool(5);

    CyclicBarrier barrier = new CyclicBarrier(5, new Runnable() {
      @Override
      public void run() {
        System.out.println("All Tasks completed ");

      }
    });

    for (int i = 0; i < 5; i++) {
      service.execute(new WorkerCyclicBarrier(i + 1, barrier));
    }

    service.shutdown();
  }
}
