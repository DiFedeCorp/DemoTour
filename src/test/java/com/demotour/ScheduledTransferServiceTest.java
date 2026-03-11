package com.demotour;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduledTransferServiceTest {

    @Test
    void scheduleAndRunDueOnceTransferMovesMoneyAndRemovesSchedule() {
        Database db = new Database();
        AccountService accountService = new AccountService();
        AuditService auditService = new AuditService(db);
        ScheduledTransferService service = new ScheduledTransferService(db, accountService, auditService);

        double fromBefore = accountService.getBalance("ACC-001");
        double toBefore = accountService.getBalance("DEST-001");

        ScheduledTransfer st = service.schedule(
                "ACC-001",
                "DEST-001",
                10.0,
                0,
                TransferFrequency.ONCE,
                "token"
        );

        assertNotNull(st.getId());

        int executed = service.runDueTransfers();
        assertEquals(1, executed);

        double fromAfter = accountService.getBalance("ACC-001");
        double toAfter = accountService.getBalance("DEST-001");
        assertEquals(fromBefore - 10.0, fromAfter);
        assertEquals(toBefore + 10.0, toAfter);

        assertTrue(service.listAll().isEmpty());
        assertFalse(auditService.listAll().isEmpty());
    }
}

