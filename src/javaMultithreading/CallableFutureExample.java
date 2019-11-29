package javaMultithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class CallableProcessor implements Callable<String> {

  private int id;

  public CallableProcessor(int id) {
    this.id = id;
  }

  @Override
  public String call() throws Exception {
    Thread.sleep(100);
    return "Id : " + id;
  }
}

public class CallableFutureExample {

  public static void main(String[] args) {

    ExecutorService service = Executors.newFixedThreadPool(2);
    List<Future<String>> futures = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      futures.add(service.submit(new CallableProcessor(i + 1)));
    }

    Boolean allDone = false;

    while (!allDone) {
      boolean inside = true;
      for (Future<String> future : futures) {
        inside = inside && future.isDone();
      }
      System.out.println("All Completed : " + inside);
      allDone = inside;
    }

    for (Future<String> future : futures) {
      try {
        System.out.println(future.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }

    service.shutdown();
  }
}
