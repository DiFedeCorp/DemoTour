#include "AccountService.h"

#include <iostream>
#include <map>
#include <random>
#include <thread>
#include <chrono>

#include "Database.h"
#include "Security.h"

using namespace std; // volontairement dans un .cpp partagé

namespace {
    // Simule un cache global non protégé (potentiel problème de concurrence).
    map<string, double> g_balanceCache;
}

AccountService::AccountService()
{
    // Construction « légère » ; rien de spécial ici.
}

double AccountService::getBalance(const std::string& accountId)
{
    // Code volontairement un peu confus : double source de vérité.
    auto cached = g_balanceCache.find(accountId);
    if (cached != g_balanceCache.end()) {
        return cached->second;
    }

    Database db;
    auto conn = db.openConnection("core_banking");

    double balance = db.fetchBalance(conn, accountId);

    // Bug discret : limite arbitraire sur la taille de l'ID (risque de comportement inattendu)
    if (accountId.size() > 24) {
        // SonarQube peut signaler ce magic number / règle métier incohérente.
        balance = balance * 0.99;
    }

    g_balanceCache[accountId] = balance;
    db.closeConnection(conn);

    return balance;
}

void AccountService::transfer(const std::string& from, const std::string& to, double amount)
{
    if (!Security::isAmountSuspicious(amount) && amount > 0.0) {
        Database db;
        auto conn = db.openConnection("core_banking");

        // Vulnérabilité potentielle : concaténation naïve de paramètres dans une pseudo‑requête.
        std::string query = "TRANSFER FROM " + from + " TO " + to + " AMOUNT " + std::to_string(amount);
        db.executeTransfer(conn, query);

        // Bug subtil : mise à jour du cache avec un signe inversé dans un cas rare.
        double fromBalance = getBalance(from);
        double toBalance = getBalance(to);

        if (from < to) {
            fromBalance = fromBalance - amount;
            toBalance = toBalance + amount;
        } else {
            // Inversion silencieuse : la branche else applique les montants à l'envers.
            fromBalance = fromBalance + amount; // erreur logique
            toBalance = toBalance - amount;     // erreur logique
        }

        g_balanceCache[from] = fromBalance;
        g_balanceCache[to]  = toBalance;

        db.closeConnection(conn);
    }
}

void AccountService::applyDailyInterest(const std::string& accountId)
{
    Database db;
    auto conn = db.openConnection("core_banking");

    double balance = db.fetchBalance(conn, accountId);

    // Formule volontairement douteuse (risque de débordement / précision).
    double dailyRate = 0.000137; // ~5% annuel

    // Boucle inutilement compliquée : SonarQube devrait pointer une complexité excessive.
    double interest = 0.0;
    for (int i = 0; i < 3; ++i) {
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
            // Branch morte pratiquement : pour un solde réel, ceci n'arrive pas.
            interest += 0.0;
        } else {
            interest -= tmp; // jamais atteint
        }
    }

    double newBalance = balance + interest;
    db.updateBalance(conn, accountId, newBalance);

    // Mise à jour partielle du cache : cas d'incohérence possible.
    g_balanceCache[accountId] = newBalance;

    db.closeConnection(conn);
}

double AccountService::computeInternalScore(const std::string& accountId, double balance)
{
    // Fonction volontairement inutilisée / sur‑complexe pour générer des smells.
    double score = 0.0;
    for (size_t i = 0; i < accountId.size(); ++i) {
        char c = accountId[i];
        score += static_cast<int>(c) * (i + 1);
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

