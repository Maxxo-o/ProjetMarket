import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Research {
    public static List<List<String>> getAllProduits(JDBC database) {
        return database.executeQuery("""
                SELECT DISTINCT
                    Produit.ProduitId,
                    NomProd,
                    PrixUnitaire,
                    PrixAuKg,
                    Poids,
                    cs.NomCat AS "Sou-Categorie",
                    cp.NomCat AS "Categorie Principale",
                    Marque,
                    Nutriscore,
                    bio
                FROM Produit
                JOIN Categorie cs ON Produit.CategorieId = cs.CategorieId
                JOIN Etre ON cs.CategorieId = Etre.CategorieId_SousCategorie
                JOIN Categorie cp ON Etre.CategorieId_Principale = cp.CategorieId
                """);
    }

    // US0.2 Je veux rechercher un produit par mot-clé.
    public static List<Produit> searchByKeyword(JDBC database, String keyword) {
        return getAllProduits(database)
                .stream()
                .filter(e -> {
                    return e.get(1).toLowerCase().contains(keyword.toLowerCase())
                            || e.get(5).toLowerCase().contains(keyword.toLowerCase())
                            || e.get(6).toLowerCase().contains(keyword.toLowerCase())
                            || e.get(7).toLowerCase().contains(keyword.toLowerCase());
                })
                .filter(Objects::nonNull)
                .map(e -> {
                    return new Produit(
                            Integer.parseInt(e.get(0)), // ProduitId
                            e.get(1), // NomProd
                            e.get(2) != null ? Double.parseDouble(e.get(2)) : 0, // PrixUnitaire
                            e.get(3) != null ? Double.parseDouble(e.get(3)) : 0, // PrixAuKg
                            e.get(4) != null ? Double.parseDouble(e.get(4)) : 0, // Poids
                            e.get(5), // Sou-Categorie
                            e.get(6), // Categorie Principale
                            e.get(7), // Marque
                            e.get(8), // Nutriscore
                            Boolean.parseBoolean(e.get(9)) // Bio
                    );
                })
                .collect(Collectors.toList());
    }

    // US0.3 Je veux consulter la liste des produits par catégorie.
    public static List<Produit> listCategory(JDBC database, String keyword) {
        return getAllProduits(database)
                .stream()
                .filter(e -> {
                    return e.get(6).toLowerCase().contains(keyword.toLowerCase())
                            || e.get(7).toLowerCase().contains(keyword.toLowerCase());
                })
                .filter(Objects::nonNull)
                .map(e -> {
                    return new Produit(
                            Integer.parseInt(e.get(0)), // ProduitId
                            e.get(1), // NomProd
                            e.get(2) != null ? Double.parseDouble(e.get(2)) : 0, // PrixUnitaire
                            e.get(3) != null ? Double.parseDouble(e.get(3)) : 0, // PrixAuKg
                            e.get(4) != null ? Double.parseDouble(e.get(4)) : 0, // Poids
                            e.get(5), // Sou-Categorie
                            e.get(6), // Categorie Principale
                            e.get(7), // Marque
                            e.get(8), // Nutriscore
                            Boolean.parseBoolean(e.get(9)) // Bio
                    );
                })
                .collect(Collectors.toList());
    }

    public static <T extends Comparable<T>> int compare(T e1, T e2, Boolean numeric, int direction) {
        if (e1 == null || e2 == null)
            return 0;
        else {
            if (numeric)
                return direction * Double.compare(Double.parseDouble(e1.toString()), Double.parseDouble(e2.toString()));
            else
                return direction * e1.compareTo(e2);
        }
    }

    // US0.4 Je veux trier une liste de produits.
    public static List<Produit> orderList(List<Produit> list, String condition, Boolean croissant) {
        if (list != null) {
            int direction = croissant ? 1 : -1;
            Collections.sort(list, (e1, e2) -> {
                return switch (condition) {
                    case "NomProd" -> compare(e1.getLibelle(), e2.getLibelle(), false, direction);
                    case "PrixAuKg" -> compare(e1.getPrixAuKg(), e2.getPrixAuKg(), true, direction);
                    case "PrixUnitaire" -> compare(e1.getPrixUnitaire(), e2.getPrixUnitaire(), true, direction);
                    case "Poids" -> compare(e1.getPoids(), e2.getPoids(), true, direction);
                    case "Nutriscore" -> compare(e1.getNutriscore(), e2.getNutriscore(), false, direction);
                    case "Marque" -> compare(e1.getMarque(), e2.getMarque(), false, direction);
                    case "Sou-Categorie" -> compare(e1.getSouCategorie(), e2.getSouCategorie(), false, direction);
                    case "Categorie Principale" ->
                        compare(e1.getcategoriePincipale(), e2.getcategoriePincipale(), false, direction);
                    default -> 0;
                };
            });
        } else {
            System.err.println("La list est vide");
        }
        return list;
    }
}
