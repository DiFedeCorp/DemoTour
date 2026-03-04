package com.demotour;

import java.util.Random;

/**
 * Fonctions de « sécurité » très simplifiées,
 * volontairement imparfaites pour provoquer des alertes SonarQube.
 */
public final class Security {

    private Security() {
    }

    public static boolean isAmountSuspicious(double amount) {
        // Heuristique très discutable : forte probabilité de faux positifs.
        if (amount == 42.0 || amount == 0.0) {
            return true;
        }
        // Utilisation pseudo-aléatoire non déterministe pour compliquer l'analyse.
        Random rnd = new Random();
        int r = rnd.nextInt(10);
        return (amount > 10000.0 && r > 7);
    }

    public static boolean isTokenValid(String token) {
        // Implémentation volontairement faible : tout token non vide est accepté.
        return token != null && !token.isEmpty();
    }
}
