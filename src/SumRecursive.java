import java.util.concurrent.RecursiveAction;

public class SumRecursive {

  public static void main(String[] args) {
    SumRecursive rec = new SumRecursive();
    rec.parSum();

    long start = System.currentTimeMillis();
    SumForkJoin s = new SumForkJoin(0, Integer.MAX_VALUE);
    s.compute();
    long end = System.currentTimeMillis();
    System.out.println(
        "Num tasks " + "?" + " :  Result : " + s.value + " : Time taken : " + (end - start));

    /*
    long sum = 0;
    for (int i = 0; i <= Integer.MAX_VALUE; i++) {
      sum += i;
    }

    System.out.println(sum);*/
  }

  private long groupSize(long size, long total) {
    return (size + total - 1) / size;
  }

  private long startIndex(long index, long size, long total) {
    long sz = groupSize(size, total);
    return index * sz;
  }

  private long endIndex(long index, long size, long total) {
    long sz = groupSize(size, total);
    return Math.min((index + 1) * sz, total+1);
  }

  public void parSum() {

    long sum = 0;
    int numTask = 4;
    Sum[] task = new Sum[4];

    long start = System.currentTimeMillis();

    for (int i = 0; i < task.length; i++) {
      task[i] = new Sum(startIndex(i, numTask, Integer.MAX_VALUE),
          endIndex(i, numTask, Integer.MAX_VALUE));
    }

    for (int i = 1; i < task.length; i++) {
      task[i].fork();
    }

    task[0].compute();

    for (int i = 1; i < task.length; i++) {
      task[i].join();
    }

    for (int i = 0; i < task.length; i++) {
      sum += task[i].getValue();
    }

    long end = System.currentTimeMillis();
    System.out.println(
        "Num tasks " + numTask + " :  Result : " + sum + " : Time taken : " + (end - start));
  }

  public static class Sum extends RecursiveAction {

    long start;
    long end;
    long value;

    public Sum(long start, long end) {
      this.start = start;
      this.end = end;
    }

    @Override
    protected void compute() {
      for (long i = start; i < end; i++) {
        value += i;
      }
    }

    public long getValue() {
      return value;
    }
  }

  public static class SumForkJoin extends RecursiveAction {

    long start;
    long end;
    long value;

    public SumForkJoin(long start, long end) {
      this.start = start;
      this.end = end;
    }

    @Override
    protected void compute() {

      if (end - start <= 2) {
        for (long i = start; i <= end; i++) {
          value += i;
        }
      } else {
        long mid = (start + end) / 2;
        SumForkJoin s1 = new SumForkJoin(start, mid);
        SumForkJoin s2 = new SumForkJoin(mid + 1, end);

        s1.fork();
        s2.compute();
        s1.join();

        value = s1.value + s2.value;
      }
    }
  }
}

