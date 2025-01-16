import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recommandation {

    public static List<Produit> Recommander(Produit p, JDBC database){
        List<List<String>> result = database.executeQuery("SELECT DISTINCT\n" +
                "        Produit.ProduitId,\n" +
                "        NomProd,\n" +
                "        PrixAuKg,\n" +
                "        PrixUnitaire,\n" +
                "        Poids,\n" +
                "        cs.NomCat AS \"Sou-Categorie\",\n" +
                "        cp.NomCat AS \"Categorie Principale\",\n" +
                "        Marque,\n" +
                "        Nutriscore,\n" +
                "        bio\n" +
                "    FROM Produit\n" +
                "    JOIN Categorie cs ON Produit.CategorieId = cs.CategorieId\n" +
                "    JOIN Etre ON cs.CategorieId = Etre.CategorieId_SousCategorie\n" +
                "    JOIN Categorie cp ON Etre.CategorieId_Principale = cp.CategorieId\n" +
                "    JOIN Recommander r ON Produit.ProduitId = r.ProduitId\n" +
                "    WHERE r.ProduitId_est_lié = " + p.getIdProduit()
        );
        return getProduitsRecommandes(result);
    }
    public static List<Produit> RecommanderPeriode(int periodeId, JDBC database){
        List<List<String>> result = database.executeQuery("SELECT DISTINCT\n" +
                "        Produit.ProduitId,\n" +
                "        NomProd,\n" +
                "        PrixAuKg,\n" +
                "        PrixUnitaire,\n" +
                "        Poids,\n" +
                "        cs.NomCat AS \"Sou-Categorie\",\n" +
                "        cp.NomCat AS \"Categorie Principale\",\n" +
                "        Marque,\n" +
                "        Nutriscore,\n" +
                "        bio\n" +
                "    FROM Produit\n" +
                "    JOIN Categorie cs ON Produit.CategorieId = cs.CategorieId\n" +
                "    JOIN Etre ON cs.CategorieId = Etre.CategorieId_SousCategorie\n" +
                "    JOIN Categorie cp ON Etre.CategorieId_Principale = cp.CategorieId\n" +
                "    JOIN Approprier a ON Produit.ProduitId = a.ProduitId\n" +
                "    JOIN Periode pe ON a.PeriodeId = pe.PeriodeId\n" +
                "    WHERE pe.periodeId = " + periodeId
        );

        return getProduitsRecommandes(result);
    }


    private static List<Produit> getProduitsRecommandes(List<List<String>> result){
        List<Produit> produits = new ArrayList<>();
        for (List<String> row : result){
            produits.add(new Produit(row));
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
