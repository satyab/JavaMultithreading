package concurrentCollections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class FirstWorkerMap implements Runnable {

  private ConcurrentMap<String, Integer> map;

  public FirstWorkerMap(ConcurrentMap<String, Integer> map) {
    this.map = map;
  }

  @Override
  public void run() {
    try {
      map.put("A", 1);
      map.put("B", 2);
      Thread.sleep(1000);
      map.put("C", 3);
      map.put("D", 4);
      Thread.sleep(1000);
      map.put("E", 5);

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

class SecondWorkerMap implements Runnable {

  private ConcurrentMap<String, Integer> map;

  public SecondWorkerMap(ConcurrentMap<String, Integer> map) {
    this.map = map;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(5000);
      System.out.println(map.get("E"));
      Thread.sleep(1000);
      System.out.println(map.get("B"));
      System.out.println(map.get("D"));
      System.out.println(map.get("A"));
      System.out.println(map.get("C"));

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

public class ConcurrentMapBasic {

  public static void main(String[] args) {
    ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();

    new Thread(new FirstWorkerMap(map)).start();
    new Thread(new SecondWorkerMap(map)).start();

  }
}
