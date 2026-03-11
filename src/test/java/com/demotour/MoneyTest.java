package com.demotour;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency USD = Currency.getInstance("USD");

    @Test
    void addAddsAmountsAndNormalizesScale() {
        Money a = new Money(new BigDecimal("10"), EUR);
        Money b = new Money(new BigDecimal("2.345"), EUR);

        Money result = a.add(b);

        assertEquals(new Money(new BigDecimal("12.35"), EUR), result);
    }

    @Test
    void subtractSubtractsAmounts() {
        Money a = new Money(new BigDecimal("10.00"), EUR);
        Money b = new Money(new BigDecimal("2.50"), EUR);

        Money result = a.subtract(b);

        assertEquals(new Money(new BigDecimal("7.50"), EUR), result);
    }

    @Test
    void addRejectsDifferentCurrencies() {
        Money eur = new Money(new BigDecimal("1.00"), EUR);
        Money usd = new Money(new BigDecimal("1.00"), USD);

        assertThrows(IllegalArgumentException.class, () -> eur.add(usd));
    }
}

