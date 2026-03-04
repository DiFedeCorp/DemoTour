#include <iostream>
#include <string>
#include <stdexcept>

#include "AccountService.h"

int main(int argc, char** argv)
{
    // Application de démonstration minimaliste pour SonarQube.
    // Ce code contient volontairement des défauts subtils.

    if (argc < 2) {
        std::cerr << "Usage: " << argv[0] << " <account-id>" << std::endl;
        return 1;
    }

    std::string accountId = argv[1];

    try {
        AccountService service;

        double balance = service.getBalance(accountId);
        std::cout << "Balance for account " << accountId << " is " << balance << std::endl;

        // Opérations basiques pour alimenter des règles SonarQube
        service.applyDailyInterest(accountId);
        service.transfer(accountId, "DEST-001", 42.50);

    } catch (const std::exception& ex) {
        std::cerr << "Unexpected error: " << ex.what() << std::endl;
        return 2;
    }

    return 0;
}

