import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String username = "C##JAVA";
        String password = "C##JAVA";
        JDBC admin = new JDBC("system", "oracle");
        admin.execute(String.format("ALTER USER %s QUOTA UNLIMITED ON USERS", username));

        JDBC database = new JDBC(username, password);
        database.execute(
                "BEGIN EXECUTE IMMEDIATE 'DROP TABLE dummy_table CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;");
        database.execute(
                "BEGIN EXECUTE IMMEDIATE 'CREATE TABLE dummy_table (id NUMBER, name VARCHAR2(100))'; EXCEPTION WHEN OTHERS THEN NULL; END;");
        database.executeUpdate("INSERT INTO dummy_table (id, name) VALUES (1, 'A')");
        ArrayList<ArrayList<String>> result = database.executeQuery("SELECT * FROM dummy_table", 2);
        result.forEach(e -> {
            e.forEach(e1 -> System.out.printf("%s\t", e1));
        });
    }
}
