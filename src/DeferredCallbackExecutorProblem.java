import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DeferredCallbackExecutorProblem {

  public static void main(String[] args) throws Exception {
    DeferredCallbackExecutor.runTestTenCallbacks();
  }
}

class DeferredCallbackExecutor {
  static Random random = new Random(System.currentTimeMillis());

  PriorityQueue<CallBack> queue = new PriorityQueue<CallBack>(new Comparator<CallBack>() {
    @Override
    public int compare(CallBack o1, CallBack o2) {
      return (int) (o1.executeAt - o2.executeAt);
    }
  });

  ReentrantLock lock = new ReentrantLock();
  Condition newCallbackArrived = lock.newCondition();

  public static void runTestTenCallbacks() throws InterruptedException {
    Set<Thread> allThreads = new HashSet<>();
    final DeferredCallbackExecutor deferredCallbackExecutor = new DeferredCallbackExecutor();

    Thread service = new Thread(new Runnable() {
      public void run() {
        try {
          deferredCallbackExecutor.start();
        } catch (InterruptedException ie) {

        }
      }
    });

    service.start();

    for (int i = 0; i < 10; i++) {
      Thread thread = new Thread(new Runnable() {
        public void run() {
          CallBack cb = new CallBack(1, "Hello this is " + Thread.currentThread().getName());
          deferredCallbackExecutor.registerCallBack(cb);
        }
      });
      thread.setName("Thread_" + (i + 1));
      thread.start();
      allThreads.add(thread);
      Thread.sleep(1000);
    }

    for (Thread t : allThreads) {
      t.join();
    }
  }

  private long findSleepDuration() {
    return queue.peek().executeAt - System.currentTimeMillis();
  }

  public void start() throws InterruptedException {

    long sleepFor = 0;

    while (true) {
      lock.lock();

      while (queue.size() == 0) {
        newCallbackArrived.await();
      }

      while (queue.size() != 0) {
        sleepFor = findSleepDuration();
        if (sleepFor <= 0) {
          break;
        }
        newCallbackArrived.await(sleepFor, TimeUnit.MILLISECONDS);
      }

      CallBack cb = queue.poll();
      System.out.println(
          "Executed at " + System.currentTimeMillis() / 1000 + " required at " + cb.executeAt / 1000
              + ": message:" + cb.message);
      lock.unlock();
    }
  }

  public void registerCallBack(CallBack callBack) {
    lock.lock();
    queue.add(callBack);
    newCallbackArrived.signal();
    lock.unlock();
  }

  static class CallBack {
    long executeAt;
    String message;

    public CallBack(long executeAfter, String message) {
      this.executeAt = System.currentTimeMillis() + executeAfter * 1000;
      this.message = message;
    }
  }
}