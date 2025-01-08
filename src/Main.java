import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(""));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String port = properties.getProperty("port");
        String SID = properties.getProperty("SID");
        String url = String.format("jdbc:oracle:thin:@localhost:%s:%s", port, SID);
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        JDBC admin = new JDBC(url, "system", "oracle");
        admin.execute(String.format("ALTER USER %s QUOTA UNLIMITED ON USERS", username));

        JDBC database = new JDBC(url, username, password);
        database.executeScript("");
        
        System.out.println(database.executeQuery("SELECT * FROM Produit", 8));

        /*
         * database.execute(
         * "BEGIN EXECUTE IMMEDIATE 'DROP TABLE dummy_table CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;"
         * );
         * database.execute(
         * "BEGIN EXECUTE IMMEDIATE 'CREATE TABLE dummy_table (id NUMBER, name VARCHAR2(100))'; EXCEPTION WHEN OTHERS THEN NULL; END;"
         * );
         * database.executeUpdate("INSERT INTO dummy_table (id, name) VALUES (1, 'A')");
         * database.executeUpdate("INSERT INTO dummy_table (id, name) VALUES (2, 'B')");
         * ArrayList<ArrayList<String>> result =
         * database.executeQuery("SELECT * FROM dummy_table", 2);
         * System.out.println(result);
         * result.forEach(e -> {
         * e.forEach(e1 -> System.out.printf("%s\t", e1));
         * System.out.println();
         * });
         */

    }
}
