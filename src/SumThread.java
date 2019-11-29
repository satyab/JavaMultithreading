public class SumThread {

  public static void main(String[] args) throws InterruptedException {
    SumExample.runTest();
  }
}

class SumExample {
  static long MAX_NUM = Integer.MAX_VALUE;
  long start;
  long end;
  long counter;

  public SumExample(long start, long end) {
    this.start = start;
    this.end = end;
  }

  public static void twoThread() throws InterruptedException {

    long start = System.currentTimeMillis();
    SumExample s1 = new SumExample(0, MAX_NUM / 2);
    SumExample s2 = new SumExample(1 + (MAX_NUM / 2), MAX_NUM);

    Thread t1 = new Thread(() -> s1.add());
    Thread t2 = new Thread(() -> s2.add());

    t1.start();
    t1.join();

    t2.start();
    t2.join();

    long finalCount = s1.counter + s2.counter;

    long end = System.currentTimeMillis();
    System.out.println("Time by Two Thread: " + (end - start) + " Result: " + finalCount);

  }

  public static void oneThread() {

    long start = System.currentTimeMillis();
    SumExample s = new SumExample(0, MAX_NUM);
    s.add();
    long end = System.currentTimeMillis();
    System.out.println("Time by Single Thread: " + (end - start) + " Result: " + s.counter);
  }

  public static void runTest() throws InterruptedException {
    oneThread();
    twoThread();
  }

  public void add() {
    for (long i = start; i <= end; i++) {
      counter += i;
    }
  }
}



