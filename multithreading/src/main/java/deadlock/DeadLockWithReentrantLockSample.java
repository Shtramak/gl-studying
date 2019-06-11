package deadlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static deadlock.Transaction.transferWithReentrantLock;
import static deadlock.Transaction.transferWithSynchronized;

public class DeadLockWithReentrantLockSample {
    public static void main(String[] args) throws InterruptedException {
        Account account1 = new Account(10000);
        Account account2 = new Account(10000);
        ExecutorService service = Executors.newFixedThreadPool(2);
/*
        for (int i = 0; i < 10000; i++) {
            int transferAmount = ThreadLocalRandom.current().nextInt(100);
            service.submit(() -> transferWithSynchronized(account1, account2, transferAmount));
            service.submit(() -> transferWithSynchronized(account2, account1, transferAmount));
        }
*/
        for (int i = 0; i < 10000; i++) {
            int transferAmount1 = ThreadLocalRandom.current().nextInt(100);
            int transferAmount2 = ThreadLocalRandom.current().nextInt(100);
            service.submit(() -> transferWithReentrantLock(account1, account2, transferAmount1));
            service.submit(() -> transferWithReentrantLock(account2, account1, transferAmount2));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        System.out.println(account1.getAmount());
        System.out.println(account2.getAmount());
        System.out.println("Total amount: " + (account1.getAmount() + account2.getAmount()));
    }
}

class Account {
    private int amount;

    public Account(int amount) {
        this.amount = amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}

class Transaction {

    private static ReentrantLock reentrantLock1 = new ReentrantLock();
    private static ReentrantLock reentrantLock2 = new ReentrantLock();

    public static void transferWithSynchronized(Account acc1, Account acc2, int transferAmount) {
        synchronized (acc1) {
            synchronized (acc2) {
                int acc1Amount = acc1.getAmount();
                acc1.setAmount(acc1Amount - transferAmount);
                int acc2Amount = acc2.getAmount();
                acc2.setAmount(acc2Amount + transferAmount);
            }
        }
    }

    public static void transferWithReentrantLock(Account acc1, Account acc2, int transferAmount) {
        reentrantLock1.lock();
//        reentrantLock2.lock();
        try {
            int acc1Amount = acc1.getAmount();
            acc1.setAmount(acc1Amount - transferAmount);
            int acc2Amount = acc2.getAmount();
            acc2.setAmount(acc2Amount + transferAmount);
        } finally {
            reentrantLock1.unlock();
//            reentrantLock2.unlock();
        }
    }
}
