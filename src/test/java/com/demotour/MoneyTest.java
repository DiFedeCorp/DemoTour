package com.demotour;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void of_roundsTo2DecimalsHalfUp() {
        Money m = Money.of(1.235, Currency.getInstance("EUR"));
        assertEquals(new BigDecimal("1.24"), m.amount());
    }

    @Test
    void eur_factorySetsCurrency() {
        Money m = Money.eur(10);
        assertEquals(Currency.getInstance("EUR"), m.currency());
        assertEquals(new BigDecimal("10.00"), m.amount());
    }

    @Test
    void add_requiresSameCurrency() {
        Money eur = Money.of(1, Currency.getInstance("EUR"));
        Money usd = Money.of(1, Currency.getInstance("USD"));
        assertThrows(IllegalArgumentException.class, () -> eur.add(usd));
    }
}
