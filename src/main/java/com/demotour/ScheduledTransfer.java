package com.demotour;

import java.util.Objects;

public final class ScheduledTransfer {
    private final String id;
    private final String fromAccountId;
    private final String toAccountId;
    private final double amount;
    private final long nextRunEpochMillis;
    private final TransferFrequency frequency;
    private final String createdBy;

    public ScheduledTransfer(
            String id,
            String fromAccountId,
            String toAccountId,
            double amount,
            long nextRunEpochMillis,
            TransferFrequency frequency,
            String createdBy
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.fromAccountId = Objects.requireNonNull(fromAccountId, "fromAccountId must not be null");
        this.toAccountId = Objects.requireNonNull(toAccountId, "toAccountId must not be null");
        this.frequency = Objects.requireNonNull(frequency, "frequency must not be null");
        this.createdBy = createdBy;
        this.amount = amount;
        this.nextRunEpochMillis = nextRunEpochMillis;
    }

    public String getId() {
        return id;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public long getNextRunEpochMillis() {
        return nextRunEpochMillis;
    }

    public TransferFrequency getFrequency() {
        return frequency;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}

