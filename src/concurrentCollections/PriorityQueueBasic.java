package concurrentCollections;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

class InsertWorker implements Runnable {

  private BlockingQueue<String> blockingQueue;

  public InsertWorker(BlockingQueue<String> blockingQueue) {
    this.blockingQueue = blockingQueue;
  }

  @Override
  public void run() {

    try {
      blockingQueue.put("B");
      blockingQueue.put("X");
      blockingQueue.put("S");
      Thread.sleep(1000);
      blockingQueue.put("A");
      Thread.sleep(1000);

      blockingQueue.put("D");

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

class RemoveWorker implements Runnable {

  private BlockingQueue<String> blockingQueue;

  public RemoveWorker(BlockingQueue<String> blockingQueue) {
    this.blockingQueue = blockingQueue;
  }

  @Override
  public void run() {

    try {
      Thread.sleep(5000);
      System.out.println(blockingQueue.take());
      Thread.sleep(1000);
      System.out.println(blockingQueue.take());
      System.out.println(blockingQueue.take());
      System.out.println(blockingQueue.take());
      System.out.println(blockingQueue.take());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

public class PriorityQueueBasic {

  public static void main(String[] args) {
    BlockingQueue<String> queue = new PriorityBlockingQueue<>();

    new Thread(new InsertWorker(queue)).start();
    new Thread(new RemoveWorker(queue)).start();

  }
}
