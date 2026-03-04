#pragma once

#include <string>

// Pseudo couche d'accès aux données, volontairement naïve.

struct DbConnection {
    char* rawHandle; // abstrait, volontairement peu sûr
};

class Database {
public:
    DbConnection openConnection(const std::string& dbName);

    void closeConnection(DbConnection& conn);

    double fetchBalance(const DbConnection& conn, const std::string& accountId);

    void updateBalance(const DbConnection& conn, const std::string& accountId, double newBalance);

    void executeTransfer(const DbConnection& conn, const std::string& rawQuery);

private:
    double readFromInMemoryStore(const std::string& accountId) const;

    void writeToInMemoryStore(const std::string& accountId, double value) const;
};

