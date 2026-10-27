
package service;

import model.Account;
import model.SavingsAccount;
import exception.BankException;
import exception.InsufficientFundsException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MiniBankTest {

    @Test
    void depositIncreasesBalance() throws BankException {
        SavingsAccount account =
                new SavingsAccount("Test", 1000, 0);

        BankService service =
                new BankService(new Account[]{account});

        service.deposit(account.getAccountNumber(), 500);

        assertEquals(1500, account.getBalance());
    }

    @Test
    void withdrawalOverBalanceThrowsException() {
        SavingsAccount account =
                new SavingsAccount("Test", 1000, 0);

        BankService service =
                new BankService(new Account[]{account});

        assertThrows(InsufficientFundsException.class, () ->
                service.withdraw(account.getAccountNumber(), 2000));
    }

    @Test
    void concurrentDepositsKeepCorrectBalance()
            throws InterruptedException {

        SavingsAccount account =
                new SavingsAccount("Test", 0, 0);

        BankService service =
                new BankService(new Account[]{account});

        ExecutorService pool =
                Executors.newFixedThreadPool(4);

        for (int i = 0; i < 1000; i++) {
            pool.submit(() -> {
                try {
                    service.deposit(
                            account.getAccountNumber(), 1);
                } catch (BankException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        pool.shutdown();

        assertTrue(pool.awaitTermination(
                10, TimeUnit.SECONDS));

        assertEquals(1000, account.getBalance());
    }
}
