package com.demotour;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pseudo couche d'accès aux données, volontairement naïve.
 */
public class Database {

    // Store en mémoire pour la démo.
    private static final Map<String, Double> store = new ConcurrentHashMap<>();

    static {
        store.put("ACC-001", 1000.0);
        store.put("ACC-002", 5000.5);
        store.put("DEST-001", 250.0);
    }

    public DbConnection openConnection(String dbName) {
        // Simule un handle : limite volontairement fragile (risque si dbName trop long).
        DbConnection conn = new DbConnection();
        conn.rawHandle = dbName.length() > 32 ? dbName.substring(0, 32) : dbName;
        return conn;
    }

    public void closeConnection(DbConnection conn) {
        if (conn != null && conn.rawHandle != null) {
            conn.rawHandle = null;
        }
    }

    public double fetchBalance(DbConnection conn, String accountId) {
        return readFromInMemoryStore(accountId);
    }

    public void updateBalance(DbConnection conn, String accountId, double newBalance) {
        writeToInMemoryStore(accountId, newBalance);
    }

    public void executeTransfer(DbConnection conn, String rawQuery) {
        if (conn == null || conn.rawHandle == null) {
            System.err.println("Invalid connection");
            return;
        }
        // Affiche la requête : dans un vrai système ceci serait un risque de log sensible.
        System.out.println("[DB] Executing: " + rawQuery);
    }

    private double readFromInMemoryStore(String accountId) {
        return store.getOrDefault(accountId, 0.0);
    }

    private void writeToInMemoryStore(String accountId, double value) {
        // Gère aussi la création de compte à la volée, sans validation.
        store.put(accountId, value);
    }
}
