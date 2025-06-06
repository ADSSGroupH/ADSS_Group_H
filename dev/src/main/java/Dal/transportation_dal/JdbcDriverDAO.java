package Dal.Transportation;

import DTO.LicenseType;
import DTO.driverDTO;
import Dal.transportation_dal.DriverDAO;
import database.Database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcDriverDAO implements DriverDAO {

    @Override
    public Optional<driverDTO> findById(String id) throws SQLException {
        String sql = "SELECT * FROM drivers WHERE employee_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDriverDTO(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<driverDTO> findAll() throws SQLException {
        List<driverDTO> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers ORDER BY employee_id";

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    drivers.add(mapResultSetToDriverDTO(rs));
                }
            }
        }

        return drivers;
    }

    @Override
    public void save(driverDTO driver) throws SQLException {
        String sql = """
            INSERT INTO drivers (employee_id, employee_name, licenseType)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, driver.getId());
            ps.setString(2, driver.getName());
            ps.setString(3, driver.getLicenseType().name());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(driverDTO driver) throws SQLException {
        String sql = """
            UPDATE drivers
            SET employee_name = ?, licenseType = ?
            WHERE employee_id = ?
        """;

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, driver.getName());
            ps.setString(2, driver.getLicenseType().name());
            ps.setString(3, driver.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM drivers WHERE employee_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean exists(String id) throws SQLException {
        String sql = "SELECT 1 FROM drivers WHERE employee_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public String getLicenseByDriverId(String id) throws SQLException {
        String sql = "SELECT licenseType FROM drivers WHERE employee_id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("licenseType");
                } else {
                    throw new SQLException("Driver not found with id: " + id);
                }
            }
        }
    }

    private driverDTO mapResultSetToDriverDTO(ResultSet rs) throws SQLException {
        String id = rs.getString("employee_id");
        String name = rs.getString("employee_name");
        LicenseType licenseType = LicenseType.valueOf(rs.getString("licenseType"));
        driverDTO driver = new driverDTO(licenseType);
        driver.setId(id);
        driver.setName(name);
        return driver;

    }






}
