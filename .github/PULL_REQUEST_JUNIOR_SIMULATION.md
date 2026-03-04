# [SIMULATION] PR #42 — Ajout du service de rapport (junior dev)

## Description
J'ai ajouté un nouveau service pour générer des rapports sur les comptes comme demandé. 
J'ai aussi modifié le Main pour afficher le rapport au démarrage.

## Fichiers modifiés
- **Nouveau:** `ReportService.java` — génération des rapports et formatage
- **Modifié:** `Main.java` — appel du rapport après récupération du solde

## Comment tester
Lancer l'app avec un account-id, le rapport s'affiche dans la console.

---
*Cette PR simule les code smells typiques d'un junior : magic numbers, duplication, noms vagues, catch vide, code commenté, concaténation en boucle, méthodes trop longues.*
