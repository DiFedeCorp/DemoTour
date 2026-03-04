#pragma once

#include <string>

// Cette API simule des opérations bancaires très simplifiées.
// Elle contient volontairement quelques anti‑patterns pour une démo SonarQube.

class AccountService {
public:
    AccountService();

    double getBalance(const std::string& accountId);

    void transfer(const std::string& from, const std::string& to, double amount);

    void applyDailyInterest(const std::string& accountId);

private:
    // Méthode utilitaire volontairement complexe / suspecte.
    double computeInternalScore(const std::string& accountId, double balance);
};

