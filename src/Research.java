import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Research {
    // US0.2 Je veux rechercher un produit par mot-clé.
    public static List<List<String>> searchByKeyword(String keyword) {
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        List<List<String>> queryResult = database.executeQuery("SELECT * FROM Produit");

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
    public static List<List<String>> listCategory(String keyword) {
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        List<List<String>> queryResult = database.executeQuery("SELECT * FROM Produit");

        List<List<String>> result = queryResult
                .stream()
                .filter(e -> e.get(6).toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        return result;
    }

    // US0.4 Je veux trier une liste de produits.
    public static List<List<String>> orderList(List<List<String>> list, String condition, Boolean croissant) {
        List<String> conditionsSimples = Arrays.asList("NomProd", "PrixAuKg", "PrixUnitaire", "Poids", "Nutriscore",
                "Categorie", "Marque");
        if (conditionsSimples.contains(condition)) {
            int indexCondition = conditionsSimples.indexOf(condition) + 1;
            int direction = croissant ? 1 : -1;
            Collections.sort(list, (e1, e2) -> {
                if (e1.get(indexCondition) == null || e2.get(indexCondition) == null) {
                    return 0;
                } else {
                    if (indexCondition > 1 && indexCondition < 5) {
                        return direction * Double.compare(Double.parseDouble(e1.get(indexCondition)),
                                Double.parseDouble(e2.get(indexCondition)));
                    }
                    return direction * e1.get(indexCondition).compareTo(e2.get(indexCondition));
                }
            });
        }
        return list;
    }
}
