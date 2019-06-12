package localthread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static java.lang.Thread.currentThread;

public class LocalThreadSample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableClass callable1 = new CallableClass();
        CallableClass callable2 = new CallableClass();
        CallableClass callable3 = new CallableClass();

        FutureTask<CallableClass> task1 = new FutureTask<>(callable1);
        FutureTask<CallableClass> task2 = new FutureTask<>(callable2);
        FutureTask<CallableClass> task3 = new FutureTask<>(callable3);

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread thread3 = new Thread(task3);

        thread1.start();
        thread2.start();
        thread3.start();

        CallableClass res1 = task1.get();
        CallableClass res2 = task2.get();
        CallableClass res3 = task3.get();

        System.out.println("Name: " + res1.getName() + ", id: " + res1.getId() + ", localThreadID: " + res1.getThreadId());
        System.out.println("Name: " + res2.getName() + ", id: " + res2.getId() + ", localThreadID: " + res2.getThreadId());
        System.out.println("Name: " + res3.getName() + ", id: " + res3.getId() + ", localThreadID: " + res3.getThreadId());
    }

    static class CallableClass implements Callable<CallableClass> {
        private static ThreadLocal<Long> threadID = ThreadLocal.withInitial(() -> 0L);
        private long id;
        private String name;
        private long localThreadID;

        public CallableClass() {

        }

        @Override
        public CallableClass call() throws Exception {
            id = currentThread().getId();
            name = "Thread-" + id;
            currentThread().setName(name);
            System.out.println("Generate thread: " + currentThread().getName());
            threadID.set(id);
            localThreadID = threadID.get();
            return this;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public long getThreadId() {
            return localThreadID;
        }
    }
}
