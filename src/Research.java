import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Research {
    // US0.2 Je veux rechercher un produit par mot-clé.
    public static List<List<String>> searchByKeyword(JDBC database, String keyword) {
        List<List<String>> queryResult = database.executeQuery(
                """
                        SELECT Produit.ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, NomCat, Marque, SUM(QteCom) AS "Quantité vendu"
                        FROM Produit
                        LEFT JOIN Composer ON Produit.ProduitId = Composer.ProduitId
                        JOIN Associer ON Produit.ProduitId = Associer.ProduitId
                        JOIN Categorie ON Associer.CategorieId = Categorie.CategorieId
                        GROUP BY Produit.ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, NomCat, Marque
                        """);

        List<List<String>> result = queryResult
                .stream()
                .filter(e -> {
                    return e.get(1).toLowerCase().contains(keyword.toLowerCase())
                            || e.get(6).toLowerCase().contains(keyword.toLowerCase())
                            || e.get(7).toLowerCase().contains(keyword.toLowerCase());
                })
                .collect(Collectors.toList());

        return result;
    }

    // US0.3 Je veux consulter la liste des produits par catégorie.
    public static List<List<String>> listCategory(JDBC database, String keyword) {
        List<List<String>> queryResult = database.executeQuery(
                """
                        SELECT Produit.ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, NomCat, Marque, SUM(QteCom) AS "Quantité vendu"
                        FROM Produit
                        LEFT JOIN Composer ON Produit.ProduitId = Composer.ProduitId
                        JOIN Associer ON Produit.ProduitId = Associer.ProduitId
                        JOIN Categorie ON Associer.CategorieId = Categorie.CategorieId
                        GROUP BY Produit.ProduitId, NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, NomCat, Marque
                        """);

        List<List<String>> result = queryResult
                .stream()
                .filter(e -> e.get(6).toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        return result;
    }

    // US0.4 Je veux trier une liste de produits.
    public static List<List<String>> orderList(List<List<String>> list, String condition, Boolean croissant) {
        List<String> conditions = Arrays.asList("NomProd", "PrixAuKg", "PrixUnitaire",
                "Poids", "Nutriscore", "NomCat", "Marque");
        int indexCondition;

        if (list != null) {
            if (conditions.contains(condition)) {
                indexCondition = conditions.indexOf(condition) + 1;
            } else {
                indexCondition = list.get(0).size() - 1;
            }
            int direction = croissant ? 1 : -1;
            Collections.sort(list, (e1, e2) -> {
                if (e1.get(indexCondition) == null || e2.get(indexCondition) == null) {
                    return 0;
                } else {
                    if (indexCondition >= 2 && indexCondition <= 4 || indexCondition == list.get(0).size() - 1) {
                        return direction * Double.compare(Double.parseDouble(e1.get(indexCondition)),
                                Double.parseDouble(e2.get(indexCondition)));
                    }
                    return direction * e1.get(indexCondition).compareTo(e2.get(indexCondition));
                }
            });
        } else {
            System.err.println("La list est null");
        }
        return list;
    }
}
