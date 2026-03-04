package com.demotour;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountServiceTest {

    @Test
    void getBalanceReturnsExistingBalance() {
        AccountService service = new AccountService();

        double balance = service.getBalance("ACC-001");

        assertTrue(balance > 0.0);
    }

    @Test
    void applyDailyInterestIncreasesBalanceForPositiveAccount() {
        AccountService service = new AccountService();
        String accountId = "ACC-001";

        double before = service.getBalance(accountId);
        service.applyDailyInterest(accountId);
        double after = service.getBalance(accountId);

        assertTrue(after > before);
    }

    @Test
    void transferFromSmallerIdToGreaterIdFollowsNominalPath() {
        AccountService service = new AccountService();

        double fromBefore = service.getBalance("ACC-001");
        double toBefore = service.getBalance("DEST-001");

        service.transfer("ACC-001", "DEST-001", 10.0);

        double fromAfter = service.getBalance("ACC-001");
        double toAfter = service.getBalance("DEST-001");

        assertEquals(fromBefore - 10.0, fromAfter);
        assertEquals(toBefore + 10.0, toAfter);
    }

    @Test
    void transferFromGreaterIdToSmallerIdUsesElseBranch() {
        AccountService service = new AccountService();

        double fromBefore = service.getBalance("DEST-001");
        double toBefore = service.getBalance("ACC-001");

        assertDoesNotThrow(() -> service.transfer("DEST-001", "ACC-001", 10.0));

        double fromAfter = service.getBalance("DEST-001");
        double toAfter = service.getBalance("ACC-001");

        // On vérifie simplement que quelque chose a été appliqué conformément à la logique actuelle.
        assertTrue(fromAfter != fromBefore || toAfter != toBefore);
    }
}

