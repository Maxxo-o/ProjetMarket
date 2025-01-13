import java.util.List;

public class ProdFreq {
    // US2.1
    public static List<List<String>> getFrequentOrder(int clientId, JDBC database) {
        // Connexion à la base de données via JDBC

        String query = "SELECT p.NomProd, COUNT(com.ProduitId) AS frequence_commande "
                + "FROM Commande c, Composer com, Produit p "
                + "WHERE c.CommandeId = com.CommandeId "
                + "AND com.ProduitId = p.ProduitId "
                + "AND c.ClientId = " + clientId + " "
                + "GROUP BY p.ProduitId, p.NomProd "
                + "ORDER BY frequence_commande DESC "
                + "FETCH FIRST 5 ROWS ONLY";

        List<List<String>> queryResult = database.executeQuery(query);

        if (queryResult != null && !queryResult.isEmpty()) {
            return queryResult;
        } else {
            System.out.println("Aucun produit trouvé pour le client " + clientId);
            return null;
        }
    }
}

/* affichage

        int clientId = 1;

        // Obtenir les 5 produits les plus fréquemment commandés pour le client
        List<List<String>> top5Products = ProdFreq.getFrequentOrder(clientId);

        // Vérifier et afficher les résultats
        if (top5Products != null && !top5Products.isEmpty()) {
            System.out.println("Top 5 des produits les plus fréquemment commandés pour le client " + clientId + ": ");

            // Affichage des produits et fréquences
            for (List<String> product : top5Products) {
                System.out.println(product.get(0) + " (" + product.get(1) + "), ");
            }
        } else {
            System.out.println("Aucun produit trouvé pour le client " + clientId);
        }

 */