package producerconsumer;

import java.util.Scanner;

public class WaitNotifySample {
    private static Double number;
    private static Object lock = new Object();

    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            try {
                produceNumber();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                consumeNumber();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();
    }

    static void produceNumber() throws InterruptedException {
        System.out.println("Producer starts...");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            double value = scanner.nextDouble();
            synchronized (lock) {
                number = value;
                System.out.println("Value is assigned...");
                lock.notifyAll();
                lock.wait();
            }
        }
    }

    static void consumeNumber() throws InterruptedException {
        System.out.println("Consumer starts...");
        synchronized (lock) {
            lock.wait();
            while (true) {
                System.out.println("Consumer reads value: " + number);
                lock.notifyAll();
                lock.wait();
            }
        }
    }
}
