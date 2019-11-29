package javaMultithreading;

/*
    Semaphore is used to keep track of resources.
    Semaphore wont help us in finding which resource is locked or free.
    Binary semaphore has value 0 or 1.

    Binary semaphore is same as Mutex.
    Mutex has concept of owner.
    Only the process that locked the mutex is supposed to unlock it.
    Mutex can self promote priority if high priority task is waiting.
    Mutex can provide deletion safety.
 */

/*
  Semaphore maintains set of permits
  acquire
  release
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

enum Downloader {
  INSTANCE;

  private Semaphore semaphore = new Semaphore(5, true);

  public void downloadData() {
    try {
      semaphore.acquire();
      download();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      semaphore.release();
    }
  }

  private void download() {

    System.out.println("Downloading data from web");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

public class SemaphoresBasic {

  public static void main(String[] args) {

    ExecutorService service = Executors.newCachedThreadPool();

    for (int i = 0; i < 100; i++) {
      service.submit(new Runnable() {
        @Override
        public void run() {
          Downloader.INSTANCE.downloadData();
        }
      });
    }

    service.shutdown();

  }
}
