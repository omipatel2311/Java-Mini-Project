
package service;

import java.util.concurrent.CountDownLatch;
import model.Account;
import model.SavingsAccount;
import exception.BankException;

public class TransferLockDemo {

    public static void main(String[] args) {

        System.out.println("--- Deadlock Demo ---");

        SavingsAccount deadA =
                new SavingsAccount("Dead A", 1000, 0);
        SavingsAccount deadB =
                new SavingsAccount("Dead B", 1000, 0);

        CountDownLatch ready = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            synchronized (deadA) {
                ready.countDown();
                waitForBoth(ready);

                synchronized (deadB) {
                    System.out.println("A to B");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (deadB) {
                ready.countDown();
                waitForBoth(ready);

                synchronized (deadA) {
                    System.out.println("B to A");
                }
            }
        });

        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        waitForBoth(ready);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Deadlock demonstrated");

        System.out.println("\n--- Fixed Lock Order ---");

        SavingsAccount a =
                new SavingsAccount("Safe A", 5000, 0);
        SavingsAccount b =
                new SavingsAccount("Safe B", 1000, 0);

        safeTransfer(a, b, 1000);

        System.out.println("A balance: " + a.getBalance());
        System.out.println("B balance: " + b.getBalance());
    }

    private static void waitForBoth(CountDownLatch ready) {
        try {
            ready.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void safeTransfer(
            Account from, Account to, long amount) {

        Account first;
        Account second;

        if (from.getAccountNumber().compareTo(
                to.getAccountNumber()) < 0) {
            first = from;
            second = to;
        } else {
            first = to;
            second = from;
        }

        synchronized (first) {
            synchronized (second) {
                try {
                    from.withdraw(amount);
                    to.deposit(amount);
                    System.out.println("Transfer successful");
                } catch (BankException e) {
                    System.out.println("Transfer failed: "
                            + e.getMessage());
                }
            }
        }
    }
}
