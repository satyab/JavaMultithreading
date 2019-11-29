package basics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapFunction {

  public static void main(String[] args) {

    List<Integer> list = new ArrayList<>();

    for (int i = 0; i < 45; i++) {
      list.add(i);
    }

    Function<Integer, Integer> func = MapFunction::add10;
    Function<Integer, Integer> funcSqr = MapFunction::sqr;

    List<Integer> list2 = mapF(list, funcSqr);

    List<Integer> list1 = parMap(list, funcSqr);

    list2.forEach(x -> System.out.print(x + ", "));
    System.out.println();
    list1.forEach(x -> System.out.print(x + ", "));

  }

  public static List<Integer> parMap(List<Integer> list, Function<Integer, Integer> func) {
    MapTask m = new MapTask(0, list.size() - 1, list, func);
    m.compute();
    return m.getList();
  }

  public static List<Integer> mapF(List<Integer> list, Function<Integer, Integer> func) {
    return list.stream().map(x -> func.apply(x)).collect(Collectors.toList());
  }

  public static Integer add10(Integer a) {
    return a + 10;
  }

  public static Integer sqr(Integer a) {
    return a * a;
  }

  public static class MapTask extends RecursiveAction {

    int start;
    int end;
    List<Integer> inList;
    List<Integer> outList;
    Function<Integer, Integer> f1;

    public MapTask(int start, int end, List<Integer> inList, Function<Integer, Integer> f) {
      this.start = start;
      this.end = end;
      this.inList = inList;
      System.out.println(inList + " " + this.inList + " " + (inList == this.inList));
      this.f1 = f;

      outList = new ArrayList<Integer>(inList.size());
    }

    @Override
    public void compute() {

      if (end - start <= 10) {
        for (int i = start; i <= end; i++) {
          outList.add(f1.apply(inList.get(i)));
        }
      } else {

        int mid = (start + end) / 2;
        MapTask m1 = new MapTask(start, mid, inList, f1);
        MapTask m2 = new MapTask(mid + 1, end, inList, f1);

        m1.fork();
        m2.compute();
        m1.join();

        outList.addAll(m1.getList());
        outList.addAll(m2.getList());

      }
    }

    public List<Integer> getList() {
      return outList;
    }
  }
}

