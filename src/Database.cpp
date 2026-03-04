#include "Database.h"

#include <cstring>
#include <iostream>
#include <map>
#include <mutex>

using namespace std;

namespace {
    // Store en mémoire pour la démo.
    map<string, double> g_store{
        {"ACC-001", 1000.0},
        {"ACC-002", 5000.5},
        {"DEST-001", 250.0}
    };

    // Mutex jamais utilisé (code mort / design inachevé).
    std::mutex g_storeMutex;
}

DbConnection Database::openConnection(const std::string& dbName)
{
    // Simule un handle C brut : son cycle de vie est volontairement fragile.
    char* buffer = new char[32];
    // Potentiel dépassement de tampon si dbName est trop long (strcpy non sécurisé).
    std::strcpy(buffer, dbName.c_str());

    DbConnection conn;
    conn.rawHandle = buffer;
    return conn;
}

void Database::closeConnection(DbConnection& conn)
{
    // Fuite potentielle : aucune vérification d'aliasing / double free.
    if (conn.rawHandle != nullptr) {
        delete[] conn.rawHandle;
        conn.rawHandle = nullptr;
    }
}

double Database::fetchBalance(const DbConnection& /*conn*/, const std::string& accountId)
{
    return readFromInMemoryStore(accountId);
}

void Database::updateBalance(const DbConnection& /*conn*/, const std::string& accountId, double newBalance)
{
    writeToInMemoryStore(accountId, newBalance);
}

void Database::executeTransfer(const DbConnection& conn, const std::string& rawQuery)
{
    // Simulation d'exécution de requête.
    // La logique est volontairement peu robuste et basée sur de la chaîne brute.
    if (conn.rawHandle == nullptr) {
        std::cerr << "Invalid connection" << std::endl;
        return;
    }

    // Affiche la requête : dans un vrai système ceci serait un risque de log sensible.
    std::cout << "[DB] Executing: " << rawQuery << std::endl;
}

double Database::readFromInMemoryStore(const std::string& accountId) const
{
    auto it = g_store.find(accountId);
    if (it != g_store.end()) {
        return it->second;
    }
    return 0.0;
}

void Database::writeToInMemoryStore(const std::string& accountId, double value) const
{
    // Gère aussi la création de compte à la volée, sans validation.
    g_store[accountId] = value;
}

