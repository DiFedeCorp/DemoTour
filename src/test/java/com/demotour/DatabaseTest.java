package com.demotour;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseTest {

    @Test
    void openConnectionShouldCreateConnectionWithHandle() {
        Database db = new Database();
        DbConnection conn = db.openConnection("core_banking");

        assertNotNull(conn);
        assertNotNull(conn.rawHandle);
        assertEquals("core_banking", conn.rawHandle);
    }

    @Test
    void openConnectionShouldTruncateLongName() {
        Database db = new Database();
        String longName = "this_is_a_very_long_database_name_exceeding_32_chars";

        DbConnection conn = db.openConnection(longName);

        assertNotNull(conn.rawHandle);
        assertTrue(conn.rawHandle.length() <= 32);
    }

    @Test
    void fetchAndUpdateBalanceWorkOnInMemoryStore() {
        Database db = new Database();
        DbConnection conn = db.openConnection("core_banking");

        double initial = db.fetchBalance(conn, "ACC-001");
        db.updateBalance(conn, "ACC-001", initial + 100.0);
        double updated = db.fetchBalance(conn, "ACC-001");

        assertEquals(initial + 100.0, updated);

        db.closeConnection(conn);
    }
}

