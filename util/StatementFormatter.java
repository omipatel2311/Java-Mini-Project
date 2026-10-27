package util;

import model.Account;

public class StatementFormatter {

    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();

        sb.append("===== Account Statement =====\n");
        sb.append("Account Number: ")
          .append(account.getAccountNumber()).append("\n");
        sb.append("Owner Name: ")
          .append(account.getOwnerName()).append("\n");
        sb.append("Balance: ")
          .append(account.getBalance()).append("\n");

        return sb.toString();
    }
}