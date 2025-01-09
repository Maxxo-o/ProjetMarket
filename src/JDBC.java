import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JDBC {
    private String url;
    private String username;
    private String password;

    public JDBC(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        connect();
    }

    private void connect() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute(String command) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            boolean hasResultSet = statement.execute(command);

            if (hasResultSet) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> executeQuery(String command) {
        List<List<String>> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            while (resultSet.next()) {
                List<String> line = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    line.add(resultSet.getString(i));
                }
                result.add(line);
            }

            return result;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<List<String>> executeQueryMetaData(String command) {
        List<List<String>> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            List<String> columeName = new ArrayList<>();
            List<String> columeType = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columeName.add(resultSetMetaData.getColumnName(i));
                columeType.add(resultSetMetaData.getColumnTypeName(i));
            }
            result.add(columeName);
            result.add(columeType);

            return result;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeUpdate(String command) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            int line = statement.executeUpdate(command);
            System.out.println(String.format("%s lines affected", line));
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeScript(String path) {
        try (Scanner scanner = new Scanner(
                new FileInputStream(path))) {
            StringBuilder command = new StringBuilder();
            while (scanner.hasNextLine()) {
                String ch = scanner.nextLine().trim();
                if (ch.isEmpty() || ch.startsWith("--")) {
                    continue;
                }
                command.append(ch);
                if (ch.endsWith(";")) {
                    String sql = command.toString().trim();
                    sql = sql.substring(0, sql.length() - 1);
                    if (sql.startsWith("INSERT")) {
                        System.out.println(sql);
                        executeUpdate(sql);
                    } else {
                        execute(sql);
                    }
                    command.setLength(0);
                }
            }
        } catch (FileNotFoundException fne) {
            System.err.println(path + " est absent ! ");
        }
    }
}