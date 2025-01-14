import java.util.ArrayList;
import java.util.List;

public class ClientPreferences {

    // Méthode pour récupérer tous les résultats pour un client donné
    public static List<List<String>> getClientPreferences(int clientId, JDBC database) {

        // Initialisation de la liste pour stocker tous les résultats sous forme de List<List<String>>
        List<List<String>> allResults = new ArrayList<>();

        // 1. Nutriscore le plus commandé
        String nutriscoreQuery = "SELECT CASE " +
                "    WHEN p.Nutriscore IS NULL THEN 'Produit sans Nutriscore' " +
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

    public static List<String> detectNewHabits(int clientId, JDBC database) {
        List<String> newHabits = new ArrayList<>();

        // Commandes récentes (30 derniers jours)
        String recentQuery = "SELECT DISTINCT p.Categorie, p.Marque, p.ProduitId " +
                "FROM Produit p " +
                "JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "WHERE cmd.ClientId = " + clientId + " AND cmd.DateCommande > SYSDATE - 30";

        List<List<String>> recentResults = database.executeQuery(recentQuery);

        // Commandes anciennes (avant 30 jours)
        String oldQuery = "SELECT DISTINCT p.Categorie, p.Marque, p.ProduitId " +
                "FROM Produit p " +
                "JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "WHERE cmd.ClientId = " + clientId + " AND cmd.DateCommande <= SYSDATE - 30";

        List<List<String>> oldResults = database.executeQuery(oldQuery);

        // Identifier les nouvelles catégories
        List<String> oldCategories = new ArrayList<>();
        for (List<String> oldRow : oldResults) {
            oldCategories.add(oldRow.get(0)); // Catégorie dans l'ancienne liste
        }

        for (List<String> recentRow : recentResults) {
            String recentCategory = recentRow.get(0); // Catégorie dans la liste récente
            if (!oldCategories.contains(recentCategory)) {
                if (!newHabits.contains("Nouvelle catégorie : " + recentCategory)) {
                    newHabits.add("Nouvelle catégorie : " + recentCategory);
                }
            }
        }

        // Identifier les nouvelles marques
        List<String> oldBrands = new ArrayList<>();
        for (List<String> oldRow : oldResults) {
            oldBrands.add(oldRow.get(1)); // Marque dans l'ancienne liste
        }

        for (List<String> recentRow : recentResults) {
            String recentBrand = recentRow.get(1); // Marque dans la liste récente
            if (!oldBrands.contains(recentBrand)) {
                if (!newHabits.contains("Nouvelle marque : " + recentBrand)) {
                    newHabits.add("Nouvelle marque : " + recentBrand);
                }
            }
        }

        // Identifier les nouveaux produits et récupérer leurs noms
        List<String> oldProducts = new ArrayList<>();
        for (List<String> oldRow : oldResults) {
            oldProducts.add(oldRow.get(2)); // Produit ID dans l'ancienne liste
        }

        for (List<String> recentRow : recentResults) {
            String recentProductId = recentRow.get(2); // Produit ID dans la liste récente

            // Vérifier si le produit est déjà dans les anciennes habitudes ou déjà inséré
            String checkQuery = "SELECT 1 FROM Preferer WHERE ProduitId = '" + recentProductId + "' AND ClientId = '" + clientId + "'";

            List<List<String>> checkResult = database.executeQuery(checkQuery);

            if (checkResult == null || checkResult.isEmpty()) {
                // Insérer dans la table Preferer
                String insertQuery = "INSERT INTO Preferer (ProduitId, ClientId) VALUES (" +recentProductId+ "," +clientId+ ")";
                database.executeUpdate(insertQuery);

                // Récupérer le nom du produit pour l'ajouter aux nouvelles habitudes
                String productNameQuery = "SELECT NomProd FROM Produit WHERE ProduitId = " + recentProductId;
                List<List<String>> productNameResult = database.executeQuery(productNameQuery);
                if (!productNameResult.isEmpty()) {
                    String productName = productNameResult.get(0).get(0);
                    newHabits.add("Nouveau produit : " + productName);
                }
            } else {
                System.out.println("Produit déjà présent dans la table Preferer : ProduitId = " + recentProductId);
            }
        }



        return newHabits;
    }


}
