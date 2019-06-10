package threadpool;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolSample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        int numOfIterations = 10000;
        for (int i = 0; i < numOfIterations; i++) {
            service.submit(new Worker());
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("Map size: " + Worker.map.size());
        printMissedKeys(numOfIterations); //when synchronized block is commented
    }

    private static void printMissedKeys(int numOfIterations) {
        Set<Integer> keys = Worker.map.keySet();
        List<Integer> missedKeys = new ArrayList<>();
        for (Integer i = 0; i < numOfIterations; i++) {
            if(!keys.contains(i)){
                missedKeys.add(i);
            }
        }
        System.out.println("Missed Keys: " + missedKeys);
    }
}

class Worker implements Runnable {

    static Map<Integer, String> map = new HashMap<>();
    private String name;
    private static final Object lock = new Object();
    private static AtomicInteger valueCounter = new AtomicInteger();

    @Override
    public void run() {
        name = "Worker-" + Thread.currentThread().getId();
        int value = valueCounter.getAndIncrement();
        sleepForMillisecond();
//        synchronized (lock) {
            map.put(value, name);
//        }
        String message = "I'm " + name + ", I put " + value + " in map";
        System.err.println(message);
    }

    private void sleepForMillisecond() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
