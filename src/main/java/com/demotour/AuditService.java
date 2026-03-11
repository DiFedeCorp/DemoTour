package com.demotour;

import java.util.List;

public class AuditService {

    private final Database db;

    public AuditService(Database db) {
        this.db = db;
    }

    public void record(String type, String payload) {
        AuditEvent ev = new AuditEvent(System.currentTimeMillis(), type, payload);
        db.appendAuditEvent(ev);
        System.out.println("[AUDIT] " + type + " :: " + payload);
    }

    public List<AuditEvent> listAll() {
        return db.listAuditEvents();
    }
}

