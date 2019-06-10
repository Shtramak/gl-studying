package reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockSample {
    private int counter;
    private ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockSample sample = new ReentrantLockSample();
        Thread incrementer = new Thread(sample::increment);
        Thread decrementer = new Thread(sample::decrement);
        incrementer.start();
        decrementer.start();
        incrementer.join();
        decrementer.join();
        System.out.println(sample.getCounter());
    }

    public void increment() {
        for (int i = 0; i < 10000; i++) {
            reentrantLock.lock();
            counter++;
            reentrantLock.unlock();
        }
    }

    public void decrement() {
        for (int i = 0; i < 10000; i++) {
            reentrantLock.lock();
            counter--;
            reentrantLock.unlock();
        }
    }

    public int getCounter() {
        return counter;
    }
}
