package tri.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/tri_selectif";
    private static final String USER = "root";
    private static final String PASSWORD = "lYhQ23,Z"; // TODO : Changer pour cacher le mdp

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
