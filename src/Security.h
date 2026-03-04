#pragma once

#include <string>

// Fonctions de « sécurité » très simplifiées,
// volontairement imparfaites pour provoquer des alertes SonarQube.

class Security {
public:
    static bool isAmountSuspicious(double amount);

    static bool isTokenValid(const std::string& token);
};

