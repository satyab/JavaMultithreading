package diningPhilosophers.basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiningPhilosopher {

  public static void main(String[] args) {
    ExecutorService service = null;
    Philosopher[] philosophers = null;

    try {
      philosophers = new Philosopher[Constants.NUMBER_OF_PHILOSOPHERS];
      Chopstick[] chopsticks = new Chopstick[Constants.NUMBER_OF_CHOPSTICKS];

      for (int i = 0; i < Constants.NUMBER_OF_CHOPSTICKS; i++) {
        chopsticks[i] = new Chopstick(i);
      }

      service = Executors.newFixedThreadPool(Constants.NUMBER_OF_PHILOSOPHERS);

      for (int i = 0; i < Constants.NUMBER_OF_PHILOSOPHERS; i++) {
        philosophers[i] = new Philosopher(i, chopsticks[i],
            chopsticks[(i + 1) % Constants.NUMBER_OF_CHOPSTICKS]);
        service.execute(philosophers[i]);
      }

      Thread.sleep(Constants.SIMULATION_RUNNING_TIME);

      for (Philosopher p : philosophers) {
        p.setFull(true);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      service.shutdown();
      while (!service.isTerminated()) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        for (Philosopher p : philosophers) {
          System.out.println(p + " eats " + p.getEatingCounter());
        }
      }
    }
  }
}

