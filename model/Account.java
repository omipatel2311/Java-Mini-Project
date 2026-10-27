package model;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.util.Objects;
import model.annotation.Id;
import model.annotation.Positive;
import java.io.Serializable;

import java.util.Objects;
public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;

    @Id
private final String accountNumber;
    private String ownerName;
    @Positive
private long balance;
    private boolean active;

    private static int counter = 0;

    public Account(String ownerName, long balance) {
        counter++;
        this.accountNumber = String.format("AC%04d", counter);
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }
public synchronized void deposit(long amount) throws InvalidAmountException {
    if (amount <= 0) {
        throw new InvalidAmountException("Amount must be greater than zero");
    }

    balance = balance + amount;
}
public synchronized boolean withdraw(long amount)
        throws InsufficientFundsException, InvalidAmountException {

    if (amount <= 0) {
        throw new InvalidAmountException("Amount must be greater than zero");
    }

    if (!canWithdraw(amount)) {
        long shortfall = Math.max(0, amount - balance);
        throw new InsufficientFundsException(shortfall);
    }

    balance = balance - amount;
    return true;
}
public void transfer(Account to, long amount) throws BankException {
    try {
        withdraw(amount);
        to.deposit(amount);
    } catch (BankException e) {
        throw e;
    } finally {
        System.out.println("Transfer attempt completed");
    }
}
    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }
    @Override
public String toString() {
    return accountNumber + " | " + ownerName + " | " + balance;
}

@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }

    if (obj == null || !(obj instanceof Account)) {
        return false;
    }

    Account other = (Account) obj;
    return accountNumber.equals(other.accountNumber);
}

@Override
public int hashCode() {
    return Objects.hash(accountNumber);
}
@Override
public int compareTo(Account other) {
    return this.accountNumber.compareTo(other.accountNumber);
}
}
