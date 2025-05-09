package tri.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) { //à mettre dans la racine du package tri
            Properties prop = new Properties();
            prop.load(input);

            URL = prop.getProperty("jdbc.url");
            USER = prop.getProperty("jdbc.user");
            PASSWORD = prop.getProperty("jdbc.password");

        } catch (Exception e) {
            System.err.println("⚠️ Erreur de lecture du fichier config.properties : " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

