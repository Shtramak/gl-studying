package countdownlatch;

import java.util.List;
import java.util.concurrent.*;

public class CountDownLatchSample implements Runnable {
    private static CountDownLatch countDownLatch = new CountDownLatch(3);
    private static List<Integer> integers = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executorService.submit(new CountDownLatchSample());
        }
        countDownLatch.await();
        System.out.println(integers);
        executorService.shutdown();
    }

    @Override
    public void run() {
        int value = ThreadLocalRandom.current().nextInt(100);
        integers.add(value);
        countDownLatch.countDown();
    }
}
