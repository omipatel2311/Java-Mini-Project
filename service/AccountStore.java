
package service;

import model.Account;
import java.util.HashMap;
import java.util.ArrayList;

public class AccountStore {

    private HashMap<String, Account> accounts = new HashMap<>();

    private ArrayList<String> history = new ArrayList<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public void addTransaction(String transaction) {
        history.add(transaction);
    }

    public ArrayList<String> getHistory() {
        return new ArrayList<>(history);
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = getAll();

        list.sort((a, b) ->
            Long.compare(b.getBalance(), a.getBalance())
        );

        return list;
    }
}
