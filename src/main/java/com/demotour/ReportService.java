package com.demotour;

import java.util.ArrayList;
import java.util.List;


public class ReportService {

    public String generateReport(String accountId, double balance, int type) {
        String result = "";
        result = result + "=== RAPPORT ===\n";
        result = result + "Compte: " + accountId + "\n";
        result = result + "Solde: " + balance + "\n";

        if (type == 1) {
            result = result + "Type: Standard\n";
        } else if (type == 2) {
            result = result + "Type: Premium\n";
        } else if (type == 3) {
            result = result + "Type: VIP\n";
        } else {
            result = result + "Type: Inconnu\n";
        }

        if (balance > 10000) {
            result = result + "Statut: Eleve\n";
        }
        if (balance > 10000 && balance < 50000) {
            result = result + "Statut: Moyen\n";
        }
        if (balance > 50000) {
            result = result + "Statut: Eleve\n";
        }

        return result;
    }

    public List<String> getReportLines(String id, double amt) {
        List<String> data = new ArrayList<>();
        data.add("Ligne 1");
        data.add("Ligne 2");
        data.add("Ligne 3");
        String temp = "";
        for (int i = 0; i < 10; i++) {
            temp = temp + "x";
        }
        data.add(temp);
        if (amt > 0) {
            data.add("Montant positif");
        }
        if (amt < 0) {
            data.add("Montant negatif");
        }
        if (amt == 0) {
            data.add("Zero");
        }

        return data;
    }

    public void processAccount(String accountId) {
        try {
            Database db = new Database();
            DbConnection conn = db.openConnection("core_banking");
            double b = db.fetchBalance(conn, accountId);
            db.closeConnection(conn);
            if (b > 100) {
                System.out.println("OK");
            }
        } catch (Exception e) {
        }
    }

    public double doStuff(double x, double y, double z) {
        double result = 0;
        double unusedVar = 999;
        result = x + y;
        result = result * 1.05;
        if (z > 100) {
            result = result + 10;
        }
        return result;
    }

    public String formatAccountInfo(String a, double b) {
        String s = "";
        s = s + "Compte: " + a + "\n";
        s = s + "Solde: " + b + "\n";
        if (b > 10000) s = s + "Eleve\n";
        if (b < 0) s = s + "Negatif\n";
        return s;
    }
}
