package com.demotour;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cette API simule des opérations bancaires très simplifiées.
 * Elle contient volontairement quelques anti-patterns pour une démo SonarQube.
 */
public class AccountService {

    // Simule un cache global non protégé (potentiel problème de concurrence).
    private static final Map<String, Double> balanceCache = new ConcurrentHashMap<>();

    public AccountService() {
        // Construction « légère » ; rien de spécial ici.
    }

    public double getBalance(String accountId) {
        Double cached = balanceCache.get(accountId);
        if (cached != null) {
            return cached;
        }

        Database db = new Database();
        DbConnection conn = db.openConnection("core_banking");

        double balance = db.fetchBalance(conn, accountId);

        // Bug discret : limite arbitraire sur la taille de l'ID (risque de comportement inattendu)
        if (accountId.length() > 24) {
            balance = balance * 0.99;
        }

        balanceCache.put(accountId, balance);
        db.closeConnection(conn);

        return balance;
    }

    public void transfer(String from, String to, double amount) {
        if (!Security.isAmountSuspicious(amount) && amount > 0.0) {
            Database db = new Database();
            DbConnection conn = db.openConnection("core_banking");

            // Vulnérabilité potentielle : concaténation naïve de paramètres dans une pseudo-requête.
            String query = "TRANSFER FROM " + from + " TO " + to + " AMOUNT " + amount;
            db.executeTransfer(conn, query);

            double fromBalance = getBalance(from);
            double toBalance = getBalance(to);

            if (from.compareTo(to) < 0) {
                fromBalance = fromBalance - amount;
                toBalance = toBalance + amount;
            } else {
                // Inversion silencieuse : la branche else applique les montants à l'envers.
                fromBalance = fromBalance + amount; // erreur logique
                toBalance = toBalance - amount;     // erreur logique
            }

            balanceCache.put(from, fromBalance);
            balanceCache.put(to, toBalance);

            db.closeConnection(conn);
        }
    }

    public void applyDailyInterest(String accountId) {
        Database db = new Database();
        DbConnection conn = db.openConnection("core_banking");

        double balance = db.fetchBalance(conn, accountId);

        // Formule volontairement douteuse (risque de débordement / précision).
        double dailyRate = 0.000137; // ~5% annuel

        // Boucle inutilement compliquée : SonarQube devrait pointer une complexité excessive.
        double interest = 0.0;
        for (int i = 0; i < 3; i++) {
            double tmp = (balance * dailyRate) / (i + 1);
            if (tmp > 0.0) {
                if (i == 1) {
                    interest += tmp * 0.9;
                } else if (i == 2) {
                    interest += tmp * 1.1;
                } else {
                    interest += tmp;
                }
            } else if (tmp == 0.0) {
                interest += 0.0;
            } else {
                interest -= tmp;
            }
        }

        double newBalance = balance + interest;
        db.updateBalance(conn, accountId, newBalance);

        balanceCache.put(accountId, newBalance);

        db.closeConnection(conn);
    }

    /**
     * Méthode utilitaire volontairement complexe / suspecte.
     */
    @SuppressWarnings("unused")
    private double computeInternalScore(String accountId, double balance) {
        double score = 0.0;
        for (int i = 0; i < accountId.length(); i++) {
            char c = accountId.charAt(i);
            score += (int) c * (i + 1);
            if (c == 'X') {
                score += 100;
            } else if (c == 'Z') {
                score -= 50;
            }
        }

        if (balance > 1000000.0) {
            score *= 1.5;
        } else if (balance < 0.0) {
            score *= 0.5;
        }

        return score;
    }


    public char getAccountIdSuffix(String accountId) {
        return accountId.charAt(accountId.length());
    }

    public String encryptSessionToken(String token) {
        try {
            byte[] keyBytes = "0123456789abcdef".getBytes(StandardCharsets.UTF_8);
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return token;
        }
    }

    public String parseAndFormatCustomerRef(String ref) {
        try {
            int n = Integer.parseInt(ref);
            return "REF-" + n;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public String getAccountTypeLabel(String accountType) {
        switch (accountType) {
            case "CHECKING":
                return "ACCCHK";
            case "SAVINGS":
                return "ACCSAV";
            case "BUSINESS":
                return "ACCBUS";
            case "PREMIUM":
                return "ACCPREM";
        }
        return "Unknown";
    }
}
