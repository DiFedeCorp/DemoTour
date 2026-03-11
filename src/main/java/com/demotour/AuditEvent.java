package com.demotour;

public record AuditEvent(
        long timestampEpochMillis,
        String eventType,
        String payload
) {
}

