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
        String categorieQuery = "SELECT cat.NomCat " +
                "FROM Produit p " +
                "JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "JOIN Associer assoc ON p.ProduitId = assoc.ProduitId " +
                "JOIN Categorie cat ON assoc.CategorieId = cat.CategorieId " +
                "WHERE cmd.ClientId = " + clientId + " " +
                "GROUP BY cat.NomCat " +
                "HAVING COUNT(*) = ( " +
                "    SELECT MAX(frequency) " +
                "    FROM ( " +
                "        SELECT COUNT(*) AS frequency " +
                "        FROM Produit p " +
                "        JOIN Composer c ON p.ProduitId = c.ProduitId " +
                "        JOIN Commande cmd ON c.CommandeId = cmd.CommandeId " +
                "        JOIN Associer assoc ON p.ProduitId = assoc.ProduitId " +
                "        WHERE cmd.ClientId = " + clientId + " " +
                "        GROUP BY assoc.CategorieId " +
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
        if (nutriscoreResult != null && !nutriscoreResult.isEmpty()) {
            System.out.println(String.join(", ", nutriscoreResult.stream().map(r -> r.get(0)).toArray(String[]::new)));
        } else {
            System.out.println("Aucun Nutriscore trouvé.");
        }

        System.out.print("Catégorie la plus commandée : ");
        if (categorieResult != null && !categorieResult.isEmpty()) {
            System.out.println(String.join(", ", categorieResult.stream().map(r -> r.get(0)).toArray(String[]::new)));
        } else {
            System.out.println("Aucune catégorie trouvée.");
        }

        System.out.print("Marque la plus commandée : ");
        if (marqueResult != null && !marqueResult.isEmpty()) {
            System.out.println(String.join(", ", marqueResult.stream().map(r -> r.get(0)).toArray(String[]::new)));
        } else {
            System.out.println("Aucune marque trouvée.");
        }

        System.out.print("Préférence Bio : ");
        if (preferenceBioResult != null && !preferenceBioResult.isEmpty()) {
            System.out.println(String.join(", ", preferenceBioResult.stream().map(r -> r.get(0)).toArray(String[]::new)));
        } else {
            System.out.println("Aucune préférence bio trouvée.");
        }

        // Retourne tous les résultats sous forme de List<List<String>>
        return allResults;
    }



    public static void updatePreferer(int clientId, JDBC database) {
        // Requête pour obtenir les 3 produits les plus commandés pour ce client
        String selectQuery =
                "SELECT p.ProduitId " +
                        "FROM Produit p, Composer c, Commande cmd " +
                        "WHERE cmd.ClientId = " + clientId + " " +
                        "AND cmd.CommandeId = c.CommandeId " +
                        "AND c.ProduitId = p.ProduitId " +
                        "AND cmd.HeureDebut >= ADD_MONTHS(SYSDATE, -5) " +
                        "AND cmd.EtatCom = 'Finalisee' " +
                        "GROUP BY p.ProduitId " +
                        "ORDER BY SUM(c.QteCom) DESC " +
                        "FETCH FIRST 5 ROWS ONLY";

        List<List<String>> topProducts = database.executeQuery(selectQuery);

        // Vérification si des produits ont été trouvés
        if (topProducts == null || topProducts.isEmpty()) {
            System.out.println("Aucun produit trouvé pour le client " + clientId);
            return;
        }

        // Suppression des anciennes préférences pour ce client
        String deleteQuery =
                "DELETE FROM Preferer WHERE ClientId = " + clientId;
        database.executeUpdate(deleteQuery);
        System.out.println("Anciennes préférences supprimées pour ClientId = " + clientId);

        // Insertion des nouveaux produits préférés
        for (List<String> productRow : topProducts) {
            String produitId = productRow.get(0); // ProduitId
            String insertQuery =
                    "INSERT INTO Preferer (ProduitId, ClientId) " +
                            "VALUES (" + produitId + ", " + clientId + ")";

            database.executeUpdate(insertQuery);
            System.out.println("Ajouté : ProduitId = " + produitId + " pour ClientId = " + clientId);
        }
        System.out.println("Mise à jour des préférences terminée.");

    }

    public static void updateAppartenirType(int clientId, JDBC database) {
        // Requête pour obtenir les profils associés aux top 5 produits les plus commandés par le client
        String selectQuery =
                "SELECT DISTINCT co.ProfilId " +
                        "FROM (SELECT cmp.ProduitId " +
                        "      FROM Commande cmd, Composer cmp " +
                        "      WHERE cmd.ClientId = " + clientId + " " +
                        "      AND cmd.CommandeId = cmp.CommandeId " +
                        "      AND cmd.HeureDebut >= ADD_MONTHS(SYSDATE, -3) " + // Derniers 3 mois
                        "      AND cmd.EtatCom = 'Finalisee' " +
                        "      GROUP BY cmp.ProduitId " +
                        "      HAVING SUM(cmp.QteCom) >= 3 " + // Seuil de 3 produits minimum
                        "      ORDER BY SUM(cmp.QteCom) DESC " +
                        "      FETCH FIRST 5 ROWS ONLY) topProducts, " + // top 5
                        "      Correspondre co " +
                        "WHERE topProducts.ProduitId = co.ProduitId";

        // Exécution de la requête pour sélectionner les profils
        List<List<String>> profileRows = database.executeQuery(selectQuery);

        // Vérification si des profils ont été trouvés
        if (profileRows == null || profileRows.isEmpty()) {
            System.out.println("Aucun profil trouvé pour le client " + clientId);
            return;
        }

        // Suppression des anciennes associations de profils pour ce client
        String deleteQuery =
                "DELETE FROM Appartenir_Type WHERE ClientId = " + clientId;
        database.executeUpdate(deleteQuery);
        System.out.println("Anciennes associations de profils supprimées pour ClientId = " + clientId);

        // Insertion des nouvelles associations de profils
        for (List<String> profileRow : profileRows) {
            String profilId = profileRow.get(0); // ProfilId
            String insertQuery =
                    "INSERT INTO Appartenir_Type (ClientId, ProfilId) " +
                            "VALUES (" + clientId + ", " + profilId + ")";

            database.executeUpdate(insertQuery);
            System.out.println("Ajouté : ProfilId = " + profilId + " pour ClientId = " + clientId);
        }

        System.out.println("Mise à jour des associations de profils terminée.");
    }
}
