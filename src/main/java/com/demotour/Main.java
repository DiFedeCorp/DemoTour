package com.demotour;

/**
 * Application de démonstration minimaliste pour SonarQube.
 * Ce code contient volontairement des défauts subtils.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar corebanking-demo.jar <account-id>");
            System.exit(1);
        }

        String accountId = args[0];
        // type 1=standard 2=premium 3=vip (le junior a mis ça en dur)
        int reportType = 1;

        try {
            AccountService service = new AccountService();

            double balance = service.getBalance(accountId);
            System.out.println("Balance for account " + accountId + " is " + balance);

            // le junior a ajouté l'appel au rapport
            ReportService reportService = new ReportService();
            String report = reportService.generateReport(accountId, balance, reportType);
            System.out.println(report);

            service.applyDailyInterest(accountId);
            service.transfer(accountId, "DEST-001", 42.50);

        } catch (Exception ex) {
            System.err.println("Unexpected error: " + ex.getMessage());
            System.exit(2);
        }
    }
}
