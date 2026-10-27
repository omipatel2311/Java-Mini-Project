
package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {

    public static String generateReport(
            Path logsFolder, Path reportFile) throws IOException {

        long totalDeposits = 0;
        long totalWithdrawals = 0;

        StringBuilder details = new StringBuilder();

        Files.createDirectories(logsFolder);

        try (DirectoryStream<Path> files =
                Files.newDirectoryStream(logsFolder, "*.log")) {

            for (Path file : files) {

                BasicFileAttributes attr =
                        Files.readAttributes(
                                file, BasicFileAttributes.class);

                details.append(file.getFileName())
                       .append(" | Size: ")
                       .append(attr.size())
                       .append(" bytes | Modified: ")
                       .append(attr.lastModifiedTime())
                       .append("\n");

                try (BufferedReader reader =
                        Files.newBufferedReader(file)) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        String[] parts = line.trim().split("\\s+");

                        if (parts.length >= 3) {
                            try {
                                long amount =
                                        Long.parseLong(parts[2]);

                                if (parts[0].equalsIgnoreCase("DEPOSIT")) {
                                    totalDeposits += amount;
                                } else if (parts[0].equalsIgnoreCase("WITHDRAW")) {
                                    totalWithdrawals += amount;
                                }

                            } catch (NumberFormatException e) {
                                // Ignore invalid amount lines
                            }
                        }
                    }
                }
            }
        }

        String report =
                "===== End of Day Report =====\n"
                + "Total Deposits: " + totalDeposits + "\n"
                + "Total Withdrawals: " + totalWithdrawals + "\n"
                + "Net Change: "
                + (totalDeposits - totalWithdrawals) + "\n"
                + "\nLog File Details:\n" + details;

        if (reportFile.getParent() != null) {
            Files.createDirectories(reportFile.getParent());
        }

        Files.writeString(
                reportFile,
                report,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );

        return report;
    }
}
