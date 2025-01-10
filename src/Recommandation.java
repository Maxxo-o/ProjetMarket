import java.util.ArrayList;
import java.util.List;

public class Recommandation {
    private static List<Produit> recommandations;
    public static List<Produit> Recommander(Profil prof , Produit prod, int idMagasin){
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        recommandations = new ArrayList<>();

        // Regarder dans la liste des produits préférés du client en premier

        List<Produit> articlesPref =  prof.getArticlesPref();

        //filtrer les produits préférés par catégorie, marque et nutriscore
        recommandations.addAll(filtrerProduits(articlesPref, prod.getCategorie(), prod.getMarque(), prod.getNutriscore(),prod.getPrixUnitaire()));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        //filtrer les produits préférés par catégorie et marque
        recommandations.addAll(filtrerProduits(articlesPref, prod.getCategorie(), prod.getMarque(), null,prod.getPrixUnitaire()));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        //filtrer les produits préférés par catégorie
        recommandations.addAll(filtrerProduits(articlesPref, prod.getCategorie(), null, null,prod.getPrixUnitaire()));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        //filtrer les produits préférés par catégorie
        recommandations.addAll(filtrerProduits(articlesPref, prod.getCategorie(), null, null,0));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        // PASSER PAR LES PRODUITS ASSOCIES AUX TYPES DE PROFILS DU CLIENT
        if (!prof.getNomProfils().isEmpty()) {
            String query = "SELECT p.* \n" +
                    "FROM Produit p, Correspondre c, TypesDeProfil t, Stocker s \n" +
                    "WHERE t.ProfilId = c.ProfilId \n" +
                    "AND s.ProduitId = p.ProduitId \n" +
                    "AND s.QteStock > 0 \n" +
                    "AND s.MagId = " + idMagasin + "\n" +
                    "AND c.ProduitId = p.ProduitId\n" +
                    "AND p.Categorie = '" + prod.getCategorie() + "'\n" +
                    "AND p.ProduitId != " + prod.getIdProduit() + "\n" +
                    "AND t.NomProfil IN (\n";

            for (String nomProfil : prof.getNomProfils()) {
                query += "'" + nomProfil + "',";
            }
            query = query.substring(0, query.length() - 1);
            query += ")";

            List<List<String>> resultProfil = database.executeQuery(query);

            List<Produit> produitsCategorieProfil = new ArrayList<>();

            // Creer les produits pour les proposer au client
            for (List<String> row : resultProfil) {
                double prixUnitaire = 0;
                double prixAuKg = 0;
                if (resultProfil.get(0).get(3) != null) prixUnitaire = Double.parseDouble(resultProfil.get(0).get(3));
                if (resultProfil.get(0).get(2) != null) prixAuKg = Double.parseDouble(resultProfil.get(0).get(2));
                produitsCategorieProfil.add(new Produit(Integer.parseInt(row.get(0)), row.get(1), prixUnitaire, prixAuKg, Double.parseDouble(row.get(4)), row.get(6), row.get(7), row.get(5)));
            }

            //filtrer les produits préférés par catégorie, marque et nutriscore
            recommandations.addAll(filtrerProduits(produitsCategorieProfil, null, prod.getMarque(), prod.getNutriscore(), prod.getPrixUnitaire()));
            if (recommandations.size() >= 3) return recommandations.subList(0, 3);

            //filtrer les produits préférés par catégorie et marque
            recommandations.addAll(filtrerProduits(produitsCategorieProfil, null, prod.getMarque(), null, prod.getPrixUnitaire()));
            if (recommandations.size() >= 3) return recommandations.subList(0, 3);

            //filtrer les produits préférés par catégorie
            recommandations.addAll(filtrerProduits(produitsCategorieProfil, null, null, null, prod.getPrixUnitaire()));
            if (recommandations.size() >= 3) return recommandations.subList(0, 3);

            //filtrer les produits préférés par catégorie
            recommandations.addAll(filtrerProduits(produitsCategorieProfil, null, null, null, 0));
            if (recommandations.size() >= 3) return recommandations.subList(0, 3);
        }

        // Récupérer les produits de la même catégorie
        List<List<String>> result = database.executeQuery("SELECT * FROM Produit p  WHERE Categorie = '" + prod.getCategorie() +"' AND ProduitId != " + prod.getIdProduit());
        List<Produit> produitsCategorie = new ArrayList<>();


        // Creer les produits pour les proposer au client
        for (List<String> row : result){
            double prixUnitaire = 0;
            double prixAuKg = 0;
            if (result.get(0).get(3) != null) prixUnitaire = Double.parseDouble(result.get(0).get(3));
            if (result.get(0).get(2) != null)  prixAuKg = Double.parseDouble(result.get(0).get(2));
            produitsCategorie.add(new Produit(Integer.parseInt(row.get(0)), row.get(1),prixUnitaire, prixAuKg, Double.parseDouble(row.get(4)), row.get(6), row.get(7), row.get(5)));
        }

        //filtrer les produits de la même catégorie par marque et nutriscore
        recommandations.addAll(filtrerProduits(produitsCategorie, null, prod.getMarque(), prod.getNutriscore(),prod.getPrixUnitaire()));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        //filtrer les produits de la même catégorie par marque
        recommandations.addAll(filtrerProduits(produitsCategorie, null, prod.getMarque(), null,prod.getPrixUnitaire()));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        recommandations.addAll(filtrerProduits(produitsCategorie, null, null, null,prod.getPrixUnitaire()));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        recommandations.addAll(filtrerProduits(produitsCategorie, null, null, null,0));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        return recommandations;
    }


    public static List<Produit> filtrerProduits(List<Produit> produits, String categorie, String marque, String nutriscore, double prixProd){
        return produits.stream()
                .filter(produit -> categorie == null || produit.getCategorie().equals(categorie))
                .filter(produit -> marque == null || produit.getMarque().equals(marque))
                .filter(produit -> nutriscore == null || produit.getNutriscore().equals(nutriscore))
                .filter(produit -> prixProd == 0 || produit.getPrixUnitaire() <= prixProd*1.2)
                .filter(produit -> !recommandations.contains(produit))
                .toList();
    }
}
