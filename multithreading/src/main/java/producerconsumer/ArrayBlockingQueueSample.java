package producerconsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class ArrayBlockingQueueSample {
    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) {
        Thread producer = new Thread(ArrayBlockingQueueSample::produce);
        Thread consumer = new Thread(ArrayBlockingQueueSample::consume);
        producer.start();
        consumer.start();
    }

    public static void produce() {
        while (true) {
            int value = ThreadLocalRandom.current().nextInt(100);
            int sleep = ThreadLocalRandom.current().nextInt(1000);
            sleep(sleep);
            try {
                queue.put(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void consume() {
        while (true) {
            Integer value = null;
            try {
                value = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int sleep = ThreadLocalRandom.current().nextInt(1000);
            sleep(sleep);
            System.out.println("Value from queue: " + value);
            System.out.println("Size of queue: " + queue.size());
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
