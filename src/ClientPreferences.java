import java.util.ArrayList;
import java.util.List;

public class ClientPreferences {

    // Méthode pour récupérer tous les résultats pour un client donné
    public static List<List<String>> getClientPreferences(int clientId) {
        // Connexion à la base de données via JDBC
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());

        // Initialisation de la liste pour stocker tous les résultats sous forme de List<List<String>>
        List<List<String>> allResults = new ArrayList<>();

        // 1. Nutriscore le plus commandé
        String nutriscoreQuery = "SELECT CASE " +
                "    WHEN p.Nutriscore = ' ' THEN 'Produit sans Nutriscore' " +
                "    ELSE p.Nutriscore " +
                "END AS NutriscoreLePlusCommande " +
                "FROM Produit p " +
                "JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "WHERE cmd.ClientId = " + clientId + " " +
                "GROUP BY p.Nutriscore " +
                "HAVING COUNT(*) = ( " +
                "    SELECT MAX(frequency) " +
                "    FROM ( " +
                "        SELECT COUNT(*) AS frequency " +
                "        FROM Produit p " +
                "        JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "        JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "        WHERE cmd.ClientId = " + clientId + " " +
                "        GROUP BY p.Nutriscore " +
                "    ) " +
                ")";
        List<List<String>> nutriscoreResult = database.executeQuery(nutriscoreQuery);
        if (nutriscoreResult != null && !nutriscoreResult.isEmpty()) {
            allResults.add(nutriscoreResult.get(0)); // Ajout de la première ligne de résultats pour le Nutriscore
        } else {
            List<String> emptyNutriscore = new ArrayList<>();
            emptyNutriscore.add("Aucun Nutriscore trouvé.");
            allResults.add(emptyNutriscore);
        }

        // 2. Catégorie de produit la plus commandée
        String categorieQuery = "SELECT p.Categorie " +
                "FROM Produit p " +
                "JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "WHERE cmd.ClientId = " + clientId + " " +
                "GROUP BY p.Categorie " +
                "HAVING COUNT(*) = ( " +
                "    SELECT MAX(frequency) " +
                "    FROM ( " +
                "        SELECT COUNT(*) AS frequency " +
                "        FROM Produit p " +
                "        JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "        JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "        WHERE cmd.ClientId = " + clientId + " " +
                "        GROUP BY p.Categorie " +
                "    ) " +
                ")";
        List<List<String>> categorieResult = database.executeQuery(categorieQuery);
        if (categorieResult != null && !categorieResult.isEmpty()) {
            allResults.add(categorieResult.get(0)); // Ajout de la première ligne de résultats pour la catégorie
        } else {
            List<String> emptyCategorie = new ArrayList<>();
            emptyCategorie.add("Aucune catégorie trouvée.");
            allResults.add(emptyCategorie);
        }

        // 3. Marque la plus commandée
        String marqueQuery = "SELECT p.Marque " +
                "FROM Produit p " +
                "JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "WHERE cmd.ClientId = " + clientId + " " +
                "GROUP BY p.Marque " +
                "HAVING COUNT(*) = ( " +
                "    SELECT MAX(frequency) " +
                "    FROM ( " +
                "        SELECT COUNT(*) AS frequency " +
                "        FROM Produit p " +
                "        JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "        JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "        WHERE cmd.ClientId = " + clientId + " " +
                "        GROUP BY p.Marque " +
                "    ) " +
                ")";
        List<List<String>> marqueResult = database.executeQuery(marqueQuery);
        if (marqueResult != null && !marqueResult.isEmpty()) {
            allResults.add(marqueResult.get(0)); // Ajout de la première ligne de résultats pour la marque
        } else {
            List<String> emptyMarque = new ArrayList<>();
            emptyMarque.add("Aucune marque trouvée.");
            allResults.add(emptyMarque);
        }

        // 4. Préférence pour les produits bio
        String preferenceBioQuery = "SELECT CASE " +
                "    WHEN COUNT(CASE WHEN p.Bio = 'TRUE' THEN 1 END) > COUNT(CASE WHEN p.Bio = 'FALSE' THEN 1 END) " +
                "        THEN 'Préférence pour les produits bio' " +
                "    WHEN COUNT(CASE WHEN p.Bio = 'TRUE' THEN 1 END) < COUNT(CASE WHEN p.Bio = 'FALSE' THEN 1 END) " +
                "        THEN 'Préférence pour les produits non-bio' " +
                "    ELSE 'Égalité entre les produits bio et non-bio' " +
                "END AS PreferenceBio " +
                "FROM Produit p " +
                "JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "WHERE cmd.ClientId = " + clientId;
        List<List<String>> preferenceBioResult = database.executeQuery(preferenceBioQuery);
        if (preferenceBioResult != null && !preferenceBioResult.isEmpty()) {
            allResults.add(preferenceBioResult.get(0)); // Ajout de la première ligne de résultats pour la préférence bio
        } else {
            List<String> emptyPreferenceBio = new ArrayList<>();
            emptyPreferenceBio.add("Aucune préférence bio trouvée.");
            allResults.add(emptyPreferenceBio);
        }

        // Affichage des résultats
        System.out.print("Nutriscore le plus commandé : ");
        System.out.println(String.join(", ", nutriscoreResult.stream().map(r -> r.get(0)).toArray(String[]::new)));

        System.out.print("Catégorie la plus commandée : ");
        System.out.println(String.join(", ", categorieResult.stream().map(r -> r.get(0)).toArray(String[]::new)));

        System.out.print("Marque la plus commandée : ");
        System.out.println(String.join(", ", marqueResult.stream().map(r -> r.get(0)).toArray(String[]::new)));

        System.out.print("Préférence Bio : ");
        System.out.println(String.join(", ", preferenceBioResult.stream().map(r -> r.get(0)).toArray(String[]::new)));

        // Retourne tous les résultats sous forme de List<List<String>>
        return allResults;
    }
}
