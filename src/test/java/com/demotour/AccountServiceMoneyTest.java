package com.demotour;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class AccountServiceMoneyTest {

    @Test
    void getBalanceAsMoney_wrapsBalanceWithCurrency() {
        AccountService service = new AccountService();
        Money balance = service.getBalanceAsMoney("ACC-001", Currency.getInstance("EUR"));

        assertEquals(Currency.getInstance("EUR"), balance.currency());
    }

    @Test
    void transfer_acceptsMoneyOverload() {
        AccountService service = new AccountService();

        service.transfer("ACC-001", "DEST-001", Money.eur(10));

        assert(service.getBalance("DEST-001") > 250.0);
    }
}

