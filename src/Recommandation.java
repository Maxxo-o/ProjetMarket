import java.util.ArrayList;
import java.util.List;

public class Recommandation {
    public static List<Produit> Recommander(Profil prof , Produit prod){
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        List<Produit> recommandations = new ArrayList<>();

        // Regarder dans la liste des produits préférés du client en premier

        List<Produit> articlesPref =  prof.getArticlesPref();

        //filtrer les produits préférés par catégorie, marque et nutriscore
        recommandations.addAll(filtrerProduits(articlesPref, prod.getCategorie(), prod.getMarque(), prod.getNutriscore()));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        //filtrer les produits préférés par catégorie et marque
        recommandations.addAll(filtrerProduits(articlesPref, prod.getCategorie(), prod.getMarque(), null));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        //filtrer les produits préférés par catégorie
        recommandations.addAll(filtrerProduits(articlesPref, prod.getCategorie(), null, null));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        // Récupérer les produits de la même catégorie
        List<List<String>> result = database.executeQuery("SELECT * FROM Produit WHERE Categorie = '" + prod.getCategorie() +"' AND ProduitId != " + prod.getIdProduit());
        List<Produit> produitsCategorie = new ArrayList<>();

        for (List<String> row : result){
            produitsCategorie.add(new Produit(Integer.parseInt(row.get(0))));
        }

        //filtrer les produits de la même catégorie par marque et nutriscore
        recommandations.addAll(filtrerProduits(produitsCategorie, null, prod.getMarque(), prod.getNutriscore()));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        //filtrer les produits de la même catégorie par marque
        recommandations.addAll(filtrerProduits(produitsCategorie, null, prod.getMarque(), null));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        recommandations.addAll(filtrerProduits(produitsCategorie, null, null, null));
        if (recommandations.size() >= 3) return recommandations.subList(0, 3);

        return recommandations;
    }


    public static List<Produit> filtrerProduits(List<Produit> produits, String categorie, String marque, String nutriscore){
        return produits.stream()
                .filter(produit -> categorie == null || produit.getCategorie().equals(categorie))
                .filter(produit -> marque == null || produit.getMarque().equals(marque))
                .filter(produit -> nutriscore == null || produit.getNutriscore().equals(nutriscore))
                .toList();
    }
}
