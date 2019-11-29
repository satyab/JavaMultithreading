package basics;

import java.util.HashSet;
import java.util.Set;

public class TokenBucketFilterProblem {
  public static void main(String[] args) throws InterruptedException {
    TokenBucketFilter.runTestMaxTokenIs1();
    System.out.println(" ");
    TokenBucketFilter.runTestMaxTokenIsTen();
  }
}

class TokenBucketFilter {

  int MAX_TOKENS;
  long lastRequestTime = System.currentTimeMillis();
  int possibleTokens = 0;

  public TokenBucketFilter(int max) {
    MAX_TOKENS = max;
  }

  public static void runTestMaxTokenIs1() throws InterruptedException {

    Set<Thread> allThreads = new HashSet<>();
    final TokenBucketFilter tokenBucketFilter = new TokenBucketFilter(1);

    for (int i = 0; i < 10; i++) {

      Thread thread = new Thread(() -> {
        try {
          tokenBucketFilter.getToken();
        } catch (InterruptedException ie) {
          System.out.println("We have a problem");
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

  public static void runTestMaxTokenIsTen() throws InterruptedException {

    Set<Thread> allThreads = new HashSet<Thread>();
    final TokenBucketFilter tokenBucketFilter = new TokenBucketFilter(5);

    // Sleep for 10 seconds.
    Thread.sleep(10000);

    // Generate 12 threads requesting tokens almost all at once.
    for (int i = 0; i < 12; i++) {

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

  synchronized void getToken() throws InterruptedException {

    possibleTokens += (System.currentTimeMillis() - lastRequestTime) / 1000;

    if (possibleTokens > MAX_TOKENS) {
      possibleTokens = MAX_TOKENS;
    }

    if (possibleTokens == 0) {
      Thread.sleep(1000);
    } else {
      possibleTokens--;
    }

    lastRequestTime = System.currentTimeMillis();

    System.out.println(
        "Granting " + Thread.currentThread().getName() + " token at " + (System.currentTimeMillis()
            / 1000));
  }
}