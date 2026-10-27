
package service;

import java.util.Scanner;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import util.Repository;
import model.Account;
import model.Customer;
import model.SavingsAccount;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.Command;
import util.AnnotationValidator;
import util.Validator;
import util.CommandParser;
import util.StatementFormatter;
import java.io.IOException;
import java.nio.file.Path;
import exception.BankException;
import util.StatePersister;
import util.TransactionLog;
import util.ReportGenerator;
import static util.Validator.isValidMobile;

record BankInfo(String name, String branch) {}

enum MenuOption {
    OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT
}

public class MiniBank {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        
Customer.Address address = new Customer.Address("Near Temple", "Changa", "388421");

Customer c1 = new Customer("Riya", "riya@gmail.com", "9876543210", address);

SavingsAccount a1 = new SavingsAccount("Riya", 5000, 1000);
CurrentAccount a2 = new CurrentAccount("Priya", 2000, 5000);
FixedDepositAccount a3 = new FixedDepositAccount("Amit", 10000);

Account[] accounts = {a1, a2, a3};

System.out.println("\n--- Practical 13 Testing ---");

Repository<Account> repository = new Repository<>();

repository.saveAll(a1, a2, a3);
Path savedAccountsFile = Path.of("data", "accounts.dat");

if (java.nio.file.Files.exists(savedAccountsFile)) {
    try {
        accounts = StatePersister.load(savedAccountsFile);
        System.out.println("Saved accounts loaded successfully.");
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Could not load accounts: " + e.getMessage());
    }
}
System.out.println("Search by ID:");
System.out.println(repository.findById(a2.getAccountNumber()));

System.out.println("\nAll Repository Accounts:");

for (Account account : repository.findAll()) {
    System.out.println(account);
}

Integer[] numbers = {10, 20, 30};

System.out.println("Average: "
        + Repository.average(numbers));

System.out.println("\n--- Practical 12 Testing ---");

AccountStore store = new AccountStore();

store.add(a1);
store.add(a2);
store.add(a3);

System.out.println("Search Account:");

Account found = store.get(a2.getAccountNumber());
System.out.println(found);
System.out.println("\nConcurrentHashMap Testing:");

TransactionProcessor mapProcessor = new TransactionProcessor();

mapProcessor.addAccount(a1);
mapProcessor.addAccount(a2);
mapProcessor.addAccount(a3);

System.out.println(
    "Found Account: " +
    mapProcessor.getAccount(a2.getAccountNumber())
);

mapProcessor.stop();

store.addTransaction("DEPOSIT AC0001 500");
store.addTransaction("WITHDRAW AC0002 200");

System.out.println("\nSorted by Balance:");

for (Account account : store.sortedByBalance()) {
    System.out.println(account);
}

System.out.println("\nTransaction History:");

for (String transaction : store.getHistory()) {
    System.out.println(transaction);
}


try {
    a1.deposit(2000);

    System.out.println("Withdraw 3000: " + a1.withdraw(3000));

    a1.withdraw(10000);

} catch (InsufficientFundsException e) {
    System.out.println("Withdrawal failed: " + e.getMessage());
    System.out.println("Shortfall: " + e.getShortfall());

} catch (InvalidAmountException e) {
    System.out.println("Invalid amount: " + e.getMessage());

} finally {
    System.out.println("Bank operation completed");
}

for (Account a : accounts) {
    System.out.println(a.getOwnerName() + " : " + a.getBalance());
    System.out.println("Interest Rate: " + a.interestRate());

    if (a instanceof SavingsAccount savings) {
        System.out.println("Savings Account: " + savings.getOwnerName());
    }
}

System.out.println("\n--- Practical 3 Testing ---");

// toString()
System.out.println(a1.toString());
System.out.println(a2.toString());
System.out.println(a3.toString());

// equals()
System.out.println("a1 equals a1: " + a1.equals(a1));
System.out.println("a1 equals a2: " + a1.equals(a2));

// instanceof
System.out.println("Is Account: " + (a1 instanceof Account));

// Address
System.out.println("Customer ID: " + c1.getCustomerId());
System.out.println("Customer Address: "
        + c1.getAddress().getLine() + ", "
        + c1.getAddress().getCity() + ", "
        + c1.getAddress().getPincode());

// clone()
Customer c2 = c1.clone();
System.out.println("Cloned Customer: " + c2.getName());
        
System.out.println("\n--- Practical 4 Testing ---");

// Mobile validation
System.out.println(isValidMobile("9876543210"));
System.out.println(Validator.isValidMobile("12345"));

// Email validation
System.out.println(Validator.isValidEmail("abc@gmail.com"));
System.out.println(Validator.isValidEmail("abc"));

// PAN validation
System.out.println(Validator.isValidPan("ABCDE1234F"));
System.out.println(Validator.isValidPan("1234"));

// IFSC validation
System.out.println(Validator.isValidIfsc("SBIN0001234"));
System.out.println(Validator.isValidIfsc("12345"));

// Command parsing
Command command = CommandParser.parse("DEPOSIT AC0001 500");
System.out.println("Type: " + command.type());
System.out.println("Account: " + command.accountNumber());
System.out.println("Amount: " + command.amount());

// Account statement
System.out.println(StatementFormatter.buildStatement(a1));

System.out.println("\n--- Practical 6 Testing ---");

// Anonymous Class
WithdrawRule rule1 = new WithdrawRule() {
    @Override
    public boolean allow(Account account, long amount) {
        return amount > 0 && account.getBalance() >= amount;
    }
};

System.out.println("Anonymous Class: " + rule1.allow(a1, 1000));

// Lambda Expression
WithdrawRule rule2 = (account, amount) ->
        amount > 0 && account.getBalance() >= amount;

System.out.println("Lambda: " + rule2.allow(a1, 1000));

// Yearly Interest
System.out.println("Yearly Interest: " + a1.yearlyInterest());

// Premium interface
System.out.println("Premium interface created successfully");
System.out.println("\n--- Practical 7 Testing ---");

Account testAccount =
        new SavingsAccount("Test", -100, 0);

String[] errors = AnnotationValidator.validate(testAccount);

for (String error : errors) {
    System.out.println(error);
}
System.out.println("\n--- Try With Resources ---");

try (BankSession session = new BankSession()) {
    System.out.println("Performing bank operation...");
}

System.out.println("\n--- Practical 9 Testing ---");

SavingsAccount threadAccount =
        new SavingsAccount("ThreadTest", 0, 0);

Thread[] threads = new Thread[10];

for (int i = 0; i < 10; i++) {
    AccountWorker worker =
            new AccountWorker(threadAccount, 1000, 1);

    threads[i] = new Thread(worker);
    threads[i].start();
}

try {
    for (int i = 0; i < 10; i++) {
        threads[i].join();
    }
} catch (InterruptedException e) {
    System.out.println("Thread interrupted");
}

System.out.println("Final Balance: " + threadAccount.getBalance());

System.out.println("\n--- Practical 10 Testing ---");

SavingsAccount batchAccount =
        new SavingsAccount("BatchTest", 0, 0);

TransactionProcessor processor = new TransactionProcessor();

for (int i = 0; i < 10; i++) {
    processor.submit(
        new AccountWorker(batchAccount, 100, 1)
    );
}

processor.stop();

System.out.println(
    "Final Batch Balance: " + batchAccount.getBalance()
);

System.out.println("\n--- Producer Consumer Testing ---");

TransactionBuffer buffer = new TransactionBuffer();

Thread producer = new Thread(() -> {
    try {
        for (long value : new long[]{100, 200, 300, 400, 500}) {
            buffer.add(value);
        }
        buffer.add(-1);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}, "Producer");

Thread consumer = new Thread(() -> {
    try {
        while (true) {
            long value = buffer.take();

            if (value == -1) {
                break;
            }

            System.out.println("Processed: " + value);
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}, "Consumer");

producer.start();
consumer.start();

try {
    producer.join();
    consumer.join();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
System.out.println("\n--- Practical 11 Testing ---");

Path dataFolder = Path.of("data");
Path accountsFile = dataFolder.resolve("accounts.dat");
Path logsFolder = Path.of("logs");
Path logFile = logsFolder.resolve("transactions.log");
Path reportFile = dataFolder.resolve("report.txt");

try {
    // Save accounts
    StatePersister.save(accounts, accountsFile);
    System.out.println("Accounts saved successfully.");

    // Load accounts
    Account[] loadedAccounts =
            StatePersister.load(accountsFile);

    System.out.println("\nLoaded Accounts:");

    for (Account account : loadedAccounts) {
        System.out.println(account);
    }

    // Write transaction log
    TransactionLog.append(
            logFile, "DEPOSIT AC0001 500");

    TransactionLog.append(
            logFile, "WITHDRAW AC0002 200");

    // Generate report
    String report =
            ReportGenerator.generateReport(
                    logsFolder, reportFile);

    System.out.println(report);
    System.out.println("Report saved: " + reportFile);

} catch (IOException | ClassNotFoundException e) {
    System.out.println("File error: " + e.getMessage());
}
BankService bankService = new BankService(accounts);
        BankInfo bank = new BankInfo("MiniBank", "Changa");

        System.out.println("===== " + bank.name() + " =====");
        System.out.println("Branch: " + bank.branch());

      
int choice;

do {
    System.out.println("\n===== MiniBank Menu =====");
    System.out.println("1. Open Account");
    System.out.println("2. Deposit");
    System.out.println("3. Withdraw");
    System.out.println("4. Transfer");
    System.out.println("5. View Statement");
    System.out.println("6. Run Report");
    System.out.println("7. List Accounts");
    System.out.println("8. Exit");

    System.out.print("Enter your choice: ");
    choice = sc.nextInt();
    sc.nextLine();

    try {
        switch (choice) {
            case 1 -> {
                System.out.print("Enter owner name: ");
                String name = sc.nextLine();

                System.out.print("Opening balance: ");
                long amount = Long.parseLong(sc.nextLine());

                bankService.openAccount(name, amount);
            }

            case 2 -> {
                System.out.print("Account number: ");
                String id = sc.nextLine();

                System.out.print("Deposit amount: ");
                long amount = Long.parseLong(sc.nextLine());

                bankService.deposit(id, amount);
            }

            case 3 -> {
                System.out.print("Account number: ");
                String id = sc.nextLine();

                System.out.print("Withdrawal amount: ");
                long amount = Long.parseLong(sc.nextLine());

                bankService.withdraw(id, amount);
            }

            case 4 -> {
                System.out.print("From account: ");
                String from = sc.nextLine();

                System.out.print("To account: ");
                String to = sc.nextLine();

                System.out.print("Transfer amount: ");
                long amount = Long.parseLong(sc.nextLine());

                bankService.transfer(from, to, amount);
            }

            case 5 -> {
                System.out.print("Account number: ");
                String id = sc.nextLine();

                System.out.println(bankService.statement(id));
            }

            case 6 -> {
                String report = ReportGenerator.generateReport(
                    Path.of("logs"),
                    Path.of("data", "report.txt")
                );

                System.out.println(report);
            }

            case 7 -> {
                for (Account account : bankService.listAccounts()) {
                    System.out.println(account);
                }
            }

            case 8 -> {
                StatePersister.save(
                    bankService.listAccounts().toArray(new Account[0]),
                    Path.of("data", "accounts.dat")
                );

                System.out.println("Accounts saved successfully.");
                System.out.println("Thank you for using MiniBank!");
            }

            default -> System.out.println("Invalid choice.");
        }

    } catch (BankException | IOException e) {
        System.out.println("Operation failed: " + e.getMessage());
    }

} while (choice != 8);

sc.close();

    }
}
