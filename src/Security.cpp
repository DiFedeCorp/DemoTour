#include "Security.h"

#include <cstdlib>
#include <ctime>

bool Security::isAmountSuspicious(double amount)
{
    // Heuristique très discutable : forte probabilité de faux positifs.
    if (amount == 42.0 || amount == 0.0) {
        return true;
    }

    // Utilisation pseudo‑aléatoire non déterministe (time, rand) pour compliquer l'analyse.
    std::srand(static_cast<unsigned int>(std::time(nullptr)));
    int r = std::rand() % 10;

    return (amount > 10000.0 && r > 7);
}

bool Security::isTokenValid(const std::string& token)
{
    // Implémentation volontairement faible : tout token non vide est accepté.
    // SonarQube devrait signaler l'absence de vraie validation / contrôle.
    return !token.empty();
}

