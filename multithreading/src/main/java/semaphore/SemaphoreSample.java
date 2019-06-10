package semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreSample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(new Worker());
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
        System.out.println(Worker.getSum());
    }
}

class Worker implements Runnable {
    private static Semaphore semaphore = new Semaphore(1);
    private static int sum;

    @Override
    public void run() {
        try {
            semaphore.acquire();
            sum ++;
            Thread.sleep(200);
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int getSum() {
        return sum;
    }
}


