import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recommandation {

    public static List<Produit> Recommander(Produit p, JDBC database){
        List<List<String>> result = database.executeQuery("SELECT p.* FROM RECOMMANDER r, Produit p WHERE r.ProduitId = p.ProduitID AND r.ProduitId_est_lié = " + p.getIdProduit());
        return getProduitsRecommandes(result);
    }
    public static List<Produit> RecommanderPeriode(int periodeId, JDBC database){
        List<List<String>> result = database.executeQuery(" SELECT * FROM RECOMMANDER_Periode WHERE periodeId = " + periodeId);
        return getProduitsRecommandes(result);
    }


    private static List<Produit> getProduitsRecommandes(List<List<String>> result){
        List<Produit> produits = new ArrayList<>();
        for (List<String> row : result){
            double prixUnitaire = 0;
            double prixAuKg = 0;
            double poids = 0;

            if (row.get(2) != null) prixUnitaire = Double.parseDouble(row.get(2));
            if (row.get(3) != null)  prixAuKg = Double.parseDouble(row.get(3));
            if (row.get(4) != null) poids = Double.parseDouble(row.get(4));

            produits.add(new Produit(Integer.parseInt(row.get(0)), row.get(1),prixUnitaire, prixAuKg, poids, row.get(6), row.get(7), row.get(5), Boolean.parseBoolean(row.get(8))));
        }

        return produits;
    }

    public static void CreerRecommandation(Panier p, JDBC database){
        //Open connection

        HashMap<Produit, Integer> produits = p.getProduits();
        for (Produit produit : produits.keySet()){

             List<List<String>> resultCount = database.executeQuery("SELECT COUNT(*) " +
                    "FROM Composer " +
                    "WHERE ProduitID = " + produit.getIdProduit());

            int nbProd = Integer.parseInt(resultCount.get(0).get(0));

            if(nbProd>=30){ // Si le produit a été acheté plus de 30 fois
                List<List<String>> result1 = database.executeQuery("SELECT ProduitID, NomProd, NombreApparitions\n" +
                        "FROM (\n" +
                        "    SELECT p.ProduitID, p.NomProd, COUNT(*) AS NombreApparitions,\n" +
                        "           ROW_NUMBER() OVER (ORDER BY COUNT(*) DESC) AS rang\n" +
                        "    FROM Produit p,Composer comp\n" +
                        "    WHERE p.ProduitID = comp.ProduitID\n" +
                        "    AND p.ProduitId <>" + produit.getIdProduit() +
                        "    AND comp.CommandeId IN (\n" +
                        "        SELECT c.CommandeId\n" +
                        "        FROM Composer c\n" +
                        "        WHERE c.ProduitID = " + produit.getIdProduit() +
                        "    )\n" +
                        "    GROUP BY p.ProduitID, p.NomProd\n" +
                        ")\n" +
                        "WHERE rang <= 3");
                database.executeUpdate("DELETE FROM RECOMMANDER WHERE ProduitId_est_lié = " + produit.getIdProduit() + " AND ProduitId_est_lié <> ProduitId");

                for (List<String> row : result1) {
                    if (Integer.parseInt(row.get(2)) >= nbProd / 2) { // Si le produit a été acheté plus de la moitié des fois
                        database.executeUpdate("INSERT INTO RECOMMANDER (ProduitId_est_lié, ProduitId) VALUES (" + produit.getIdProduit() + ", " + row.get(0) + ")");
                     }
                }
            }
        }


        //Close connection
    }
}
