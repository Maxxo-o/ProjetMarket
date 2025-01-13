import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistique {
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
                        COUNT(CommandeId) AS \"Nombre de commande\",
                        SUM(QteCom) AS \"Quantité vendu\",
                        SUM(
                        CASE
                            WHEN PrixUnitaire IS NOT NULL
                            THEN QteCom * PrixUnitaire
                            ELSE QteCom * PrixAuKg * Poids
                        END
                    ) AS \"CA\"
                    FROM Produit
                    JOIN Composer ON Produit.ProduitId = Composer.ProduitId
                    GROUP BY
                        Produit.ProduitId,
                        NomProd
                    ORDER BY
                        \"%s\" DESC
                    """, conditions.get(condition)));
            return queryResult;
        } else {
            System.err.println("Invalid condition");
            return null;
        }
    }

    public static List<List<String>> categorie(JDBC database, String condition) {
        // Catégories manquants
        Map<String, String> conditions = new HashMap<>(Map.of(
                "Le plus commendé", "Nombre de commande",
                "Le plus vendu", "Quantité vendu",
                "Le plus grand CA", "CA"));

        List<List<String>> queryResult = database.executeQuery("""
                SELECT

                FROM
                GROUP BY

                """);

        return queryResult;
    }

    public static List<List<String>> client(JDBC database, String condition) {
        Map<String, String> conditions = new HashMap<>(Map.of(
                "Le plus commendé", "Nombre de commande",
                "Le plus acheté", "Quantité acheté",
                "Le plus grand CA", "CA"));

        List<List<String>> queryResult = database.executeQuery(String.format("""
                SELECT
                    Client.ClientId,
                    Nom,
                    Prenom,
                    COUNT(Commande.CommandeId) AS \"Nombre de commande\",
                    SUM(QteCom) AS \"Quantité acheté\",
                    SUM(
                        CASE
                            WHEN PrixUnitaire IS NOT NULL
                            THEN QteCom * PrixUnitaire
                            ELSE QteCom * PrixAuKg * Poids
                        END
                    ) AS \"CA\"
                FROM Client
                JOIN Commande ON Client.ClientId = Commande.ClientId
                JOIN Composer ON Commande.CommandeId = Composer.CommandeId
                JOIN Produit ON Composer.ProduitId = Produit.ProduitId
                GROUP BY
                    Client.ClientId,
                    Nom,
                    Prenom
                ORDER BY
                    \"%s\" DESC
                """, conditions.get(condition)));

        return queryResult;
    }
}
