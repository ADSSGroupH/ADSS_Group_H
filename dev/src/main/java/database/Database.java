package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Singleton-style database utility class for managing SQLite connection and schema initialization.
 */
public final class Database {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static Connection conn;
    private static final Logger log = Logger.getLogger(Database.class.getName());


    static {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Establish connection
            conn = DriverManager.getConnection(DB_URL);

            // Initialize database schema
            try (Statement st = conn.createStatement()) {

                // Create shipment areas table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS shipmentAreas (
                        id INTEGER PRIMARY KEY,
                        name TEXT NOT NULL
                    );
                """);

                // Create sites table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS sites (
                        name TEXT PRIMARY KEY,
                        address TEXT NOT NULL,
                        phoneNumber TEXT NOT NULL,
                        contactPersonName TEXT NOT NULL,
                        shipmentAreaId INTEGER NOT NULL,
                        FOREIGN KEY (shipmentAreaId) REFERENCES shipmentAreas(id)
                    );
                """);

                // Create trucks table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS trucks (
                        plateNumber TEXT PRIMARY KEY,
                        model TEXT NOT NULL,
                        netWeight INTEGER NOT NULL,
                        maxWeight INTEGER NOT NULL,
                        licenseType TEXT NOT NULL
                    );
                """);

                // Create drivers table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS drivers (
                        username TEXT PRIMARY KEY,
                        licenseType TEXT NOT NULL
                    );
                """);

                // Create transportations table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS transportations (
                        id INTEGER PRIMARY KEY,
                        date TEXT NOT NULL,
                        departureTime TEXT NOT NULL,
                        arrivalTime TEXT NOT NULL,
                        truckPlateNumber TEXT NOT NULL,
                        driverName TEXT NOT NULL,
                        originName TEXT NOT NULL,
                        originShipmentAreaId, INTEGER,
                        succeeded INTEGER DEFAULT 0,
                        accident TEXT NOT NULL,
                        FOREIGN KEY (truckPlateNumber) REFERENCES trucks(license_plate),
                        FOREIGN KEY (driverName) REFERENCES drivers(username),
                        FOREIGN KEY (originName) REFERENCES sites(name)
                    );
                """);

                // Create items table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS items (
                        itemsDocumentId INTEGER PRIMARY KEY,
                        itemId INTEGER NOT NULL,
                        name TEXT NOT NULL,
                        quantity INTEGER NOT NULL,
                        weight INTEGER NOT NULL,
                        FOREIGN KEY (itemsDocumentId) REFERENCES itemsDocument(id)
                    );
                """);

                // Create items_documents table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS ItemsDocuments (
                        id INTEGER PRIMARY KEY,
                        destinationName TEXT NOT NULL,
                        shipmentAreaId INTEGER NOT NULL,
                        arrivalTime TEXT NOT NULL,
                        transportationId INTEGER NOT NULL,
                        FOREIGN KEY (destinationName) REFERENCES sites(name),
                        FOREIGN KEY (transportationId) REFERENCES transportations(id)
                    );
                """);

                // Create items_documents_items join table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS items_documents_items (
                        document_id INTEGER NOT NULL,
                        item_id INTEGER NOT NULL,
                        FOREIGN KEY (document_id) REFERENCES items_documents(id),
                        FOREIGN KEY (item_id) REFERENCES items(id)
                    );
                """);

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Database initialization failed: " + e.getMessage());
        }
    }

    private Database() {
        // Prevent instantiation
    }

    /**
     * Returns the active SQLite database connection.
     */
    public static Connection getConnection() throws SQLException {
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                log.info("Connection closed");
            }
        } catch (SQLException e) {
            log.severe("Failed to close connection: " + e.getMessage());

        }
    }
        public static void init() {
            try {
                conn = DriverManager.getConnection(DB_URL);
                try (Statement st = conn.createStatement()) {
                    st.executeUpdate("CREATE TABLE IF NOT EXISTS trucks (plateNumber TEXT PRIMARY KEY, model TEXT, netWeight INTEGER, maxWeight INTEGER, licenseType TEXT)");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to reinitialize DB", e);
            }
        }





}
