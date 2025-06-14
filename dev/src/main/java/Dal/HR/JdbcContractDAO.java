package Dal.HR;

import DTO.HR.EmployeeContractDTO;
import database.Database;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcContractDAO implements ContractDAO {
    private static final String SELECT_BY_ID = "SELECT * FROM employee_contracts WHERE id = ?";
    private static final String SELECT_BY_EMPLOYEE_ID = "SELECT * FROM employee_contracts WHERE employee_id = ?";
    private static final String SELECT_ALL = "SELECT * FROM employee_contracts";
    private static final String SELECT_ACTIVE = "SELECT * FROM employee_contracts WHERE is_archived = false";
    private static final String SELECT_BY_START_DATE = "SELECT * FROM employee_contracts WHERE start_date = ?";
    private static final String SELECT_BY_DATE_RANGE = "SELECT * FROM employee_contracts WHERE start_date BETWEEN ? AND ?";
    private static final String INSERT = "INSERT INTO employee_contracts (id, employee_id, start_date, free_days, sickness_days, monthly_work_hours, social_contributions, advanced_study_fund, salary, archived_at, is_archived) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE employee_contracts SET employee_id = ?, start_date = ?, free_days = ?, sickness_days = ?, monthly_work_hours = ?, social_contributions = ?, advanced_study_fund = ?, salary = ?, archived_at = ?, is_archived = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM employee_contracts WHERE id = ?";
    private static final String DELETE_BY_EMPLOYEE_ID = "DELETE FROM employee_contracts WHERE employee_id = ?";
    private static final String EXISTS = "SELECT COUNT(*) FROM employee_contracts WHERE id = ?";
    private static final String EXISTS_BY_EMPLOYEE_ID = "SELECT COUNT(*) FROM employee_contracts WHERE employee_id = ?";
    private static final String COUNT = "SELECT COUNT(*) FROM employee_contracts";

    @Override
    public Optional<EmployeeContractDTO> findById(String id) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_BY_ID)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToContractDTO(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<EmployeeContractDTO> findByEmployeeId(String employeeId) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_BY_EMPLOYEE_ID)) {

            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToContractDTO(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<EmployeeContractDTO> findAll() throws SQLException {
        List<EmployeeContractDTO> contracts = new ArrayList<>();
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                contracts.add(mapResultSetToContractDTO(rs));
            }
        }
        return contracts;
    }

    @Override
    public List<EmployeeContractDTO> findActiveContracts() throws SQLException {
        List<EmployeeContractDTO> contracts = new ArrayList<>();
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_ACTIVE);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                contracts.add(mapResultSetToContractDTO(rs));
            }
        }
        return contracts;
    }

    @Override
    public List<EmployeeContractDTO> findByStartDate(String startDate) throws SQLException {
        List<EmployeeContractDTO> contracts = new ArrayList<>();
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_BY_START_DATE)) {

            // המרת מ-String ל-Date לפני ההצבה ב-PreparedStatement
            stmt.setDate(1, startDate != null ? Date.valueOf(startDate) : null);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contracts.add(mapResultSetToContractDTO(rs));
            }
        }
        return contracts;
    }

    @Override
    public List<EmployeeContractDTO> findByDateRange(String startDate, String endDate) throws SQLException {
        List<EmployeeContractDTO> contracts = new ArrayList<>();
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(SELECT_BY_DATE_RANGE)) {

            stmt.setDate(1, startDate != null ? Date.valueOf(startDate) : null);
            stmt.setDate(2, endDate != null ? Date.valueOf(endDate) : null);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contracts.add(mapResultSetToContractDTO(rs));
            }
        }
        return contracts;
    }

    @Override
    public void save(EmployeeContractDTO contract) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(INSERT)) {

            stmt.setString(1, contract.getId());
            stmt.setString(2, contract.getEmployeeID());
            stmt.setDate(3, contract.getStartDate() != null ? Date.valueOf(contract.getStartDate()) : null);
            stmt.setInt(4, contract.getFreeDays());
            stmt.setInt(5, contract.getSicknessDays());
            stmt.setInt(6, contract.getMonthlyWorkHours());
            stmt.setString(7, contract.getSocialContributions());
            stmt.setString(8, contract.getAdvancedStudyFund());
            stmt.setInt(9, contract.getSalary());
            stmt.setString(10, contract.getArchivedAt());
            stmt.setBoolean(11, contract.isArchived());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(EmployeeContractDTO contract) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(UPDATE)) {

            stmt.setString(1, contract.getEmployeeID());
            stmt.setDate(2, contract.getStartDate() != null ? Date.valueOf(contract.getStartDate()) : null);
            stmt.setInt(3, contract.getFreeDays());
            stmt.setInt(4, contract.getSicknessDays());
            stmt.setInt(5, contract.getMonthlyWorkHours());
            stmt.setString(6, contract.getSocialContributions());
            stmt.setString(7, contract.getAdvancedStudyFund());
            stmt.setInt(8, contract.getSalary());
            stmt.setString(9, contract.getArchivedAt());
            stmt.setBoolean(10, contract.isArchived());
            stmt.setString(11, contract.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(DELETE)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteByEmployeeId(String employeeId) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(DELETE_BY_EMPLOYEE_ID)) {

            stmt.setString(1, employeeId);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean exists(String id) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(EXISTS)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    @Override
    public boolean existsByEmployeeId(String employeeId) throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(EXISTS_BY_EMPLOYEE_ID)) {

            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    @Override
    public int getContractCount() throws SQLException {
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(COUNT);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    private EmployeeContractDTO mapResultSetToContractDTO(ResultSet rs) throws SQLException {
        String dateStr = null;

        // תומך במקרה שבו start_date הוא מספר מילישניות מאז epoch
        long startDateMillis = rs.getLong("start_date");
        if (!rs.wasNull()) {
            LocalDate localDate = Instant.ofEpochMilli(startDateMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            dateStr = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE); // yyyy-MM-dd
        }

        String archivedAt = rs.getString("archived_at");

        return new EmployeeContractDTO(
                rs.getString("id"),
                rs.getString("employee_id"),
                dateStr,
                rs.getInt("free_days"),
                rs.getInt("sickness_days"),
                rs.getInt("monthly_work_hours"),
                rs.getString("social_contributions"),
                rs.getString("advanced_study_fund"),
                rs.getInt("salary"),
                archivedAt,
                rs.getBoolean("is_archived")
        );
    }


}
