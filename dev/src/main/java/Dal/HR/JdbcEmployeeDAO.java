package Dal.HR;

import DTO.HR.EmployeeDTO;
import DTO.HR.RoleDTO;
import DTO.HR.ShiftDTO;
import database.Database;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcEmployeeDAO implements EmployeeDAO {

    private EmployeeDTO mapResultSetToEmployeeDTO(ResultSet rs) throws SQLException {
        return new EmployeeDTO(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("phone_number"),
                rs.getString("branch_id"),
                rs.getString("role_ids"),
                rs.getInt("salary"),
                rs.getString("contract_id"),
                rs.getString("bank_details"),
                rs.getBoolean("is_archived"),
                rs.getString("archived_at"),
                rs.getBoolean("is_manager")
        );
    }

    @Override
    public Optional<EmployeeDTO> findById(String id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSetToEmployeeDTO(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<EmployeeDTO> findAll() throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(mapResultSetToEmployeeDTO(rs));
            }
        }
        return employees;
    }

    @Override
    public List<EmployeeDTO> findActiveEmployees() throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE is_archived = false";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(mapResultSetToEmployeeDTO(rs));
            }
        }
        return employees;
    }

    @Override
    public List<EmployeeDTO> findByBranch(String branchId) throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE branch_id = ? AND is_archived = false";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, branchId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployeeDTO(rs));
                }
            }
        }
        return employees;
    }

    @Override
    public List<EmployeeDTO> findManagers() throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE is_manager = true AND is_archived = false";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(mapResultSetToEmployeeDTO(rs));
            }
        }
        return employees;
    }

    @Override
    public Optional<EmployeeDTO> findByIdAndPassword(String id, String password) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ? AND password = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSetToEmployeeDTO(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public void save(EmployeeDTO employee) throws SQLException {
        String sql = "INSERT INTO employees (id, name, phone_number, branch_id, role_ids, salary, contract_id, bank_details, is_archived, archived_at, is_manager, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, employee.getId());
            ps.setString(2, employee.getName());
            ps.setString(3, employee.getPhoneNumber());
            ps.setString(4, employee.getBranchId());
            ps.setString(5, employee.getRoleIds());
            ps.setInt(6, employee.getSalary());
            ps.setString(7, employee.getContractId());
            ps.setString(8, employee.getBankDetails());
            ps.setBoolean(9, employee.isArchived());
            ps.setString(10, employee.getArchivedAt());
            ps.setBoolean(11, employee.isManager());
            ps.setString(12, null);
            ps.executeUpdate();
        }
    }

    @Override
    public void update(EmployeeDTO employee) throws SQLException {
        String sql = "UPDATE employees SET name = ?, phone_number = ?, branch_id = ?, role_ids = ?, salary = ?, contract_id = ?, bank_details = ?, is_archived = ?, archived_at = ?, is_manager = ?, password = ? WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getPhoneNumber());
            ps.setString(3, employee.getBranchId());
            ps.setString(4, employee.getRoleIds());
            ps.setInt(5, employee.getSalary());
            ps.setString(6, employee.getContractId());
            ps.setString(7, employee.getBankDetails());
            ps.setBoolean(8, employee.isArchived());
            ps.setString(9, employee.getArchivedAt());
            ps.setBoolean(10, employee.isManager());
            ps.setString(11, null);
            ps.setString(12, employee.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean exists(String id) throws SQLException {
        String sql = "SELECT 1 FROM employees WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public int getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees WHERE is_archived = false";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<List<EmployeeDTO>> findAvailableForShift(ShiftDTO targetShift, RoleDTO requiredRole) throws SQLException {
        List<EmployeeDTO> available = new ArrayList<>();
        List<EmployeeDTO> unavailable = new ArrayList<>();

        String weekStartDate = (LocalDate.now().with(DayOfWeek.SUNDAY)).toString();

        String sql = """
    SELECT DISTINCT e.*, 
           CASE 
               WHEN wp.preferred_shift_ids_csv IS NOT NULL 
                    AND (wp.preferred_shift_ids_csv LIKE ? 
                         OR wp.preferred_shift_ids_csv LIKE ?
                         OR wp.preferred_shift_ids_csv LIKE ?
                         OR wp.preferred_shift_ids_csv = ?)
               THEN true 
               ELSE false 
           END as has_requested_shift
    FROM employees e
    INNER JOIN EmployeeRoles er ON e.id = er.employee_id
    LEFT JOIN weekly_preferences wp ON e.id = wp.employee_id 
                                   AND wp.week_start_date = ?
    WHERE e.is_archived = false
    AND er.role_id = ?
""";

        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            String shiftId = targetShift.getId();

            // כל הדרכים האפשריות שה-shift_id יכול להופיע ב-CSV:
            ps.setString(1, shiftId + ",%");        // בתחילת הרשימה
            ps.setString(2, "%," + shiftId + ",%"); // באמצע הרשימה
            ps.setString(3, "%," + shiftId);        // בסוף הרשימה
            ps.setString(4, shiftId);               // היחיד ברשימה
            ps.setString(5, weekStartDate);
            ps.setString(6, requiredRole.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EmployeeDTO employee = mapResultSetToEmployeeDTO(rs);
                    boolean hasRequestedShift = rs.getBoolean("has_requested_shift");
                    (hasRequestedShift ? available : unavailable).add(employee);
                }
            }
        }

        List<List<EmployeeDTO>> result = new ArrayList<>();
        result.add(available);
        result.add(unavailable);
        return result;
    }



    public List<EmployeeDTO> findQualified(String branchId, String roleId) throws SQLException {
        List<EmployeeDTO> qualifiedEmployees = new ArrayList<>();
        String sql = """
            SELECT e.*
            FROM Employees e
            JOIN EmployeeRoles er ON e.id = er.employee_id
            WHERE e.branch_id = ? AND er.role_id = ? AND e.is_archived = false
            """;

        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setString(1, branchId);
            stmt.setString(2, roleId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmployeeDTO dto = mapResultSetToEmployeeDTO(rs);
                    qualifiedEmployees.add(dto);
                }
            }
        }
        return qualifiedEmployees;
    }
}
