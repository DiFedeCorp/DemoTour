package com.demotour;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityTest {

    @Test
    void amountEqualTo42IsAlwaysSuspicious() {
        assertTrue(Security.isAmountSuspicious(42.0));
    }

    @Test
    void amountEqualToZeroIsAlwaysSuspicious() {
        assertTrue(Security.isAmountSuspicious(0.0));
    }

    @RepeatedTest(5)
    void smallPositiveAmountIsNormallyNotSuspicious() {
        assertFalse(Security.isAmountSuspicious(10.0));
    }

    @Test
    void nonEmptyTokenIsValid() {
        assertTrue(Security.isTokenValid("token"));
    }

    @Test
    void emptyOrNullTokenIsInvalid() {
        assertFalse(Security.isTokenValid(""));
        assertFalse(Security.isTokenValid(null));
    }
}

