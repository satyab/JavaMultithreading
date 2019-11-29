package concurrentCollections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class FirstWorker implements Runnable {

  private BlockingQueue<Integer> blockingQueue;

  public FirstWorker(BlockingQueue<Integer> blockingQueue) {
    this.blockingQueue = blockingQueue;
  }

  @Override
  public void run() {
    int counter = 0;

    while (true) {
      try {
        System.out.println("Putting items in queue : " + counter);
        blockingQueue.put(counter);
        counter++;
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

class SecondWorker implements Runnable {

  private BlockingQueue<Integer> blockingQueue;

  public SecondWorker(BlockingQueue<Integer> blockingQueue) {
    this.blockingQueue = blockingQueue;
  }

  @Override
  public void run() {
    while (true) {
      try {
        int k = blockingQueue.take();
        System.out.println("Removing items from queue : " + k);
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

public class BlockingQueueBasic {

  public static void main(String[] args) {
    BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    FirstWorker firstWorker = new FirstWorker(queue);
    SecondWorker secondWorker = new SecondWorker(queue);

    new Thread(firstWorker).start();
    new Thread(secondWorker).start();

  }
}
