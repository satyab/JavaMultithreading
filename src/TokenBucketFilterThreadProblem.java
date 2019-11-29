import java.util.HashSet;
import java.util.Set;

public class TokenBucketFilterThreadProblem {
  public static void main(String args[]) throws InterruptedException {
    Set<Thread> allThreads = new HashSet<>();
    final TokenBucketFilterThread tokenBucketFilter = new TokenBucketFilterThread(1);

    for (int i = 0; i < 10; i++) {

      Thread thread = new Thread(new Runnable() {

        public void run() {
          try {
            tokenBucketFilter.getToken();
          } catch (InterruptedException ie) {
            System.out.println("We have a problem");
          }
        }
      });
      thread.setName("Thread_" + (i + 1));
      allThreads.add(thread);
    }

    for (Thread t : allThreads) {
      t.start();
    }

    for (Thread t : allThreads) {
      t.join();
    }

  }

}

class TokenBucketFilterThread {
  int possibleToken = 0;
  int MAX_TOKEN;
  int ONE_SECOND = 1000;

  public TokenBucketFilterThread(int max) {
    this.MAX_TOKEN = max;
    Thread dt = new Thread(() -> daemon());
    dt.setDaemon(true);
    dt.start();
  }

  public void daemon() {
    synchronized (this) {
      if (possibleToken < MAX_TOKEN) {
        possibleToken++;
      }
      this.notify();
    }

    try {
      Thread.sleep(ONE_SECOND);
    } catch (InterruptedException e) {

    }
  }

  public void getToken() throws InterruptedException {
    synchronized (this) {
      while (possibleToken == 0) {
        this.wait();
      }
      possibleToken--;
    }
    System.out.println(
        "Granting " + Thread.currentThread().getName() + " token at "
            + System.currentTimeMillis() / 1000);
  }
}
