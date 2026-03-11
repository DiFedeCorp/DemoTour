package com.demotour;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Objet-valeur immutable pour manipuler des montants monétaires.
 * Invariants :
 * - devise non nulle
 * - montant non nul, normalisé à 2 décimales (HALF_UP)
 */
public record Money(BigDecimal amount, Currency currency) {

    private static final int DEFAULT_SCALE = 2;

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        amount = amount.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "other must not be null");
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must match: " + currency + " vs " + other.currency);
        }
    }
}

