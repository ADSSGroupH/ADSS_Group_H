package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Path to your SQLite DB file
            String url = "jdbc:sqlite:database/database.db";
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }
}
