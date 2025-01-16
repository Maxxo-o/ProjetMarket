import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddProduitsCSV {
    public static void readCSV(JDBC database, String path) {
        List<Produit> produits = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                List<String> list = Arrays.asList(line.split(";"));
                produits.add(new Produit(
                        Integer.parseInt(list.get(0)), // ProduitId
                        list.get(1), // NomProd
                        !list.get(2).equals("null") ? Double.parseDouble(list.get(2)) : 0, // PrixUnitaire
                        !list.get(3).equals("null") ? Double.parseDouble(list.get(3)) : 0, // PrixAuKg
                        !list.get(4).equals("null") ? Double.parseDouble(list.get(4)) : 0, // Poids
                        Integer.parseInt(list.get(5)),
                        list.get(6), // Marque
                        list.get(7), // Nutriscore
                        Boolean.parseBoolean(list.get(8)), // Bio
                        database));
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
        StringBuilder sql = new StringBuilder("INSERT ALL\n");
        produits.forEach(e -> sql.append(e.queryPushProductToBd()));
        sql.append("SELECT * FROM DUAL");
        try {
            database.executeUpdate(sql.toString());
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
