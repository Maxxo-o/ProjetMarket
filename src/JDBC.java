import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import oracle.jdbc.driver.parser.util.Array;

public class JDBC {
    private String username;
    private String password;

    public JDBC(String username, String password) {
        this.username = username;
        this.password = password;
        connect();
    }

    public void connect() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", username, password)) {
            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute(String command) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", username, password)) {
            Statement statement = connection.createStatement();
            boolean hasResultSet = statement.execute(command);

            if (hasResultSet) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1));
                }
                System.out.println();
            } else {
                System.out.println("Command executed successfully.");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<String>> executeQuery(String command, int width) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", username, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                ArrayList<String> line = new ArrayList<>();
                for (int i = 1; i <= width; i++) {
                    line.add(resultSet.getString(i));
                }
                result.add(line);
            }
            return result;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeUpdate(String command) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", username, password)) {
            Statement statement = connection.createStatement();
            int line = statement.executeUpdate(command);
            System.out.println(String.format("%s lines affected", line));
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

    }
}
