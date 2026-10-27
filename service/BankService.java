
package service;

import java.util.ArrayList;

import model.Account;
import model.SavingsAccount;

import util.Repository;
import util.StatementFormatter;
import util.Validator;

import exception.BankException;
import exception.AccountNotFoundException;
import exception.InvalidAmountException;

public class BankService {

    private Repository<Account> repository =
            new Repository<>();
            public BankService(Account[] initialAccounts) {
    for (Account account : initialAccounts) {
        repository.save(account);
    }
}

    public Account openAccount(String name, long balance)
            throws InvalidAmountException {

        if (balance < 0) {
            throw new InvalidAmountException(
                    "Opening balance cannot be negative");
        }

        Account account =
                new SavingsAccount(name, balance, 0);

        repository.save(account);

        System.out.println("Account opened successfully.");
        System.out.println(account);

        return account;
    }

    public void deposit(String accountNumber, long amount)
            throws BankException {

        Account account = repository.findById(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found: " + accountNumber);
        }

        account.deposit(amount);
        System.out.println("Deposit successful.");
    }

    public void withdraw(String accountNumber, long amount)
            throws BankException {

        Account account = repository.findById(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found: " + accountNumber);
        }

        account.withdraw(amount);
        System.out.println("Withdrawal successful.");
    }

    public void transfer(String fromId, String toId, long amount)
            throws BankException {

        Account from = repository.findById(fromId);
        Account to = repository.findById(toId);

        if (from == null || to == null) {
            throw new AccountNotFoundException(
                    "Source or destination account not found");
        }

        from.withdraw(amount);
        to.deposit(amount);

        System.out.println("Transfer successful.");
    }

    public String statement(String accountNumber)
            throws BankException {

        Account account = repository.findById(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found: " + accountNumber);
        }

        return StatementFormatter.buildStatement(account);
    }

    public ArrayList<Account> listAccounts() {
        return repository.findAll();
    }
}
