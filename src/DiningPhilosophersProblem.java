import java.util.Random;
import java.util.concurrent.Semaphore;

public class DiningPhilosophersProblem {
}

class DiningPhilosophers {
  private static Random random = new Random(System.currentTimeMillis());
  // Five semaphore represent the five forks.
  private Semaphore[] forks = new Semaphore[5];
  private Semaphore maxDiners = new Semaphore(4);

  // Initializing the semaphores with a permit of 1
  public DiningPhilosophers() {
    forks[0] = new Semaphore(1);
    forks[1] = new Semaphore(1);
    forks[2] = new Semaphore(1);
    forks[3] = new Semaphore(1);
    forks[4] = new Semaphore(1);
  }

  //Represents how a philosopher lives his life
  public void lifecycleOfPhilosopher(int id) throws InterruptedException {

    while (true) {
      contemplate();
      eat(id);
    }
  }

  // We can sleep the thread when the philosopher is thinking
  void contemplate() throws InterruptedException {
    Thread.sleep(random.nextInt(500));
  }

  // This method will have the meat of the solution, where the
  // philosopher is trying to eat.
  void eat(int id) throws InterruptedException {

    //maxDiners.acquire();

    if (id == 3) {
      acquireForkLeftHanded(3);
    } else {
      acquireForkForRightHanded(id);
    }

    //forks[id].acquire();
    //forks[(id + 1) % 5].acquire();

    System.out.println("Philosopher " + id + " is eating");

    forks[id].release();
    forks[(id + 1) % 5].release();
    //maxDiners.release();
  }

  void acquireForkForRightHanded(int id) throws InterruptedException {
    forks[id].acquire();
    forks[(id + 1) % 5].acquire();
  }

  // Left-handed philosopher picks the left fork first and then
  // the right one.
  void acquireForkLeftHanded(int id) throws InterruptedException {
    forks[(id + 1) % 5].acquire();
    forks[id].acquire();
  }
}
