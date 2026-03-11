package com.demotour;


public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar corebanking-demo.jar <account-id>\n" +
                    "   or: java -jar corebanking-demo.jar schedule <from> <to> <amount> <delay-days> <frequency> <token>\n" +
                    "   or: java -jar corebanking-demo.jar run-due\n" +
                    "   or: java -jar corebanking-demo.jar list-scheduled\n" +
                    "   or: java -jar corebanking-demo.jar list-audit");
            System.exit(1);
        }

        try {
            if ("schedule".equalsIgnoreCase(args[0])) {
                // schedule <from> <to> <amount> <delay-days> <frequency> <token>
                String from = args[1];
                String to = args[2];
                double amount = Double.parseDouble(args[3]);
                int delayDays = Integer.parseInt(args[4]);
                TransferFrequency freq = TransferFrequency.valueOf(args[5].toUpperCase());
                String token = args[6];

                Database db = new Database();
                AccountService service = new AccountService();
                AuditService auditService = new AuditService(db);
                ScheduledTransferService scheduled = new ScheduledTransferService(db, service, auditService);

                ScheduledTransfer st = scheduled.schedule(from, to, amount, delayDays, freq, token);
                System.out.println("Scheduled transfer created: " + st.getId());
                return;
            }

            if ("run-due".equalsIgnoreCase(args[0])) {
                Database db = new Database();
                AccountService service = new AccountService();
                AuditService auditService = new AuditService(db);
                ScheduledTransferService scheduled = new ScheduledTransferService(db, service, auditService);

                int executed = scheduled.runDueTransfers();
                System.out.println("Executed scheduled transfers: " + executed);
                return;
            }

            if ("list-scheduled".equalsIgnoreCase(args[0])) {
                Database db = new Database();
                AccountService service = new AccountService();
                AuditService auditService = new AuditService(db);
                ScheduledTransferService scheduled = new ScheduledTransferService(db, service, auditService);

                for (ScheduledTransfer st : scheduled.listAll()) {
                    System.out.println(st.getId() + " from=" + st.getFromAccountId() + " to=" + st.getToAccountId()
                            + " amount=" + st.getAmount() + " nextRun=" + st.getNextRunEpochMillis()
                            + " freq=" + st.getFrequency());
                }
                return;
            }

            if ("list-audit".equalsIgnoreCase(args[0])) {
                Database db = new Database();
                AuditService auditService = new AuditService(db);
                for (AuditEvent ev : auditService.listAll()) {
                    System.out.println(ev.timestampEpochMillis() + " " + ev.eventType() + " " + ev.payload());
                }
                return;
            }

            String accountId = args[0];
            // type 1=standard 2=premium 3=vip (le junior a mis ça en dur)
            int reportType = 1;

            AccountService service = new AccountService();

            double balance = service.getBalance(accountId);
            System.out.println("Balance for account " + accountId + " is " + balance);

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
