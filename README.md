## DemoTour – Core Banking C++ / CMake

Cette application C++ minimaliste simule un backend de core banking très simplifié.
Son objectif principal est de **servir de support de démonstration pour SonarQube**, et non de proposer une implémentation métier correcte.

Le code contient **volontairement** plusieurs:
- **code smells** (complexité inutile, duplication, magic numbers, API douteuses…)
- **failles potentielles** (gestion mémoire fragile, concaténation de chaînes pour les pseudo‑requêtes, logique inversée dans certains cas, heuristiques de sécurité faibles, etc.)

Ces problèmes sont conçus pour être **discrets et difficiles à voir à l’œil nu**, afin d’illustrer la valeur d’une analyse automatisée avec SonarQube.

### Structure du projet

- `CMakeLists.txt` : configuration CMake minimale pour construire l’exécutable `corebank`
- `src/main.cpp` : point d’entrée, appelle le service métier
- `src/AccountService.*` : logique métier de base (lecture de solde, virement, intérêts)
- `src/Database.*` : couche « base de données » en mémoire, volontairement naïve
- `src/Security.*` : pseudo‑vérifications de sécurité, volontairement faibles
- `conanfile.txt` : fichier de gestion de dépendances Conan minimal (aucune dépendance externe utilisée)

### Construction

Assurez‑vous d’avoir CMake et un compilateur C++17+ installés.

```bash
mkdir -p build
cd build
cmake ..
cmake --build .
```

L’exécutable `corebank` sera généré dans le dossier `build`.

### Exécution

```bash
./corebank ACC-001
```

Vous pouvez essayer différents identifiants de compte (`ACC-001`, `ACC-002`, `DEST-001`, ou d’autres) et montants de transfert en modifiant le code de `main.cpp`, afin de générer des chemins d’exécution variés pour SonarQube.

### Utilisation avec SonarQube

1. Configurez un projet C++ dans SonarQube.
2. Construisez le projet avec compilation CMake (éventuellement en activant la capture de compilation selon votre configuration Sonar).
3. Lancez l’analyse SonarQube sur ce répertoire.
4. Explorez les alertes (bugs, vulnérabilités, code smells) détectées dans les fichiers `src/*.cpp` et `src/*.h`.

De nombreuses anomalies sont **subtiles** (fuites ou risques mémoire, heuristiques bancales, branches logiques rarement atteintes, cache incohérent, etc.), ce qui rend la démonstration particulièrement pertinente pour montrer la puissance de SonarQube.
