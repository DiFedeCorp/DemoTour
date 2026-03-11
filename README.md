## DemoTour – Core Banking Java / Maven

Cette application Java minimaliste simule un backend de core banking très simplifié.
Son objectif principal est de **servir de support de démonstration pour SonarQube**, et non de proposer une implémentation métier correcte.

Le code contient **volontairement** plusieurs:
- **code smells** (complexité inutile, duplication, magic numbers, API douteuses…)
- **failles potentielles** (concaténation de chaînes pour les pseudo-requêtes, logique inversée dans certains cas, heuristiques de sécurité faibles, etc.)

### Structure du projet

- `pom.xml` : configuration Maven pour construire le JAR
- `src/main/java/com/demotour/Main.java` : point d'entrée, appelle le service métier
- `src/main/java/com/demotour/AccountService.java` : logique métier de base (lecture de solde, virement, intérêts)
- `src/main/java/com/demotour/Database.java` : couche « base de données » en mémoire, volontairement naïve
- `src/main/java/com/demotour/Security.java` : pseudo-vérifications de sécurité, volontairement faibles
- `src/main/java/com/demotour/DbConnection.java` : type représentant une connexion simulée

### Prérequis

- JDK 17+
- Maven 3.6+

### Construction

```bash
mvn clean package
```

Le JAR exécutable sera généré dans `target/corebanking-demo-0.1.0.jar`.

### Exécution

```bash
java -jar target/corebanking-demo-0.1.0.jar ACC-001
```

Ou avec Maven :

```bash
mvn exec:java -Dexec.mainClass="com.demotour.Main" -Dexec.args="ACC-001"
```

Vous pouvez essayer différents identifiants de compte (`ACC-001`, `ACC-002`, `DEST-001`, ou d'autres) et montants de transfert en modifiant le code de `Main.java`, afin de générer des chemins d'exécution variés pour SonarQube.

### Utilisation avec SonarQube

1. Configurez un projet Java dans SonarQube.
2. Lancez l'analyse SonarQube sur ce répertoire (Maven est détecté automatiquement).
3. Explorez les alertes (bugs, vulnérabilités, code smells) détectées dans les fichiers `src/main/java/com/demotour/*.java`.
