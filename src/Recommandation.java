import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recommandation {

    public static List<Produit> Recommander(Produit p, JDBC database){

        List<List<String>> result = database.executeQuery(" SELECT * FROM RECOMMANDER WHERE produitId_Base = " + p.getIdProduit());
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
            if (result.get(0).get(3) != null) prixUnitaire = Double.parseDouble(result.get(0).get(3));
            if (result.get(0).get(2) != null)  prixAuKg = Double.parseDouble(result.get(0).get(2));
            produits.add(new Produit(Integer.parseInt(row.get(0)), row.get(1),prixUnitaire, prixAuKg, Double.parseDouble(row.get(4)), row.get(6), row.get(7), row.get(5), Boolean.parseBoolean(row.get(8))));
        }

        return produits;
    }

    public static void CreerRecommandation(Panier p, JDBC database){
        //Open connection


        HashMap<Produit, Integer> produits = p.getProduits();
        for (Produit produit : produits.keySet()){
            database.executeQuery("SELECT * FROM COMMANDE c");
        }


        //Close connection
    }
}
