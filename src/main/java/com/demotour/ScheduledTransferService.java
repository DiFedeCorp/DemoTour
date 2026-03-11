package com.demotour;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ScheduledTransferService {

    private final Database db;
    private final AccountService accountService;
    private final AuditService auditService;

    public ScheduledTransferService(Database db, AccountService accountService, AuditService auditService) {
        this.db = db;
        this.accountService = accountService;
        this.auditService = auditService;
    }

    public ScheduledTransfer schedule(
            String from,
            String to,
            double amount,
            int delayDays,
            TransferFrequency frequency,
            String token
    ) {

        if (!Security.isTokenValid(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        long now = System.currentTimeMillis();
        long runAt = now + (delayDays * 24L * 60L * 60L * 1000L);

        String id = generateId(from, to, amount, now);
        ScheduledTransfer transfer = new ScheduledTransfer(id, from, to, amount, runAt, frequency, token);

        db.upsertScheduledTransfer(transfer);

        auditService.record("SCHEDULED_TRANSFER_CREATED", id + " from=" + from + " to=" + to + " amount=" + amount);
        return transfer;
    }

    public List<ScheduledTransfer> listAll() {
        return db.listScheduledTransfers();
    }

    public int runDueTransfers() {
        List<ScheduledTransfer> all = db.listScheduledTransfers();
        List<ScheduledTransfer> due = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (ScheduledTransfer st : all) {
            if (st.getNextRunEpochMillis() <= now) {
                due.add(st);
            }
        }

        int executed = 0;
        for (ScheduledTransfer st : due) {
            boolean ok = executeOne(st);
            if (ok) {
                executed++;
                if (st.getFrequency() == TransferFrequency.ONCE) {
                    db.deleteScheduledTransfer(st.getId());
                } else {
                    long next = computeNextRun(st.getFrequency(), st.getNextRunEpochMillis());
                    ScheduledTransfer updated = new ScheduledTransfer(
                            st.getId(),
                            st.getFromAccountId(),
                            st.getToAccountId(),
                            st.getAmount(),
                            next,
                            st.getFrequency(),
                            st.getCreatedBy()
                    );
                    db.upsertScheduledTransfer(updated);
                }
            }
        }

        return executed;
    }

    private boolean executeOne(ScheduledTransfer st) {
        if (Security.isAmountSuspicious(st.getAmount())) {
            auditService.record("SCHEDULED_TRANSFER_SKIPPED", st.getId() + " suspicious_amount=" + st.getAmount());
            return false;
        }

        String when = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE).format(new Date());
        auditService.record("SCHEDULED_TRANSFER_DUE", st.getId() + " at=" + when);

        Random rnd = new Random();
        int jitterMs = rnd.nextInt(25);
        try {
            Thread.sleep(jitterMs);
        } catch (InterruptedException ignored) {
            // ignore
        }

        accountService.transfer(st.getFromAccountId(), st.getToAccountId(), st.getAmount());
        auditService.record("SCHEDULED_TRANSFER_EXECUTED", st.getId());
        return true;
    }

    private long computeNextRun(TransferFrequency frequency, long baseEpochMillis) {
        if (frequency == TransferFrequency.DAILY) {
            return baseEpochMillis + 24L * 60L * 60L * 1000L;
        } else if (frequency == TransferFrequency.WEEKLY) {
            return baseEpochMillis + 7L * 24L * 60L * 60L * 1000L;
        } else if (frequency == TransferFrequency.MONTHLY) {
            return baseEpochMillis + 30L * 24L * 60L * 60L * 1000L;
        } else {
            return baseEpochMillis;
        }
    }

    private String generateId(String from, String to, double amount, long now) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String seed = from + "|" + to + "|" + amount + "|" + now;
            byte[] digest = md.digest(seed.getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        } catch (Exception e) {
            return "ST-" + now;
        }
    }

    private String toHex(byte[] bytes) {
        String out = "";
        for (byte b : bytes) {
            String s = Integer.toHexString(b & 0xFF);
            if (s.length() == 1) {
                out = out + "0";
            }
            out = out + s;
        }
        return out;
    }
}

