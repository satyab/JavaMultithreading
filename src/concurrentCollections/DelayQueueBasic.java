package concurrentCollections;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class DelayedWorker implements Delayed {

  private long duration;
  private String message;

  public DelayedWorker(long duration, String message) {
    this.duration = System.currentTimeMillis() + duration;
    this.message = message;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public long getDelay(TimeUnit unit) {
    return unit.convert(duration - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
  }

  @Override
  public int compareTo(Delayed o) {
    if (this.duration < ((DelayedWorker) o).getDuration()) {
      return -1;
    }

    if (this.duration > ((DelayedWorker) o).getDuration()) {
      return 1;
    }

    return 0;
  }
}

public class DelayQueueBasic {

  public static void main(String[] args) {
    BlockingQueue<DelayedWorker> queue = new DelayQueue<>();

    try {
      queue.put(new DelayedWorker(1000, "First Message"));
      queue.put(new DelayedWorker(5000, "second Message"));
      queue.put(new DelayedWorker(2000, "Third Message"));

    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    while (!queue.isEmpty()) {
      try {
        System.out.println(queue.take().getMessage());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
