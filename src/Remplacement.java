import java.util.ArrayList;
import java.util.List;

public class Remplacement {
    private static List<Produit> produitsRemplacement;
    public static List<Produit> Recommander(Profil prof , Produit prod, int idMagasin, JDBC database){
        produitsRemplacement = new ArrayList<>();

        // Regarder dans la liste des produits préférés du client en premier

        List<Produit> articlesPref =  prof.getArticlesPref();

        //filtrer les produits préférés par catégorie, marque et nutriscore
        produitsRemplacement.addAll(filtrerProduits(articlesPref, prod.getCategorie(), prod.getMarque(), prod.getNutriscore(),prod.getPrixUnitaire()));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        //filtrer les produits préférés par catégorie et marque
        produitsRemplacement.addAll(filtrerProduits(articlesPref, prod.getCategorie(), prod.getMarque(), null,prod.getPrixUnitaire()));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        //filtrer les produits préférés par catégorie
        produitsRemplacement.addAll(filtrerProduits(articlesPref, prod.getCategorie(), null, null,prod.getPrixUnitaire()));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        //filtrer les produits préférés par catégorie
        produitsRemplacement.addAll(filtrerProduits(articlesPref, prod.getCategorie(), null, null,0));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

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
                produitsCategorieProfil.add(new Produit(Integer.parseInt(row.get(0)), row.get(1), prixUnitaire, prixAuKg, Double.parseDouble(row.get(4)), row.get(6), row.get(7), row.get(5),Boolean.parseBoolean(row.get(8))));
            }

            //filtrer les produits préférés par catégorie, marque et nutriscore
            produitsRemplacement.addAll(filtrerProduits(produitsCategorieProfil, null, prod.getMarque(), prod.getNutriscore(), prod.getPrixUnitaire()));
            if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

            //filtrer les produits préférés par catégorie et marque
            produitsRemplacement.addAll(filtrerProduits(produitsCategorieProfil, null, prod.getMarque(), null, prod.getPrixUnitaire()));
            if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

            //filtrer les produits préférés par catégorie
            produitsRemplacement.addAll(filtrerProduits(produitsCategorieProfil, null, null, null, prod.getPrixUnitaire()));
            if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

            //filtrer les produits préférés par catégorie
            produitsRemplacement.addAll(filtrerProduits(produitsCategorieProfil, null, null, null, 0));
            if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);
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
            produitsCategorie.add(new Produit(Integer.parseInt(row.get(0)), row.get(1),prixUnitaire, prixAuKg, Double.parseDouble(row.get(4)), row.get(6), row.get(7), row.get(5), Boolean.parseBoolean(row.get(8))));
        }

        //filtrer les produits de la même catégorie par marque et nutriscore
        produitsRemplacement.addAll(filtrerProduits(produitsCategorie, null, prod.getMarque(), prod.getNutriscore(),prod.getPrixUnitaire()));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        //filtrer les produits de la même catégorie par marque
        produitsRemplacement.addAll(filtrerProduits(produitsCategorie, null, prod.getMarque(), null,prod.getPrixUnitaire()));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        produitsRemplacement.addAll(filtrerProduits(produitsCategorie, null, null, null,prod.getPrixUnitaire()));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        produitsRemplacement.addAll(filtrerProduits(produitsCategorie, null, null, null,0));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        return produitsRemplacement;
    }


    // TODO : Faire par rapport aux habitudes de consommation du client
    // TODO : Rajouter si le produit doit etre bio
    public static List<Produit> filtrerProduits(List<Produit> produits, String categorie, String marque, String nutriscore, double prixProd){
        return produits.stream()
                .filter(produit -> categorie == null || produit.getCategorie().equals(categorie))
                .filter(produit -> marque == null || produit.getMarque().equals(marque))
                .filter(produit -> nutriscore == null || produit.getNutriscore().equals(nutriscore))
                .filter(produit -> prixProd == 0 || produit.getPrixUnitaire() <= prixProd*1.2)
                .filter(produit -> !produitsRemplacement.contains(produit))
                .toList();
    }
}
