import java.util.List;

public class ProdFreq {
    // US2.1
    public static List<List<String>> getFrequentOrder(int clientId) {
        // Connexion à la base de données via JDBC
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());

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
