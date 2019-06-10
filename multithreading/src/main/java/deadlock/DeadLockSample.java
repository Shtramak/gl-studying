package deadlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DeadLockSample {
    public static void main(String[] args) throws InterruptedException {
        DeadLockCreator creator = new DeadLockCreator();
        ExecutorService service = Executors.newFixedThreadPool(2);
        System.out.println("Starting both methods of creator in many threads...");
        for (int i = 0; i < 100; i++) {
            service.submit(creator::firstMethod);
            service.submit(creator::secondMethod);
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("No way to get here...");
    }
}

class DeadLockCreator {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void firstMethod() {
        synchronized (lock1) {
            synchronized (lock2) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void secondMethod() {
        synchronized (lock2) {
            synchronized (lock1) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
