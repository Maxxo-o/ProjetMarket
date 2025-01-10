import java.util.ArrayList;
import java.util.List;

public class ClientPreferences {

    // Méthode pour récupérer tous les résultats pour un client donné
    public static List<String> getClientPreferences(int clientId) {
        // Connexion à la base de données via JDBC
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());

        // Initialisation de la liste pour stocker tous les résultats sous forme de String
        List<String> allResults = new ArrayList<>();

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
            allResults.addAll(flattenList(nutriscoreResult));
        } else {
            allResults.add("Aucun Nutriscore trouvé.");
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
            allResults.addAll(flattenList(categorieResult));
        } else {
            allResults.add("Aucune catégorie trouvée.");
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
            allResults.addAll(flattenList(marqueResult));
        } else {
            allResults.add("Aucune marque trouvée.");
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
            allResults.addAll(flattenList(preferenceBioResult));
        } else {
            allResults.add("Aucune préférence bio trouvée.");
        }

        // Retourne tous les résultats sous forme de liste de chaînes
        return allResults;
    }

    // Méthode pour aplatir une List<List<String>> en une List<String>
    private static List<String> flattenList(List<List<String>> inputList) {
        List<String> flatList = new ArrayList<>();
        for (List<String> subList : inputList) {
            flatList.addAll(subList);
        }
        return flatList;
    }
}


/* affichage

        // Affichage des résultats
        if (clientPreferences != null && !clientPreferences.isEmpty()) {
            // Nutriscore
            System.out.print("Nutriscore le plus commandé : ");
            System.out.println(String.join(", ", clientPreferences.subList(0, 1)));

            // Catégorie
            System.out.print("Catégorie la plus commandée : ");
            System.out.println(String.join(", ", clientPreferences.subList(1, 2)));

            // Marque
            System.out.print("Marque la plus commandée : ");
            System.out.println(String.join(", ", clientPreferences.subList(2, 3)));

            // Préférence Bio
            System.out.print("Préférence Bio : ");
            System.out.println(String.join(", ", clientPreferences.subList(3, 4)));
        } else {
            System.out.println("Aucun résultat trouvé pour le client " + clientId);
        }

 */