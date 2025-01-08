import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Research {
    public static void searchByKeyword(String keyword) {
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        ArrayList<ArrayList<String>> queryResult = database.executeQuery("SELECT * FROM Produit", 8);

        List<ArrayList<String>> result = queryResult
                .stream()
                .filter(e -> {
                    return e.get(1).toLowerCase().contains(keyword.toLowerCase())
                            || e.get(6).toLowerCase().contains(keyword.toLowerCase())
                            || e.get(7).toLowerCase().contains(keyword.toLowerCase());
                })
                .collect(Collectors.toList());

        result.forEach(System.out::println);
        System.out.println();
    }

    public static void listCategory(String keyword) {
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        ArrayList<ArrayList<String>> queryResult = database.executeQuery("SELECT * FROM Produit", 8);

        List<ArrayList<String>> result = queryResult
                .stream()
                .filter(e -> e.get(6).toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        result.forEach(System.out::println);
        System.out.println();
    }
}
