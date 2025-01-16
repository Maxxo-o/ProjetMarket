import java.util.ArrayList;
import java.util.List;

public class Remplacement {
    private static List<Produit> produitsRemplacement;
    public static List<Produit> Remplacer(Profil prof , Produit prod, int idMagasin, JDBC database){
        produitsRemplacement = new ArrayList<>();

        // Regarder dans la liste des produits préférés du client en premier

        List<Produit> articlesPref =  prof.getArticlesPref();

        //filtrer les produits préférés par catégorie, marque et nutriscore
        produitsRemplacement.addAll(filtrerProduits(articlesPref, prod.getSouCategorie(), prod.getMarque(), prod.getNutriscore(),prod.getPrixUnitaire()));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        //filtrer les produits préférés par catégorie et marque
        produitsRemplacement.addAll(filtrerProduits(articlesPref, prod.getSouCategorie(), prod.getMarque(), null,prod.getPrixUnitaire()));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        //filtrer les produits préférés par catégorie
        produitsRemplacement.addAll(filtrerProduits(articlesPref, prod.getSouCategorie(), null, null,prod.getPrixUnitaire()));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        //filtrer les produits préférés par catégorie
        produitsRemplacement.addAll(filtrerProduits(articlesPref, prod.getSouCategorie(), null, null,0));
        if (produitsRemplacement.size() >= 3) return produitsRemplacement.subList(0, 3);

        // PASSER PAR LES PRODUITS ASSOCIES AUX TYPES DE PROFILS DU CLIENT
        if (!prof.getNomProfils().isEmpty()) {
            String query = "SELECT p.ProduitId, p.NomProd,p.PrixAuKg, p.PrixUnitaire, Poids, cs.NomCat AS \"Sou-Categorie\", cp.NomCat AS \"Categorie Principale\",p.Marque, p.nutriscore,p.bio\n" +
                    "FROM Produit p, Correspondre c, TypesDeProfil t, Stocker s, Categorie cs, Etre e, Categorie cp \n" +
                    "WHERE t.ProfilId = c.ProfilId \n" +
                    "AND s.ProduitId = p.ProduitId \n" +
                    "AND s.QteStock > 0 \n" +
                    "AND s.MagId = " + idMagasin + "\n" +
                    "AND c.ProduitId = p.ProduitId\n" +
                    "AND p.CategorieId = cs.CategorieId\n" +
                    "AND cs.CategorieId = e.CategorieId_SousCategorie\n" +
                    "AND e.CategorieId_Principale = cp.CategorieId\n" +
                    "AND cs.NomCat = '" + prod.getSouCategorie() + "'\n" +
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
                produitsCategorieProfil.add(new Produit(row));
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
                        "    WHERE Produit.ProduitId != " + prod.getIdProduit() + "\n" +
                        "    AND cs.NomCat ='" + prod.getSouCategorie() +"'");

        List<Produit> produitsCategorie = new ArrayList<>();


        // Creer les produits pour les proposer au client
        for (List<String> row : result){
            produitsCategorie.add(new Produit(row));
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
                .filter(produit -> categorie == null || produit.getSouCategorie().equals(categorie))
                .filter(produit -> marque == null || produit.getMarque().equals(marque))
                .filter(produit -> nutriscore == null || produit.getNutriscore().equals(nutriscore))
                .filter(produit -> prixProd == 0 || produit.getPrixUnitaire() <= prixProd*1.2)
                .filter(produit -> !produitsRemplacement.contains(produit))
                .toList();
    }
}
