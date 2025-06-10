package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL  = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "your_user";
    private static final String PASS = "your_pass";

    public static Connection connect() throws SQLException {
        // טוענים את המחלקה של הדרייבר (JDBC4 אמור לעשות את זה אוטומטית,
        // אבל לפעמים בלי ה-jar זה לא מספיק)
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found!", e);
        }
        // מחזירים Connection
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
