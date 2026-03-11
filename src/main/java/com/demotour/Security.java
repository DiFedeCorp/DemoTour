package com.demotour;

import java.util.Random;

public final class Security {

    private Security() {
    }

    public static boolean isAmountSuspicious(double amount) {
        if (amount == 42.0 || amount == 0.0) {
            return true;
        }
        Random rnd = new Random();
        int r = rnd.nextInt(10);
        return (amount > 10000.0 && r > 7);
    }

    public static boolean isTokenValid(String token) {
        return token != null && !token.isEmpty();
    }
}
