# 🛒 SuperMarché CLI - Projet Java MIAGE

Bienvenue dans **SuperMarché CLI**, une application Java en ligne de commande qui simule un supermarché en ligne. Ce projet a été réalisé dans le cadre du Master 1 MIAGE. Il permet aux utilisateurs de faire leurs courses directement depuis une interface en ligne de commande, avec gestion d’un catalogue, d’un panier et d’une base de données SQLite.

## 🎯 Objectifs du projet

- Simuler un supermarché numérique avec gestion de produits et d’utilisateurs.
- Permettre à un utilisateur de :
  - Consulter les produits disponibles
  - Ajouter ou retirer des articles à son panier
  - Finaliser ses achats
- Utiliser une base de données SQLite pour la persistance des données.
- Offrir une interface simple et intuitive en ligne de commande (CLI).

## 🛠️ Technologies utilisées

- **Java 17+**
- **SQLite** via JDBC
- **Maven** (ou autre outil de build, si utilisé)
- Interface **CLI** (console)

## 🗃️ Structure du projet

supermarche-cli/
│
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── model/ # Classes métiers : Produit, Utilisateur, Panier, etc.
│ │ │ ├── dao/ # Accès aux données (produits, commandes, etc.)
│ │ │ ├── service/ # Logique métier
│ │ │ └── Main.java # Point d'entrée de l'application CLI
│ │ └── resources/
│ │ └── supermarche.db # Base de données SQLite
└── README.md

bash
Copier
Modifier

## 🚀 Lancer l'application

### 1. Cloner le dépôt

git clone https://github.com/votre-utilisateur/supermarche-cli.git
cd supermarche-cli
2. Compiler et exécuter
bash
Copier
Modifier
javac -d out src/main/java/**/*.java
java -cp out Main
Ou avec Maven :

bash
Copier
Modifier
mvn compile
mvn exec:java -Dexec.mainClass="Main"
3. Utilisation
Une fois lancé, suivez les instructions dans la console pour :

🔍 Rechercher un produit

➕ Ajouter au panier

🧾 Visualiser le panier

💳 Passer la commande

🧪 Exemples de commandes
text
Copier
Modifier
1 - Lister les produits
2 - Rechercher un produit
3 - Ajouter au panier
4 - Voir le panier
5 - Finaliser la commande
0 - Quitter
💾 Base de données
La base SQLite est stockée localement et contient les tables suivantes :

produits : liste des produits disponibles

commandes : historique des commandes

panier : contenu du panier temporaire

La structure de la base est initialisée automatiquement au premier lancement.

👨‍💻 Auteurs
Étudiant(e) : [Votre nom ici]

Master 1 MIAGE – [Nom de l'université]

📄 Licence
Ce projet est à but éducatif dans le cadre du cursus universitaire. Reproduction ou réutilisation libre dans un contexte académique.

