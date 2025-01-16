import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistique {
    public static int NbProposeRecom = 0;
    public static int NbChoisiRecom = 0;
    public static int NbProposeRempla = 0;
    public static int NbPChoisiRempla = 0;

    public static List<List<String>> produit(JDBC database, String condition) {
        Map<String, String> conditions = new HashMap<>(Map.of(
                "Le plus commendé", "Nombre de commande",
                "Le plus vendu", "Quantité vendu",
                "Le plus grand CA", "CA"));

        if (conditions.containsKey(condition)) {
            List<List<String>> queryResult = database.executeQuery(String.format("""
                    SELECT
                        Produit.ProduitId,
                        NomProd,
                        COUNT(CommandeId) AS "Nombre de commande",
                        SUM(QteCom) AS "Quantité vendu",
                        SUM(
                            CASE
                                WHEN PrixUnitaire IS NOT NULL
                                THEN QteCom * PrixUnitaire
                                ELSE QteCom * PrixAuKg * Poids / 1000
                            END
                        ) AS "CA"
                    FROM Produit
                    JOIN Composer ON Produit.ProduitId = Composer.ProduitId
                    GROUP BY
                        Produit.ProduitId,
                        NomProd
                    ORDER BY
                        "%s" DESC
                    """, conditions.get(condition)));
            return queryResult;
        } else {
            System.err.println("Invalid condition\n");
            return null;
        }
    }

    public static List<List<String>> categorie(JDBC database, String condition) {
        Map<String, String> conditions = new HashMap<>(Map.of(
                "Le plus commendé", "Nombre de commande",
                "Le plus vendu", "Quantité vendu",
                "Le plus grand CA", "CA"));

        // Catégories manquants
        if (conditions.containsKey(condition)) {
            List<List<String>> queryResult = database.executeQuery(String.format("""
                    SELECT
                        Categorie.CategorieId,
                        NomCat,
                        COUNT(CommandeId) AS "Nombre de commande",
                        SUM(QteCom) AS "Quantité vendu",
                        SUM(
                            CASE
                                WHEN PrixUnitaire IS NOT NULL
                                THEN QteCom * PrixUnitaire
                                ELSE QteCom * PrixAuKg * Poids / 1000
                            END
                        ) AS "CA"
                    FROM Categorie
                    JOIN Associer ON Categorie.CategorieId = Associer.CategorieId
                    JOIN Produit ON Associer.ProduitId = Produit.ProduitId
                    JOIN Composer ON Produit.ProduitId = Composer.ProduitId
                    GROUP BY
                        Categorie.CategorieId,
                        NomCat
                    ORDER BY
                        "%s" DESC
                    """, conditions.get(condition)));
            return queryResult;
        } else {
            System.err.println("Invalid condition\n");
            return null;
        }
    }

    public static List<List<String>> client(JDBC database, String condition) {
        Map<String, String> conditions = new HashMap<>(Map.of(
                "Le plus commendé", "Nombre de commande",
                "Le plus acheté", "Quantité acheté",
                "Le plus grand CA", "CA"));

        if (conditions.containsKey(condition)) {
            List<List<String>> queryResult = database.executeQuery(String.format("""
                    SELECT
                        Client.ClientId,
                        Nom,
                        Prenom,
                        COUNT(Commande.CommandeId) AS "Nombre de commande",
                        SUM(QteCom) AS "Quantité acheté",
                        SUM(
                            CASE
                                WHEN PrixUnitaire IS NOT NULL
                                THEN QteCom * PrixUnitaire
                                ELSE QteCom * PrixAuKg * Poids / 1000
                            END
                        ) AS "CA"
                    FROM Client
                    JOIN Commande ON Client.ClientId = Commande.ClientId
                    JOIN Composer ON Commande.CommandeId = Composer.CommandeId
                    JOIN Produit ON Composer.ProduitId = Produit.ProduitId
                    GROUP BY
                        Client.ClientId,
                        Nom,
                        Prenom
                    ORDER BY
                        "%s" DESC
                    """, conditions.get(condition)));
            return queryResult;
        } else {
            System.err.println("Invalid condition\n");
            return null;
        }
    }

    public static void general(JDBC database) {
        String prixMoyenneSurClients = database.executeQuery("""
                WITH ClientCA(ClientId, CA) AS (
                    SELECT
                        Client.ClientId,
                        SUM(
                            CASE
                                WHEN PrixUnitaire IS NOT NULL
                                THEN QteCom * PrixUnitaire
                                ELSE QteCom * PrixAuKg * Poids / 1000
                            END
                        )
                    FROM Client
                    JOIN Commande ON Client.ClientId = Commande.ClientId
                    JOIN Composer ON Commande.CommandeId = Composer.CommandeId
                    JOIN Produit ON Composer.ProduitId = Produit.ProduitId
                    GROUP BY
                        Client.ClientId
                )
                SELECT
                    CAST(AVG(CA) AS DECIMAL(38,2))
                FROM
                    ClientCA
                """).get(0).get(0);

        String qteMoyenneParCommande = database.executeQuery("""
                WITH CommandeQte(CommandeId, quentite) AS (
                    SELECT
                        CommandeId,
                        SUM(QteCom)
                    FROM Composer
                    JOIN Produit ON Composer.ProduitId = Produit.ProduitId
                    GROUP BY
                        CommandeId
                )
                SELECT
                    CAST(AVG(quentite) AS DECIMAL(38,2))
                FROM
                    CommandeQte
                """).get(0).get(0);

        List<List<String>> pourcentageCategorie = database.executeQuery("""
                WITH CA(CATotal) AS (
                    SELECT
                        SUM(
                            CASE
                                WHEN PrixUnitaire IS NOT NULL
                                THEN QteCom * PrixUnitaire
                                ELSE QteCom * PrixAuKg * Poids / 1000
                            END
                        )
                    FROM Produit
                    JOIN Composer ON Produit.ProduitId = Composer.ProduitId
                )
                SELECT
                    c.NomCat,
                    CAST(SUM(
                        CASE
                            WHEN PrixUnitaire IS NOT NULL
                            THEN QteCom * PrixUnitaire
                            ELSE QteCom * PrixAuKg * Poids / 1000
                        END
                    ) * 100 / (SELECT CATotal FROM CA) AS DECIMAL(38,2)) AS "Pourcetage(%)"
                FROM Categorie
                JOIN Etre ON Categorie.CategorieId = Etre.CategorieId_SousCategorie
                JOIN Categorie c ON c.CategorieId = Etre.CategorieId_Principale
                JOIN Associer ON Categorie.CategorieId = Associer.CategorieId
                JOIN Produit ON Associer.ProduitId = Produit.ProduitId
                JOIN Composer ON Produit.ProduitId = Composer.ProduitId
                GROUP BY
                    Etre.CategorieId_Principale,
                    c.NomCat
                ORDER BY
                    "Pourcetage(%)" DESC
                """);

        // Integer length = pourcentageCategorie.stream().mapToInt(e ->
        // e.get(0).length()).max();
        System.out.println(String.format("""
                Prix moyen d'une commande sur tous les clients: %s
                Quantité moyenne des produits achetés par commande: %s
                Pourcentage de CA des catégories principales :
                """,
                prixMoyenneSurClients,
                qteMoyenneParCommande));
        pourcentageCategorie.forEach(e -> {
            System.out.println(String.format("%35s\t%s", e.get(0), e.get(1)));
        });
    }

    public static void getTempsMoyenRealisationPanier(JDBC database) {
        List<List<String>> result = database.executeQuery(
                "SELECT AVG(HeureFin - HeureDebut) * 24 * 60 AS TempsMoyenPanier " +
                        "FROM Commande " +
                        "WHERE HeureFin IS NOT NULL");

        if (result.isEmpty() || result.getFirst().getFirst() == null) {
            System.out.println("Aucune donnée disponible pour calculer le temps moyen de réalisation des paniers.");
        } else {
            double tempsMoyen = Double.parseDouble(result.getFirst().getFirst()); // En minutes

            // Conversion en heures et minutes
            int heures = (int) tempsMoyen / 60;
            int minutes = (int) tempsMoyen % 60;

            System.out.printf("Temps moyen de réalisation des paniers : %d heures et %d minutes (%.2f heures)%n",
                    heures, minutes, tempsMoyen / 60);
        }

        // System.out.println(result);
        // return result.getFirst().getFirst();

    }

    public static void getTempsMoyenRealisationCommande(JDBC database) {
        List<List<String>> result = database.executeQuery(
                "SELECT AVG(HeureFinCommande - HeureFin) * 24 * 60 AS TempsMoyenCommande " +
                        "FROM Commande " +
                        "WHERE HeureFinCommande IS NOT NULL");
        if (result.isEmpty() || result.getFirst().getFirst() == null) {
            System.out.println("Aucune donnée disponible pour calculer le temps moyen de réalisation des Commandes.");
        } else {
            double tempsMoyen = Double.parseDouble(result.getFirst().getFirst()); // En minutes
            int heures = (int) tempsMoyen / 60;
            int minutes = (int) tempsMoyen % 60;

            System.out.printf("Temps moyen de réalisation des Commandes : %d heures et %d minutes (%.2f heures)%n",
                    heures, minutes, tempsMoyen / 60);
        }
    }

    public static double efficaciteRecom() {
        if (NbProposeRecom == 0) {
            return 0;
        }
        return NbChoisiRecom / NbChoisiRecom;
    }

    public static double efficaciteRenpla() {
        if (NbProposeRempla == 0) {
            return 0;
        }
        return NbPChoisiRempla / NbProposeRempla;
    }

}
