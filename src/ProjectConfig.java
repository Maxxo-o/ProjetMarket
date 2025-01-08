import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ProjectConfig {

    public static Properties properties = new Properties();

    public static String getURL() {
        try {
            properties.load(new FileInputStream("./src/config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String port = properties.getProperty("port");
        String SID = properties.getProperty("SID");
        return String.format("jdbc:oracle:thin:@localhost:%s:%s", port, SID);
    }

    public static String getPassword() {
        try {
            properties.load(new FileInputStream("./src/config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("password");
    }

    public static String getUsername() {
        try {
            properties.load(new FileInputStream("./src/config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("username");
    }
}
