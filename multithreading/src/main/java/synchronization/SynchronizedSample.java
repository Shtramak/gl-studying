package synchronization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SynchronizedSample {

    @Test
    void synchronizedIncrementProvidesCorrectIncrement() throws InterruptedException {
        TestClass testClass = new TestClass();
        startThreadWithJoinPerRunnable(testClass::synchronizedIncrement,
                testClass::synchronizedIncrement,
                testClass::synchronizedIncrement);
        Assertions.assertEquals(300000, testClass.getCounter());
    }

    @Test
    void nonSynchronizedIncrementProvidesUndefinedIncrement() throws InterruptedException {
        TestClass testClass = new TestClass();
        startThreadWithJoinPerRunnable(testClass::nonSynchronizedIncrement,
                testClass::nonSynchronizedIncrement,
                testClass::nonSynchronizedIncrement);
        Assertions.assertNotEquals(300000, testClass.getCounter());
    }

    @Test
    void blockSynchronizedIncrementProvidesCorrectIncrement() throws InterruptedException {
        TestClass testClass = new TestClass();
        startThreadWithJoinPerRunnable(testClass::blockSynchronizedIncrement,
                testClass::blockSynchronizedIncrement,
                testClass::blockSynchronizedIncrement);
        Assertions.assertEquals(300000, testClass.getCounter());
    }

    @Test
    void nonSynchronizedAddToList1ProvidesUndefinedNumberOfInsertions() throws InterruptedException {
        long start = System.currentTimeMillis();
        TestClass testClass = new TestClass();
        for (int i = 0; i < 1000; i++) {
            startThreadWithJoinPerRunnable(testClass::nonSynchronizedAddToList1,
                    testClass::nonSynchronizedAddToList1);
        }
        long finish = System.currentTimeMillis();
        System.out.println("Aprox. time of execution: " + (finish - start));
        System.out.println("Expected: " + 2000 + "; Actual: " + testClass.getList1().size());
        Assertions.assertNotEquals(2000, testClass.getList1().size());
    }

    @Test
    void synchronizedAddToListsProvidesCorrectInsertions() throws InterruptedException {
        long start = System.currentTimeMillis();
        TestClass testClass = new TestClass();
        for (int i = 0; i < 1000; i++) {
            startThreadWithJoinPerRunnable(testClass::synchronizedAddToList1,
                    testClass::synchronizedAddToList2);
        }
        long finish = System.currentTimeMillis();
        System.out.println("Aprox. time of execution: " + (finish - start));
        Assertions.assertEquals(1000, testClass.getList1().size());
        Assertions.assertEquals(1000, testClass.getList2().size());
    }

    @Test
    void synchronizedAddToList1ProvidesCorrectInsertions() throws InterruptedException {
        long start = System.currentTimeMillis();
        TestClass testClass = new TestClass();
        for (int i = 0; i < 1000; i++) {
            startThreadWithJoinPerRunnable(testClass::synchronizedAddToList1,
                    testClass::synchronizedAddToList1);
        }
        long finish = System.currentTimeMillis();
        System.out.println("Aprox. time of execution: " + (finish - start));
        Assertions.assertEquals(2000, testClass.getList1().size());
    }

    private void startThreadWithJoinPerRunnable(Runnable... runnables) {
        List<Thread> threads = Stream.of(runnables)
                .map(Thread::new)
                .collect(Collectors.toList());
        threads.forEach(Thread::start);
        threads.forEach(SynchronizedSample::joinThread);
    }

    private static void joinThread(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class TestClass {
    private int counter;
    private final Object lock1 = new Object();
    private List<Integer> list1 = new ArrayList<>();
    private List<Integer> list2 = new ArrayList<>();
    private final Object lock2 = new Object();

    public synchronized void synchronizedIncrement() {
        for (int i = 0; i < 100000; i++) {
            counter++;
        }
    }

    public void nonSynchronizedIncrement() {
        for (int i = 0; i < 100000; i++) {
            counter++;
        }
    }

    public void blockSynchronizedIncrement() {
        for (int i = 0; i < 100000; i++) {
            synchronized (lock1) {
                counter++;
            }
        }
    }

    public void nonSynchronizedAddToList1() {
        int element = ThreadLocalRandom.current().nextInt(100);
        list1.add(element);
    }

    public void synchronizedAddToList1() {
        synchronized (lock1) {
            int element = ThreadLocalRandom.current().nextInt(100);
            list1.add(element);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void synchronizedAddToList2() {
        synchronized (lock2) {
            int element = ThreadLocalRandom.current().nextInt(100);
            list2.add(element);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Integer> getList1() {
        return list1;
    }

    public List<Integer> getList2() {
        return list2;
    }

    public int getCounter() {
        return counter;
    }
}
