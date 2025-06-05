package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public final class Database {

    private static final String DB_URL = "jdbc:sqlite:hr_management.db";
    private static Connection conn;
    private static final Logger log = Logger.getLogger(Database.class.getName());
    private static final boolean RESET_DB_ON_START = false; // שנה ל-true אם אתה רוצה לאפס את הדאטה

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);

            try (Statement st = conn.createStatement()) {

                // Drop existing tables if they exist
                /*st.executeUpdate("DROP TABLE IF EXISTS EmployeeRoles");
                st.executeUpdate("DROP TABLE IF EXISTS weekly_preferences");
                st.executeUpdate("DROP TABLE IF EXISTS shift_assignments");
                st.executeUpdate("DROP TABLE IF EXISTS shifts");
                st.executeUpdate("DROP TABLE IF EXISTS employee_contracts");
                st.executeUpdate("DROP TABLE IF EXISTS employees");
                st.executeUpdate("DROP TABLE IF EXISTS roles");
                st.executeUpdate("DROP TABLE IF EXISTS branches");
                st.executeUpdate("DROP TABLE IF EXISTS shift_swap_requests");
                st.executeUpdate("DROP TABLE IF EXISTS drivers");
                */
                if (RESET_DB_ON_START) {
                    st.executeUpdate("DROP TABLE IF EXISTS items_documents_items");
                    st.executeUpdate("DROP TABLE IF EXISTS ItemsDocuments");
                    st.executeUpdate("DROP TABLE IF EXISTS items");
                    st.executeUpdate("DROP TABLE IF EXISTS transportations");
                    st.executeUpdate("DROP TABLE IF EXISTS trucks");
                    st.executeUpdate("DROP TABLE IF EXISTS sites");
                    st.executeUpdate("DROP TABLE IF EXISTS shipmentAreas");
                    st.executeUpdate("DROP TABLE IF EXISTS drivers");
                    st.executeUpdate("DROP TABLE IF EXISTS shift_swap_requests");
                    st.executeUpdate("DROP TABLE IF EXISTS shift_assignments");
                    st.executeUpdate("DROP TABLE IF EXISTS shifts");
                    st.executeUpdate("DROP TABLE IF EXISTS weekly_preferences");
                    st.executeUpdate("DROP TABLE IF EXISTS employee_contracts");
                    st.executeUpdate("DROP TABLE IF EXISTS employees");
                    st.executeUpdate("DROP TABLE IF EXISTS EmployeeRoles");
                    st.executeUpdate("DROP TABLE IF EXISTS roles");
                    st.executeUpdate("DROP TABLE IF EXISTS branches");
                }


                //HR Schemas

                // Create Employee-Roles junction table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS EmployeeRoles (
                        employee_id VARCHAR(50),
                        role_id VARCHAR(50),
                        PRIMARY KEY (employee_id, role_id),
                        FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
                        FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
                    )
                """);

                //Create Drivers Table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS drivers (
                        employee_id VARCHAR(50),
                        employee_name VARCHAR(50),
                        licenseType VARCHAR(50),
                        PRIMARY KEY (employee_id)
                    )
                """);



                // Create Branches table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS branches (
                        id VARCHAR(50),
                        name VARCHAR(255) NOT NULL,
                        address VARCHAR(500) NOT NULL,
                        employee_ids TEXT,
                        PRIMARY KEY (id)
                    )
                """);

                // Create Roles table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS roles (
                        id VARCHAR(50),
                        name VARCHAR(255) NOT NULL UNIQUE,
                        is_archived boolean,
                        PRIMARY KEY (id)
                    )
                """);

                // Create Employees table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS employees (
                        id VARCHAR(50),
                        name VARCHAR(255) NOT NULL,
                        phone_number VARCHAR(20),
                        branch_id VARCHAR(50),
                        role_ids TEXT,
                        salary INTEGER NOT NULL DEFAULT 0,
                        contract_id VARCHAR(50),
                        bank_details VARCHAR(500),
                        is_archived INTEGER NOT NULL DEFAULT 0,
                        archived_at VARCHAR(255),
                        is_manager INTEGER NOT NULL DEFAULT 0,
                        password VARCHAR(255),
                        PRIMARY KEY (id),
                        FOREIGN KEY (branch_id) REFERENCES branches(id) ON DELETE SET NULL
                    )
                """);

                // Create Employee Contracts table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS employee_contracts (
                        id VARCHAR(100),
                        employee_id VARCHAR(50) NOT NULL,
                        start_date VARCHAR(20) NOT NULL,
                        free_days INTEGER NOT NULL DEFAULT 0,
                        sickness_days INTEGER NOT NULL DEFAULT 0,
                        monthly_work_hours INTEGER NOT NULL,
                        social_contributions VARCHAR(255),
                        advanced_study_fund VARCHAR(255),
                        salary INTEGER NOT NULL,
                        archived_at VARCHAR(255),
                        is_archived INTEGER NOT NULL DEFAULT 0,
                        PRIMARY KEY (id),
                        FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
                    )
                """);

                // Create Shifts table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS shifts (
                        id VARCHAR(50),
                        date VARCHAR(20) NOT NULL,
                        start_time VARCHAR(10) NOT NULL,
                        end_time VARCHAR(10) NOT NULL,
                        type VARCHAR(100) NOT NULL,
                        required_roles_csv TEXT,
                        assignments_csv TEXT,
                        shift_manager_id VARCHAR(50),
                        archived_at VARCHAR(255),
                        is_archived INTEGER NOT NULL DEFAULT 0,
                        PRIMARY KEY (id),
                        FOREIGN KEY (shift_manager_id) REFERENCES employees(id) ON DELETE SET NULL
                    )
                """);

                // Create Shift Assignments table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS shift_assignments (
                        id VARCHAR(150),
                        employee_id VARCHAR(50) NOT NULL,
                        shift_id VARCHAR(50) NOT NULL,
                        role_id VARCHAR(50) NOT NULL,
                        archived_at VARCHAR(255),
                        is_archived INTEGER NOT NULL DEFAULT 0,
                        PRIMARY KEY (id),
                        FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
                        FOREIGN KEY (shift_id) REFERENCES shifts(id) ON DELETE CASCADE,
                        FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
                    )
                """);

                // Create Shift Swap Requests table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS shift_swap_requests (
                        id TEXT PRIMARY KEY,
                        requestor_id TEXT NOT NULL,
                        from_shift_id TEXT NOT NULL,
                        to_shift_id TEXT NOT NULL,
                        status TEXT NOT NULL,
                        date TEXT NOT NULL,
                        archived_at TEXT,
                        is_archived INTEGER DEFAULT 0,
                        FOREIGN KEY (requestor_id) REFERENCES employees(id),
                        FOREIGN KEY (from_shift_id) REFERENCES shifts(id),
                        FOREIGN KEY (to_shift_id) REFERENCES shifts(id)
                    )
                """);

                // Create Weekly Preferences table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS weekly_preferences (
                        employee_id VARCHAR(50),
                        preferred_shift_ids_csv TEXT,
                        week_start_date VARCHAR(20) NOT NULL,
                        created_at VARCHAR(20) NOT NULL,
                        last_modified VARCHAR(20) NOT NULL,
                        status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                        notes TEXT,
                        employee_id_simple VARCHAR(50),
                        PRIMARY KEY (employee_id, week_start_date),
                        FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
                    )
                """);

                //Transportation Schemas
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


                // Create transportations table
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS transportations (
                        id INTEGER PRIMARY KEY,
                        date TEXT NOT NULL,
                        departureTime TEXT NOT NULL,
                        truckPlateNumber TEXT NOT NULL,
                        driverName TEXT NOT NULL,
                        originName TEXT NOT NULL,
                        originShipmentAreaId INTEGER,
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
                                        itemsDocumentId INTEGER NOT NULL,
                                        itemId INTEGER NOT NULL,
                                        name TEXT NOT NULL,
                                        quantity INTEGER NOT NULL,
                                        weight INTEGER NOT NULL,
                                        PRIMARY KEY (itemsDocumentId, itemId),
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


                // Create indexes for better performance
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_employees_branch ON employees(branch_id)");
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_employees_archived ON employees(is_archived)");
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_shifts_date ON shifts(date)");
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_shifts_archived ON shifts(is_archived)");
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_shift_assignments_employee ON shift_assignments(employee_id)");
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_shift_assignments_shift ON shift_assignments(shift_id)");
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_contracts_employee ON employee_contracts(employee_id)");
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_shift_swap_requests ON shift_swap_requests(id)");
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_weekly_preferences_employee ON weekly_preferences(employee_id)");
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_weekly_preferences_week ON weekly_preferences(week_start_date)");

                //insert manager details:
                st.executeUpdate("""
                    INSERT OR IGNORE INTO branches (id, name, address, employee_ids) VALUES
                    ('1', 'example', 'example', '123456789,111,112,113,114,115,116,117,118')
                """);

                // Roles
                st.executeUpdate("""
                    INSERT OR IGNORE INTO roles (id, name, is_archived) VALUES
                    ('0', 'manager', false)
                """);

                // Employees
                st.executeUpdate("""
                    INSERT OR IGNORE INTO employees (id, name, phone_number, branch_id, role_ids, salary, contract_id, bank_details, is_archived, archived_at, is_manager, password) VALUES
                    ('123456789', 'manager', '054-4332473', '1', '0', 2000, '123456789-2025-04-17', 'n', 0, NULL, 1, '123')
                """);

                // Employee Contracts
                st.executeUpdate("""
                    INSERT OR IGNORE INTO employee_contracts (id, employee_id, start_date, free_days, sickness_days, monthly_work_hours, social_contributions, advanced_study_fund, salary, archived_at, is_archived) VALUES
                    ('123456789-2025-04-17', '123456789', '2025-04-17', 10, 10, 20, 'example', 'example', 2000, '17.04.2025', 1)
                """);



                // Employee Roles
                st.executeUpdate("""
                    INSERT OR IGNORE INTO EmployeeRoles (employee_id, role_id) VALUES
                    ('123456789', '0')
                """);

                // Drivers
                st.executeUpdate("""
                    INSERT OR IGNORE INTO drivers (employee_id, employee_name, licenseType) VALUES
                    ('114', 'Dana',"A"),
                    ('115', 'Eli',"B"),
                    ('116', 'Snir',"C")
                """);




            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Database initialization failed: " + e.getMessage());
        }
    }

    // Insert updated sample data
    public static void InsertData(){
        try (Statement st = conn.createStatement()) {

            // Roles
            st.executeUpdate("""
                    INSERT OR IGNORE INTO roles (id, name, is_archived) VALUES
                    ('1', 'shift manager', false),
                    ('2', 'cashier', false),
                    ('3', 'stocker', false),
                    ('4', 'driver', false),
                    ('5', 'butcher', false)
                """);

            // Employees
            st.executeUpdate("""
                    INSERT OR IGNORE INTO employees (id, name, phone_number, branch_id, role_ids, salary, contract_id, bank_details, is_archived, archived_at, is_manager, password) VALUES
                    ('111', 'Hila', '050-0000001', '1', '2', 8000, '111-2025-06-01', '123', 0, NULL, 0, 'pass1'),
                    ('112', 'Yarden', '050-0000002', '1', '2', 7800, '112-2025-06-01', '456', 0, NULL, 0, 'pass2'),
                    ('113', 'Charlie', '050-0000003', '1', '3', 7500, '113-2025-06-01', '789', 0, NULL, 0, 'pass3'),
                    ('114', 'Dana', '050-0000004', '1', '4', 7200, '114-2025-06-01', '101', 0, NULL, 0, 'pass4'),
                    ('115', 'Eli', '050-0000005', '1', '3,4', 7900, '115-2025-06-01', '202', 0, NULL, 0, 'pass5'),
                    ('116', 'Snir', '050-0000006', '1', '2,4', 7600, '116-2025-06-01', '303', 0, NULL, 0, 'pass6'),
                    ('117', 'David', '050-0000007', '1', '1', 9000, '117-2025-06-01', '303', 0, NULL, 0, 'pass7'),
                    ('118', 'Ben', '050-0000008', '1', '5', 7700, '118-2025-06-01', '505', 0, NULL, 0, 'pass8')
                """);

            // Employee Contracts
            st.executeUpdate("""
                    INSERT OR IGNORE INTO employee_contracts (id, employee_id, start_date, free_days, sickness_days, monthly_work_hours, social_contributions, advanced_study_fund, salary, archived_at, is_archived) VALUES
                    ('111-2025-06-01', '111', '2025-06-01', 12, 5, 160, 'Basic', 'Standard', 8000, NULL, 0),
                    ('112-2025-06-01', '112', '2025-06-01', 12, 5, 160, 'Basic', 'Standard', 7800, NULL, 0),
                    ('113-2025-06-01', '113', '2025-06-01', 10, 4, 160, 'Basic', 'None', 7500, NULL, 0),
                    ('114-2025-06-01', '114', '2025-06-01', 10, 3, 160, 'Basic', 'None', 7200, NULL, 0),
                    ('115-2025-06-01', '115', '2025-06-01', 10, 3, 160, 'Standard', 'Standard', 7900, NULL, 0),
                    ('116-2025-06-01', '116', '2025-06-01', 12, 5, 160, 'Basic', 'Standard', 7600, NULL, 0),
                    ('117-2025-06-01', '117', '2025-06-01', 12, 5, 160, 'Basic', 'Standard', 9000, NULL, 0),
                    ('118-2025-06-01', '118', '2025-06-01', 10, 3, 160, 'Standard', 'Standard', 7700, NULL, 0)
                """);

            // Shifts
            st.executeUpdate("""
                    INSERT OR IGNORE INTO shifts (id, date, start_time, end_time, type, required_roles_csv, assignments_csv, shift_manager_id, archived_at, is_archived) VALUES
                    ('morn_0', '2025-06-01', '08:00', '14:00', 'Morning', '2,3,5', '111morn_02,113morn_03,118morn_05,117morn_01', '117', NULL, 0),
                    ('eve_0', '2025-06-01', '14:00', '21:00', 'Evening', '4', '114eve_04,117eve_01', '117', NULL, 0),
                    ('morn_1', '2025-06-02', '08:00', '14:00', 'Morning', '2,3,5', '112morn_12,115morn_13,118morn_15,117morn_11', '117', NULL, 0),
                    ('eve_1', '2025-06-02', '14:00', '21:00', 'Evening', '4', '116eve_14,117eve_11', '117', NULL, 0),
                    ('morn_2', '2025-06-03', '08:00', '14:00', 'Morning', '2,3,5', NULL, '117', NULL, 0),
                    ('eve_2', '2025-06-03', '14:00', '21:00', 'Evening', '4', NULL, '117', NULL, 0)
                """);

            // Shift Assignments
            st.executeUpdate("""
                    INSERT OR IGNORE INTO shift_assignments (id, employee_id, shift_id, role_id, archived_at, is_archived) VALUES
                    ('111morn_02', '111', 'morn_0', '2', NULL, 0),
                    ('113morn_03', '113', 'morn_0', '3', NULL, 0),
                    ('118morn_05', '118', 'morn_0', '5', NULL, 0),
                    ('117morn_01', '117', 'morn_0', '1', NULL, 0),
                    ('114eve_04', '114', 'eve_0', '4', NULL, 0),
                    ('117eve_01', '117', 'eve_0', '1', NULL, 0),
                    ('112morn_12', '112', 'morn_1', '2', NULL, 0),
                    ('115morn_13', '115', 'morn_1', '3', NULL, 0),
                    ('118morn_15', '118', 'morn_1', '5', NULL, 0),
                    ('117morn_11', '117', 'morn_1', '1', NULL, 0),
                    ('116eve_14', '116', 'eve_1', '4', NULL, 0),
                    ('117eve_11', '117', 'eve_1', '1', NULL, 0)
                """);

            // Sample Weekly Preferences
            st.executeUpdate("""
                    INSERT OR IGNORE INTO weekly_preferences (employee_id, preferred_shift_ids_csv, week_start_date, created_at, last_modified, status, notes, employee_id_simple) VALUES
                    ('111', 'morn_0,morn_2,morn_4', '2025-06-01', '2025-06-01', '2025-06-01', 'SUBMITTED', 'Prefer mornings', '111'),
                    ('112', 'morn_1,morn_3,morn_5', '2025-06-01', '2025-06-01', '2025-06-01', 'DRAFT', NULL, '112'),
                    ('114', 'eve_0,eve_2,eve_4', '2025-06-01', '2025-06-01', '2025-06-01', 'APPROVED', 'Evening driver', '114')
                """);

            // Employee Roles
            st.executeUpdate("""
                    INSERT OR IGNORE INTO EmployeeRoles (employee_id, role_id) VALUES
                    ('111', '2'),
                    ('112', '2'),
                    ('113', '3'),
                    ('114', '4'),
                    ('115', '3'),
                    ('115', '4'),
                    ('116', '2'),
                    ('116', '4'),
                    ('117', '1'),
                    ('118', '5')
                """);
            st.executeUpdate("""
                   INSERT OR IGNORE INTO trucks (plateNumber, model, netWeight, maxWeight, licenseType) VALUES
                   ('11111111', 'Audi', 350, 800, 'C'),
                   ('12345678', 'Mercedes', 200, 920, 'A')
                """);
            st.executeUpdate("""
                   INSERT OR IGNORE INTO shipmentAreas (id, name) VALUES
                    (123, 'Negev'),
                    (111, 'Galil'),
                    (222, 'Center')
                """);
            st.executeUpdate("""
                   INSERT OR IGNORE INTO sites (name, address, phoneNumber, contactPersonName, shipmentAreaId) VALUES
                   ('Ikea', 'Beer sheva', '086312589', 'Ido', 111),
                   ('Nike', 'Tel aviv', '081111111', 'Tal', 123),
                   ('Mango', 'Sderot', '032222222', 'Jordi', 222),
                   ('Zara', 'Beer sheva', '031234567', 'Hila', 111)
                """);
            st.executeUpdate("""
                   INSERT OR IGNORE INTO transportations (id, date, departureTime, truckPlateNumber, driverName, originName, originShipmentAreaId, succeeded, accident) VALUES
                   (1234, '2025-06-01', '10:00', '11111111', 'Dana', 'Ikea', 111, 1, 'No accidents reported'),
                   (4444, '2025-06-01', '11:00', '12345678', 'Eli', 'Mango', 222, 0, 'No accidents reported')
                """);
            st.executeUpdate("""
                   INSERT OR IGNORE INTO items (itemsDocumentId, itemId, name, quantity, weight) VALUES
                   (4444, 1, 'milk', 2, 1),
                   (4444, 2, 'bread', 3, 2),
                   (5555, 3, 'coffee', 1, 1),
                   (5555, 4, 'water', 1, 4)
                """);
            st.executeUpdate("""
                   INSERT OR IGNORE INTO ItemsDocuments (id, destinationName, shipmentAreaId, arrivalTime, transportationId) VALUES
                   (4444, 'Ikea', 111, '12:00', 1234),
                   (5555, 'Mango', 222, '13:00', 4444)
                """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Database() {
        // Prevent instantiation
    }

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
                st.executeUpdate("CREATE TABLE IF NOT EXISTS branches (id VARCHAR(50) PRIMARY KEY, name VARCHAR(255), address VARCHAR(500), employee_ids TEXT)");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reinitialize DB", e);
        }
    }
}
